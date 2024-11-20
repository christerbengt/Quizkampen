package Quizgame.game_classes;

import Quizgame.server.database.Question;

import java.util.List;
import java.util.Random;

public class Round {
  final private String category;
  final private List<Question> questionList;
  // private Map<Player, List<Answer>> playerAnswers;
  private boolean isComplete;

  public Round(String category, List<Question> questionList) {
    this.category = category;
    this.questionList = questionList;
    this.isComplete = false;

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

  public Question getRandomQuestion() {
    if (questionList == null || questionList.isEmpty()) {
      return null;
    }
    Random random = new Random();
    int randomIndex = random.nextInt(questionList.size());
    return questionList.get(randomIndex);

  }


  public int getScore() {
    return 0;
  }
}
