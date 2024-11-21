package Quizgame.game_classes;

import Quizgame.server.database.Question;
import Quizgame.server.database.QuestionDatabase;

import java.util.*;

public class Round {
    final private String category;
    private final List<Question> questionList;
    private final List<Question> randomQuestionList = new ArrayList<>();
    // private Map<Player, List<Answer>> playerAnswers;
    private boolean isComplete;
    private int counter = 0;

    public Round(String category) {
      this.category = category;
      this.isComplete = false;
      this.questionList = getQuestionByTopic(category);
      createRandomQuestionList();
    }

    private List<Question> getQuestionByTopic(String topic) {
        QuestionDatabase questionDatabase = new QuestionDatabase("files/");
        return questionDatabase.getQuestionsByCategory(topic);
    }

    public Object getPlayer() {
        return null;
    }

    // public void submitAnswer(Player player, Answer answer) {
    // }


    // public int getRoundScore(Player player) {
    //  return 0;
    // }

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

    public int getScore() {
        return 0;
    }
}
