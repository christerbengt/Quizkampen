package Quizgame.game_classes;

import Quizgame.GameProtocol;
import Quizgame.server.database.Question;
import Quizgame.server.database.QuestionDatabase;

import java.util.*;

public class GameLogic {
    private enum GameState {
        WAITING_FOR_PLAYERS,
        QUESTION_DISPLAY,
        AWAITING_ANSWERS,
        SHOWING_RESULTS,
        GAME_END
    }

    private GameState currentState;
    private final QuestionDatabase questionDB;
    private GameSession currentSession;
    private Question currentQuestion;
    private final Map<String, Player> players;
    private int questionNumber;
    private static final int QUESTIONS_PER_GAME = 10; // Total questions per game

    public GameLogic(QuestionDatabase questionDB) {
        this.questionDB = questionDB;
        this.players = new HashMap<>();
        this.currentState = GameState.WAITING_FOR_PLAYERS;
    }

    // Server-side message handling
    public GameProtocol.Message handleGameMessage(GameProtocol.Message message, String clientId) {
        switch (message.getType()) {
            case "JOIN_GAME":
                return handlePlayerJoin(clientId, message.getContent());

            case "ANSWER_CHOICE":
                return handleAnswerChoice(clientId, message.getContent());

            case "READY_FOR_NEXT":
                return handleReadyForNext(clientId);

            default:
                return new GameProtocol.Message("ERROR", "Unknown game message type");
        }
    }

    private GameProtocol.Message handlePlayerJoin(String clientId, String playerName) {
        if (currentState != GameState.WAITING_FOR_PLAYERS) {
            return new GameProtocol.Message("ERROR", "Cannot join game in progress");
        }

        Player newPlayer = new Player(clientId, playerName, new ArrayList<>());
        players.put(clientId, newPlayer);

        // If we have enough players, start the game
        if (players.size() >= 2) { // Minimum 2 players
            startNewGame();
            return new GameProtocol.Message("GAME_START", "The game is beginning!");
        }

        return new GameProtocol.Message("WAIT", "Waiting for more players...");
    }

    private GameProtocol.Message handleAnswerChoice(String clientId, String choiceIndex) {
        if (currentState != GameState.AWAITING_ANSWERS) {
            return new GameProtocol.Message("ERROR", "Not accepting answers at this time");
        }

        Player player = players.get(clientId);
        try {
            int choice = Integer.parseInt(choiceIndex);
            boolean isCorrect = currentQuestion.isCorrectAnswer(choice);

            if (isCorrect) {
                currentSession.addPoint(player); // Simple scoring - just add one point
                return new GameProtocol.Message("ANSWER_RESULT", "Correct!");
            } else {
                return new GameProtocol.Message("ANSWER_RESULT", "Incorrect!");
            }
        } catch (NumberFormatException e) {
            return new GameProtocol.Message("ERROR", "Invalid answer choice");
        }
    }

    private GameProtocol.Message handleReadyForNext(String clientId) {
        if (currentState != GameState.SHOWING_RESULTS) {
            return new GameProtocol.Message("ERROR", "Not ready for next question");
        }

        if (questionNumber >= QUESTIONS_PER_GAME) {
            currentState = GameState.GAME_END;
            return new GameProtocol.Message("GAME_END", generateFinalResults());
        } else {
            sendNextQuestion();
            return new GameProtocol.Message("NEXT_QUESTION", "Question " + questionNumber + " beginning");
        }
    }

    private void startNewGame() {

        if (players.size() < 2) {
            throw new IllegalStateException("Not enough players to start a game");
        }

        Player[] playerArray = players.values().toArray(new Player[0]);
        int numRounds = 2;
        int roundIndex = 0;
        currentSession = new GameSession(
                UUID.randomUUID().toString(),
                playerArray[0],
                playerArray[1],
                numRounds,
                roundIndex
                );

        playerArray[0].getGameSessions().add(currentSession);
        playerArray[1].getGameSessions().add(currentSession);
        questionNumber = 0;
        currentState = GameState.QUESTION_DISPLAY;
        sendNextQuestion();
    }

    private void sendNextQuestion() {
        questionNumber++;
        currentQuestion = questionDB.getRandomQuestion();
        currentState = GameState.AWAITING_ANSWERS;

        // Format question data to include question text and all choices
        StringBuilder questionData = new StringBuilder();
        questionData.append(currentQuestion.getQuestionText()).append("|");
        for (String choice : currentQuestion.getChoices()) {
            questionData.append(choice).append("|");
        }

        broadcastToAllPlayers(new GameProtocol.Message("QUESTION", questionData.toString()));
    }

    private String generateFinalResults() {
        // Sort players by score
        List<Player> sortedPlayers = new ArrayList<>(players.values());
        sortedPlayers.sort((p1, p2) -> p2.getScore() - p1.getScore());

        StringBuilder results = new StringBuilder();
        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            results.append(String.format("%d. %s - %d points;",
                    i + 1,
                    player.getName(),
                    player.getScore()));
        }
        return results.toString();
    }

    // Method to broadcast message to all players
    private void broadcastToAllPlayers(GameProtocol.Message message) {

    }
}

