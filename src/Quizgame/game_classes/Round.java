package Quizgame.game_classes;

import Quizgame.server.database.Question;
import Quizgame.server.database.QuestionDatabase;

import java.util.*;

public class Round {
    private final List<Question> questionList;
    private final List<Question> randomQuestionList = new ArrayList<>();
    private boolean isComplete;
    private int counter = 0;

    public Round(String category) {
      this.isComplete = false;
      this.questionList = getQuestionByTopic(category);
      createRandomQuestionList();
    }

    private List<Question> getQuestionByTopic(String topic) {
        QuestionDatabase questionDatabase = new QuestionDatabase("files/");
        return questionDatabase.getQuestionsByCategory(topic);
    }

    public boolean isComplete() {
      return isComplete;
    }

    public void createRandomQuestionList() {
        Collections.shuffle(questionList);
        randomQuestionList.addAll(questionList.subList(0, 2));
    }


    public List<Question> getRandomQuestionList() {
        if (counter < 1){
            counter++;
        } else {
            isComplete = true;
        }
        return randomQuestionList;
    }
}
