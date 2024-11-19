package Quizgame;

import java.io.Serializable;

/**
 * Protocol class that handles message formatting and parsing
 */
public class GameProtocol {
    // Message types
    public static final String CONNECT = "CONNECT";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String WELCOME = "WELCOME";
    public static final String ERROR = "ERROR";

    /**
     * Message class for all communication
     */
    public static class Message implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String type;
        private final String content;

        public Message(String type, String content) {
            this.type = type;
            this.content = content;
        }

        public String getType() { return type; }
        public String getContent() { return content; }
    }

    /**
     * Creates welcome message with client ID
     */
    public Message createWelcomeMessage(String clientId) {
        return new Message(WELCOME, clientId);
    }

    /**
     * Creates error message
     */
    public Message createErrorMessage(String error) {
        return new Message(ERROR, error);
    }

    /**
     * Handles incoming message and returns appropriate response
     */
    public Message handleMessage(Message message, String clientId) {
        switch (message.getType()) {
            case CONNECT:
                return createWelcomeMessage(clientId);
            case DISCONNECT:
                return new Message(DISCONNECT, "Goodbye " + clientId);
            default:
                return createErrorMessage("Unknown message type: " + message.getType());
        }
    }
}
