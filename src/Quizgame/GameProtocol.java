package Quizgame;

import java.io.Serializable;
import java.util.List;

/**
 * Protocol class that handles message formatting and parsing
 */
public class GameProtocol {
    // Message types
    public static final String CONNECT = "CONNECT";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String WELCOME = "WELCOME";
    public static final String ERROR = "ERROR";
    public static final String JOIN_GAME = "JOIN_GAME";
    public static final String GAME_START = "GAME_START";
    public static final String QUESTION = "QUESTION";
    public static final String ANSWER_CHOICE = "ANSWER_CHOICE";
    public static final String ANSWER_RESULT = "ANSWER_RESULT";
    public static final String GAME_END = "GAME_END";

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
    public static Message createWelcomeMessage(String clientId) {
        return new Message(WELCOME, clientId);
    }

    /**
     * Creates error message
     */
    public static Message createErrorMessage(String error) {
        return new Message(ERROR, error);
    }

    /**
     * Creates a question message with choices
     */
    public static Message createQuestionMessage(String questionText, List<String> choices) {
        StringBuilder data = new StringBuilder(questionText).append("|");
        for (String choice : choices) {
            data.append(choice).append("|");
        }
        return new Message(QUESTION, data.toString().substring(0, data.length() - 1));
    }

    /**
     * Creates answer result message
     */
    public static Message createAnswerResultMessage(boolean correct) {
        return new Message(ANSWER_RESULT, correct ? "Correct!" : "Incorrect!");
    }

    /**
     * Creates game end message with final scores
     */
    public static Message createGameEndMessage(String results) {
        return new Message(GAME_END, results);
    }

    /**
     * Creates game start message
     */
    public static Message createGameStartMessage(int playerCount) {
        return new Message(GAME_START, "Game starting with " + playerCount + " players!");
    }

    /**
     * Handles incoming message and returns appropriate response
     */
    public static Message handleMessage(Message message, String clientId) {
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