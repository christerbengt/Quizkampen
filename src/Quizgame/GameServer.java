package Quizgame;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    private final ServerSocket serverSocket;
    private final ExecutorService clientHandlers;
    private final Map<String, ClientHandler> connectedClients;
    private final GameProtocol protocol;
    private volatile boolean running;

    public GameServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(66666);
        this.clientHandlers = Executors.newCachedThreadPool();
        this.connectedClients = new ConcurrentHashMap<>();
        this.protocol = new GameProtocol();
        this.running = false;
    }

    public void start() {
        running = true;
        System.out.println("Server started on port " + serverSocket.getLocalPort());

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                handleNewClient(clientSocket);
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client: " + e.getMessage());
                }
            }
        }
    }

    private void handleNewClient(Socket clientSocket) {
        try {
            ClientHandler handler = new ClientHandler(clientSocket);
            clientHandlers.execute(handler);
        } catch (IOException e) {
            System.err.println("Failed to initialize client handler: " + e.getMessage());
            try {
                clientSocket.close();
            } catch (IOException closeError) {
                System.err.println("Failed to close socket after handler initialization failure: " + closeError.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
        try {
            serverSocket.close();
            clientHandlers.shutdown();
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final ObjectInputStream in;
        private final ObjectOutputStream out;
        private final String clientId;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.clientId = UUID.randomUUID().toString();
            connectedClients.put(clientId, this);
        }

        @Override
        public void run() {
            try {
                // Send welcome message
                sendMessage(protocol.createWelcomeMessage(clientId));

                // Message handling loop
                while (running && !socket.isClosed()) {
                    GameProtocol.Message message = (GameProtocol.Message) in.readObject();
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error handling client " + clientId + ": " + e.getMessage());
            } finally {
                disconnect();
            }
        }

        private void handleMessage(GameProtocol.Message message) throws IOException {
            GameProtocol.Message response = protocol.handleMessage(message, clientId);
            sendMessage(response);

            if (message.getType().equals(GameProtocol.DISCONNECT)) {
                disconnect();
            }
        }

        private void sendMessage(GameProtocol.Message message) throws IOException {
            out.writeObject(message);
            out.flush();
        }

        private void disconnect() {
            try {
                connectedClients.remove(clientId);
                socket.close();
            } catch (IOException e) {
                System.err.println("Error disconnecting client " + clientId + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        int port = 12345; // You can change this default port

        try {
            GameServer server = new GameServer(port);
            // Add shutdown hook to handle ctrl+c gracefully
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down server...");
                server.stop();
            }));

            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
