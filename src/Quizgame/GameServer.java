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
    private final Map<String, GameSession> activeSessions;
    private final GameLogic gameLogic;
    private volatile boolean running;

    public GameServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientHandlers = Executors.newCachedThreadPool();
        this.connectedClients = new ConcurrentHashMap<>();
        this.waitingPlayers = new ConcurrentLinkedQueue<>();
        this.activeSessions = new ConcurrentHashMap<>();
        this.gameLogic = new GameLogic(new QuestionDatabase("files/")); //Adjust path as needed
        this.running = false;
    }

    /**
     * Starts the server and begins accepting client connections
     */
    public void start() {
        running = true;
        System.out.println("Game Server starting on port " + serverSocket.getLocalPort());

        startMatchmaking();

        // Main server loop
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
        activeSessions.put(session.getSessionId(), session);

        Message gameStartMsg = new Message("GAME_START", session.getSessionId());
        connectedClients.get(player1Id).sendToClient(gameStartMsg);
        connectedClients.get(player2Id).sendToClient(gameStartMsg);
    }

    /**
     * Handles communication with a single client
     */
    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ObjectInputStream in;
        private final ObjectOutputStream out;
        private final String clientId;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.clientId = UUID.randomUUID().toString();

            // Initialize streams
            ObjectOutputStream tempOut = null;
            ObjectInputStream tempIn = null;
            try {
                // Must create output stream first to prevent deadlock
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
                // Register client
                connectedClients.put(clientId, this);
                System.out.println("New client connected: " + clientId);

                // Send welcome message to client
                sendToClient(new Message("WELCOME", clientId));

                // Start message handling loop
                while (running && !clientSocket.isClosed()) {
                    try {
                        Object message = in.readObject();
                        handleClientMessage(message);
                    } catch (ClassNotFoundException e) {
                        System.err.println("Error reading message from client " + clientId + ": " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error in client handler: " + e.getMessage());
            } finally {
                disconnect();
            }
        }

        /**
         * Handles incoming messages from the client
         */
        private void handleClientMessage(Object message) {
            if (message instanceof Message) {
                Message msg = (Message) message;
                System.out.println("Received from " + clientId + ": " + msg.getType());

                // Echo message back to client for now
                sendToClient(new Message("ECHO", msg.getContent()));
            }
        }

        /**
         * Sends a message to the client
         */
        private void sendToClient(Message message) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("Error sending message to client " + clientId + ": " + e.getMessage());
                disconnect();
            }
        }

        /**
         * Closes the client connection and cleans up resources
         */
        public void disconnect() {
            try {
                connectedClients.remove(clientId);
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
                System.out.println("Client disconnected: " + clientId);
            } catch (IOException e) {
                System.err.println("Error closing client connection: " + e.getMessage());
            }
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            GameServer server = new GameServer(5000);
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }
}
