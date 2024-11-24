package Quizgame.game_classes;

import Quizgame.GameProtocol;
import Quizgame.server.database.Question;
import Quizgame.server.database.QuestionDatabase;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameLogic {
    private enum GameState {
        WAITING_FOR_PLAYERS,    // Initial state, waiting for two players
        CATEGORY_SELECTION,     // First player selecting category
        PLAYER1_TURN,          // First player answering questions
        PLAYER2_TURN,          // Second player answering questions
        ROUND_RESULTS,         // Showing results for the current round
        GAME_END               // Game is complete
    }

    private final QuestionDatabase questionDB;
    private final Map<String, GameSession> activeSessions;
    private final Map<String, Player> players;
    private final Map<String, GameState> sessionStates;
    private final Map<String, String> currentCategories;
    private final Map<String, List<Question>> currentRoundQuestions;
    private final Map<String, Integer> questionIndexes;
    private final Map<String, Map<String, List<Boolean>>> playerAnswers; // sessionId -> playerId -> answers

    // Configuration constants (could be moved to properties file)
    private static final int QUESTIONS_PER_ROUND = 2;
    private static final int TOTAL_ROUNDS = 6;

    public GameLogic(QuestionDatabase questionDB) {
        this.questionDB = questionDB;
        this.activeSessions = new ConcurrentHashMap<>();
        this.players = new ConcurrentHashMap<>();
        this.sessionStates = new ConcurrentHashMap<>();
        this.currentCategories = new ConcurrentHashMap<>();
        this.currentRoundQuestions = new ConcurrentHashMap<>();
        this.questionIndexes = new ConcurrentHashMap<>();
        this.playerAnswers = new ConcurrentHashMap<>();
    }

    public GameSession createNewGame(String player1Id, String player2Id) {
        String sessionId = UUID.randomUUID().toString();
        Player player1 = new Player(player1Id, "Player1", new ArrayList<>());
        Player player2 = new Player(player2Id, "Player2", new ArrayList<>());

        GameSession session = new GameSession(sessionId, player1, player2, TOTAL_ROUNDS, 0);

        // Initialize session data
        activeSessions.put(sessionId, session);
        players.put(player1Id, player1);
        players.put(player2Id, player2);
        sessionStates.put(sessionId, GameState.CATEGORY_SELECTION);
        playerAnswers.put(sessionId, new HashMap<>());
        playerAnswers.get(sessionId).put(player1Id, new ArrayList<>());
        playerAnswers.get(sessionId).put(player2Id, new ArrayList<>());
        questionIndexes.put(sessionId, 0);

        return session;
    }

    public GameProtocol.Message handleCategorySelection(String sessionId, String playerId, String category) {
        GameSession session = activeSessions.get(sessionId);
        if (session == null) {
            return new GameProtocol.Message("ERROR", "Invalid session");
        }

        GameState state = sessionStates.get(sessionId);
        if (state != GameState.CATEGORY_SELECTION) {
            return new GameProtocol.Message("ERROR", "Not in category selection phase");
        }

        // Only first player can select category
        if (!session.getPlayer1().getId().equals(playerId)) {
            return new GameProtocol.Message("ERROR", "Not your turn to select category");
        }

        // Set category and prepare questions
        currentCategories.put(sessionId, category);
        List<Question> questions = questionDB.getQuestionsByCategory(category);
        if (questions.size() < QUESTIONS_PER_ROUND) {
            return new GameProtocol.Message("ERROR", "Not enough questions in category");
        }

        // Randomly select questions for this round
        Collections.shuffle(questions);
        currentRoundQuestions.put(sessionId, questions.subList(0, QUESTIONS_PER_ROUND));

        // Update game state
        sessionStates.put(sessionId, GameState.PLAYER1_TURN);

        return new GameProtocol.Message("CATEGORY_SELECTED", "Category selected: " + category);
    }

    public GameProtocol.Message handleAnswer(String sessionId, String playerId, String answer) {
        GameSession session = activeSessions.get(sessionId);
        if (session == null) {
            return new GameProtocol.Message("ERROR", "Invalid session");
        }

        GameState state = sessionStates.get(sessionId);
        String currentPlayerId = (state == GameState.PLAYER1_TURN) ?
                session.getPlayer1().getId() : session.getPlayer2().getId();

        if (!playerId.equals(currentPlayerId)) {
            return new GameProtocol.Message("ERROR", "Not your turn");
        }

        int questionIndex = questionIndexes.get(sessionId);
        Question currentQuestion = currentRoundQuestions.get(sessionId).get(questionIndex);

        // Record answer
        boolean isCorrect = currentQuestion.getCorrectAnswer().equals(answer);
        playerAnswers.get(sessionId).get(playerId).add(isCorrect);

        // Update question index and check for turn completion
        questionIndex++;
        questionIndexes.put(sessionId, questionIndex);

        if (questionIndex >= QUESTIONS_PER_ROUND) {
            // Move to next player or round results
            if (state == GameState.PLAYER1_TURN) {
                sessionStates.put(sessionId, GameState.PLAYER2_TURN);
                questionIndexes.put(sessionId, 0); // Reset for player 2
                return new GameProtocol.Message("TURN_END", "Player 1 turn complete");
            } else {
                sessionStates.put(sessionId, GameState.ROUND_RESULTS);
                return new GameProtocol.Message("ROUND_COMPLETE", "Round complete");
            }
        }

        return new GameProtocol.Message("ANSWER_RECORDED",
                isCorrect ? "Correct!" : "Incorrect!");
    }

    public GameProtocol.Message getCurrentQuestion(String sessionId) {
        if (!currentRoundQuestions.containsKey(sessionId)) {
            return new GameProtocol.Message("ERROR", "No active questions");
        }

        int index = questionIndexes.get(sessionId);
        Question question = currentRoundQuestions.get(sessionId).get(index);

        return GameProtocol.createQuestionMessage(
                question.getQuestion(),
                Arrays.asList(
                        question.getAnswerOption1(),
                        question.getAnswerOption2(),
                        question.getAnswerOption3(),
                        question.getAnswerOption4()
                )
        );
    }

    public GameProtocol.Message getRoundResults(String sessionId) {
        GameSession session = activeSessions.get(sessionId);
        if (session == null) {
            return new GameProtocol.Message("ERROR", "Invalid session");
        }

        if (sessionStates.get(sessionId) != GameState.ROUND_RESULTS) {
            return new GameProtocol.Message("ERROR", "Round not complete");
        }

        // Calculate scores
        List<Boolean> player1Answers = playerAnswers.get(sessionId).get(session.getPlayer1().getId());
        List<Boolean> player2Answers = playerAnswers.get(sessionId).get(session.getPlayer2().getId());

        int player1Score = (int) player1Answers.stream().filter(b -> b).count();
        int player2Score = (int) player2Answers.stream().filter(b -> b).count();

        // Update session scores
        for (int i = 0; i < player1Score; i++) session.addPoint(session.getPlayer1());
        for (int i = 0; i < player2Score; i++) session.addPoint(session.getPlayer2());

        // Clear round data
        playerAnswers.get(sessionId).get(session.getPlayer1().getId()).clear();
        playerAnswers.get(sessionId).get(session.getPlayer2().getId()).clear();

        // Check if game is complete
        if (session.isRoundComplete()) {
            sessionStates.put(sessionId, GameState.GAME_END);
            return new GameProtocol.Message("GAME_COMPLETE", session.getScoreBoard());
        } else {
            sessionStates.put(sessionId, GameState.CATEGORY_SELECTION);
            return new GameProtocol.Message("ROUND_SCORES",
                    String.format("Player 1: %d, Player 2: %d", player1Score, player2Score));
        }
    }

    public void handlePlayerLeave(String sessionId, String playerId) {
        GameSession session = activeSessions.get(sessionId);
        if (session != null) {
            // Clean up session data
            activeSessions.remove(sessionId);
            sessionStates.remove(sessionId);
            currentCategories.remove(sessionId);
            currentRoundQuestions.remove(sessionId);
            questionIndexes.remove(sessionId);
            playerAnswers.remove(sessionId);

            // Clean up player data
            players.remove(playerId);
        }
    }
}
