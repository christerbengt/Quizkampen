package Quizgame.game_classes;

import Quizgame.server.database.Question;
import Quizgame.server.database.QuestionDatabase;

import java.util.*;

public class Round {
    final private String category;
    private final List<Question> questionList;
    private final LinkedList<Question> randomQuestionList = new LinkedList<>();
    // private Map<Player, List<Answer>> playerAnswers;
    private boolean isComplete;

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

    public Question getQuestion(){
        return randomQuestionList.pop();
    }

    public LinkedList<Question> getRandomQuestionList() {
        return randomQuestionList;
    }

    public int getScore() {
        return 0;
    }
}
