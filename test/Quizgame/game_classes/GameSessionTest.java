package Quizgame.game_classes;

import Quizgame.server.database.Question;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    GameSession gameSession = new GameSession(3, "Bosse", "Kent-Agent");
    Question stupidQuestion = new Question("Bla", "x", "xo", "xoxox", "xoxoxo", "hej", "Stupidid");

    @Test
    void getQuestions() {
        gameSession.createNewRound("Music");
        List<Question> questions = gameSession.getQuestions();
        assertTrue(gameSession.isAnswerCorrect("Bosse", "Hej", stupidQuestion));
        System.out.println("Player got: " + gameSession.getScore("Bosse") + " points");
    }
}