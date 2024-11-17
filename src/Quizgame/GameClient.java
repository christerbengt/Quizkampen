package Quizgame;

import Quizgame.GUI.QuizCampenGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameClient {
    private Socket serverConnection;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final QuizCampenGUI gui;
    private final String serverAddress;
    private final int serverPort;
    private volatile boolean connected;
    private String clientId;

    // Executor for handling incoming messages
    private final ExecutorService messageHandler;

    public GameClient(String serverAddress, int serverPort, QuizCampenGUI gui) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.gui = gui;
        this.messageHandler = Executors.newSingleThreadExecutor();
        this.connected = false;
    }

    // Establishes connection with the game server
    public boolean connect() {
        try {
            // Create socket connection
            serverConnection = new Socket(serverAddress, serverPort);

            // Create streams - order is important to prevent deadlock
            out = new ObjectOutputStream(serverConnection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(serverConnection.getInputStream());

            connected = true;

            // Start message receiving thread
            messageHandler.execute(this::handleServerMessages);

            System.out.println("Connected to server at " + serverAddress + ":" + serverPort);
            return true;

        } catch (IOException e) {
            System.err.println("Could not connect to server " + e.getMessage());
            disconnect();
            return false;
        }
    }

    // Handles incoming messages from server
    private void handleServerMessages() {
        try {
            while (connected && !serverConnection.isClosed()) {
                Object message = in.readObject();
                if (message instanceof GameServer.Message) {
                    processServerMessage ((GameServer.Message) message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if (connected) {
                System.err.println("Error receiving server message: " + e.getMessage());
            }
        }
    }
    // Process messages received from server
    private void processServerMessage(GameServer.Message message) {
        switch (message.getType()) {
            case "Welcome":
                // Store client ID assigned by server
                clientId = message.getContent();
                gui.updateStatus("Connected as client: " + clientId);
                break;

            case "ECHO":
                // Handle echo message from server
                gui.updateStatus("Server echo: " + message.getContent());
                break;

            default:
                System.out.println("Unknown message type: " + message.getType());
        }
    }

    // Sends a message to server
    public void sendToServer(String type, String content) {
        if (connected) {
            try {
                GameServer.Message message = new GameServer.Message(type, content);
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("Error sending server message: " + e.getMessage());
                disconnect();
            }
        }
    }
    // Disconnects from server
    public void disconnect() {
        connected = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (serverConnection != null) serverConnection.close();
            messageHandler.shutdown();
            gui.updateStatus("Disconnected from server");
        } catch (IOException e) {
            System.err.println("Error disconnecting from server: " + e.getMessage());
        }
    }
    // Returns connection status
    public boolean isConnected() {
        return connected;
    }
    // Returns the client ID given by server
    public String getClientId() {
        return clientId;
    }
}
