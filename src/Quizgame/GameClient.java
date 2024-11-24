package Quizgame;

import Quizgame.client.GUI.QuizCampenGUI;
import Quizgame.GameProtocol.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameClient {
    private Socket serverConnection;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private QuizCampenGUI gui;
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
            serverConnection = new Socket(InetAddress.getLocalHost(), 5000);

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
                if (message instanceof Message) {
                    processServerMessage((Message) message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if (connected) {
                System.err.println("Error receiving server message: " + e.getMessage());
            }
        }
    }

    private void processServerMessage(Message message) {
        switch (message.getType()) {
            case "WELCOME":
                clientId = message.getContent(); // Store client ID
                System.out.println("Connected as client: " + clientId);
                break;

            case "GAME_START":
                gui.showNextPanel("Category"); // Proceed to the next panel
                break;

//            case "QUESTION":
//                gui.updateQuestionPanel(message.getContent()); // Update the question in the panel
//                break;
//
//            case "ANSWER_RESULT":
//                gui.updateStatus(message.getContent()); // Show feedback about the answer
//                break;
//
//            case "GAME_END":
//                gui.showFinalScores(message.getContent()); // Update scoreboard with final scores
//                break;

            default:
                System.out.println("Unknown message type: " + message.getType());
        }
    }


    // Sends a message to server
    public void sendToServer(String type, String content) {
        if (connected) {
            try {
                Message message = new Message(type, content);
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
            // gui.updateStatus("Disconnected from server");
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

    public static void main(String[] args) {
        try {
            String serverAddress = "localhost";
            int serverPort = 5000;

            // Parse command-line arguments
            if (args.length > 0) {
                serverAddress = args[0];
                if (args.length > 1) {
                    try {
                        serverPort = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid port number. Using default port: " + serverPort);
                    }
                }
            }

            QuizCampenGUI gui = new QuizCampenGUI(null);
            GameClient client = new GameClient(serverAddress, serverPort, gui);
            gui.setClient(client);

            if (client.connect()) {
                gui.setVisible(true);
            } else {
                System.err.println("Could not connect to server");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error initializing client: " + e.getMessage());
            System.exit(1);
        }
    }

    public void setGui(QuizCampenGUI gui) {
        this.gui = gui;
    }

    public QuizCampenGUI getGui() {
        return gui;
    }
}
