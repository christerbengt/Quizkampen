package Quizgame.server.database;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionDatabaseTest {

    QuestionDatabase database = new QuestionDatabase("files/");

    @Test
    public void getQuestionsByCategory() {
        assertEquals(20, database.getQuestionsByCategory("Sport").size());
        for (int i = 0; i < database.getQuestionsByCategory("Sport").size(); i++) {
            System.out.println(database.getQuestionsByCategory("Sport").get(i).getQuestion());
        }
    }
}