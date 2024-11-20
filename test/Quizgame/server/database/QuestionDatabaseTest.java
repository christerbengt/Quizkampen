package Quizgame.server.database;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionDatabaseTest {

    QuestionDatabase database = new QuestionDatabase("files/MusikQuiz_with_Category.csv");

    @Test
    public void getQuestionsByCategory() {
        assertEquals(20, database.getQuestionsByCategory("Music").size());
        for (int i = 0; i < database.getQuestionsByCategory("Music").size(); i++) {
            System.out.println(database.getQuestionsByCategory("Music").get(i).getQuestion());
        }
    }

    @Test
    public void isQuestionCorrect(){
        assertTrue(database.isQuestionCorrect("xxx", "XXX"));
    }
}