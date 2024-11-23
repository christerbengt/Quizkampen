package Quizgame;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import Quizgame.GameProtocol.Message;
import Quizgame.game_classes.GameLogic;
import Quizgame.game_classes.GameSession;
import Quizgame.server.database.QuestionDatabase;

public class GameServer {
    private final ServerSocket serverSocket;
    private final ExecutorService clientHandlers;
    private final Map<String, ClientHandler> connectedClients;
    private final Queue<String> waitingPlayers;
    private final GameLogic gameLogic;
    private volatile boolean running;

    public GameServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientHandlers = Executors.newCachedThreadPool();
        this.connectedClients = new ConcurrentHashMap<>();
        this.waitingPlayers = new ConcurrentLinkedQueue<>();
        this.gameLogic = new GameLogic(new QuestionDatabase("files/"));
        this.running = false;
    }

    public void start() {
        running = true;
        System.out.println("Game Server starting on port " + serverSocket.getLocalPort());
        startMatchmaking();

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                handleNewClient(clientSocket);
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }

    private void handleNewClient(Socket clientSocket) {
        ClientHandler handler = new ClientHandler(clientSocket);
        clientHandlers.execute(handler);
    }

    private void startMatchmaking() {
        Thread matchmaker = new Thread(() -> {
            while (running) {
                matchPlayers();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        matchmaker.setDaemon(true);
        matchmaker.start();
    }

    private void matchPlayers() {
        while (waitingPlayers.size() >= 2) {
            String player1Id = waitingPlayers.poll();
            String player2Id = waitingPlayers.poll();

            if (player1Id != null && player2Id != null &&
                    connectedClients.containsKey(player1Id) &&
                    connectedClients.containsKey(player2Id)) {

                startNewGame(player1Id, player2Id);
            }
        }
    }

    private void startNewGame(String player1Id, String player2Id) {
        GameSession session = gameLogic.createNewGame(player1Id, player2Id);

        // Notify both players that game is starting and who goes first
        Message gameStartMsg = new Message("GAME_START", session.getSessionId());
        connectedClients.get(player1Id).sendToClient(gameStartMsg);
        connectedClients.get(player2Id).sendToClient(gameStartMsg);

        // Tell player 1 to select category
        Message selectCategoryMsg = new Message("SELECT_CATEGORY", "Please select a category");
        connectedClients.get(player1Id).sendToClient(selectCategoryMsg);

        // Tell player 2 they're waiting
        Message waitMsg = new Message("WAIT", "Waiting for Player 1 to select category");
        connectedClients.get(player2Id).sendToClient(waitMsg);
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ObjectInputStream in;
        private final ObjectOutputStream out;
        private final String clientId;
        private String currentSessionId;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.clientId = UUID.randomUUID().toString();

            ObjectOutputStream tempOut = null;
            ObjectInputStream tempIn = null;
            try {
                tempOut = new ObjectOutputStream(socket.getOutputStream());
                tempOut.flush();
                tempIn = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.err.println("Error creating streams for client " + clientId + ": " + e.getMessage());
            }
            this.out = tempOut;
            this.in = tempIn;
        }

        @Override
        public void run() {
            try {
                connectedClients.put(clientId, this);
                sendToClient(new Message("WELCOME", clientId));

                while (running && !clientSocket.isClosed()) {
                    Object message = in.readObject();
                    if (message instanceof Message) {
                        handleClientMessage((Message) message);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error in client handler: " + e.getMessage());
            } finally {
                disconnect();
            }
        }

        private void handleClientMessage(Message message) {
            switch (message.getType()) {
                case "JOIN_QUEUE":
                    waitingPlayers.offer(clientId);
                    sendToClient(new Message("QUEUE_STATUS", "Waiting for opponent..."));
                    break;

                case "CATEGORY_SELECT":
                    if (currentSessionId != null) {
                        Message response = gameLogic.handleCategorySelection(
                                currentSessionId, clientId, message.getContent());
                        broadcastToSession(response);

                        if (response.getType().equals("CATEGORY_SELECTED")) {
                            // Send first question to player 1
                            Message questionMsg = gameLogic.getCurrentQuestion(currentSessionId);
                            sendToClient(questionMsg);
                        }
                    }
                    break;

                case "ANSWER":
                    if (currentSessionId != null) {
                        Message response = gameLogic.handleAnswer(
                                currentSessionId, clientId, message.getContent());
                        handleAnswerResponse(response);
                    }
                    break;

                case "LEAVE_GAME":
                    if (currentSessionId != null) {
                        gameLogic.handlePlayerLeave(currentSessionId, clientId);
                        currentSessionId = null;
                    }
                    break;

                default:
                    System.out.println("Unknown message type: " + message.getType());
            }
        }

        private void handleAnswerResponse(Message response) {
            switch (response.getType()) {
                case "ANSWER_RECORDED":
                    // Send next question if available
                    Message nextQuestion = gameLogic.getCurrentQuestion(currentSessionId);
                    sendToClient(nextQuestion);
                    break;

                case "TURN_END":
                    // Switch to player 2's turn
                    broadcastToSession(new Message("TURN_SWITCH", "Switching to Player 2"));
                    Message firstQuestion = gameLogic.getCurrentQuestion(currentSessionId);
                    // Send to the other player
                    broadcastToSession(firstQuestion);
                    break;

                case "ROUND_COMPLETE":
                    Message results = gameLogic.getRoundResults(currentSessionId);
                    broadcastToSession(results);
                    break;

                case "GAME_COMPLETE":
                    broadcastToSession(response);
                    // Clean up session
                    currentSessionId = null;
                    break;
            }
        }

        public void sendToClient(Message message) {
            try {
                out.writeObject(message);
                out.flush();
                out.reset(); // Prevent object caching
            } catch (IOException e) {
                System.err.println("Error sending message to client " + clientId + ": " + e.getMessage());
                disconnect();
            }
        }

        private void broadcastToSession(Message message) {
            if (currentSessionId != null) {
                for (ClientHandler handler : connectedClients.values()) {
                    if (currentSessionId.equals(handler.currentSessionId)) {
                        handler.sendToClient(message);
                    }
                }
            }
        }

        public void disconnect() {
            try {
                connectedClients.remove(clientId);
                waitingPlayers.remove(clientId);
                if (currentSessionId != null) {
                    gameLogic.handlePlayerLeave(currentSessionId, clientId);
                }
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
                System.out.println("Client disconnected: " + clientId);
            } catch (IOException e) {
                System.err.println("Error closing client connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            GameServer server = new GameServer(5000);
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }
}