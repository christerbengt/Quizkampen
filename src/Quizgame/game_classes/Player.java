package Quizgame.game_classes;
import java.util.List;

public class Player {
  private final String name;
  private int score = 0;

  public Player(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getScore() {
    return score;
  }

  public void addPoint() {
    this.score++;
  }

/*  public String getId() {
    return id;
  }*/

/*  public List<GameSession> getGameSessions(){
    return currentGames;
  }*/

/*  public int getScore() {
    int totalScore = 0;
    for (GameSession gameSession : currentGames) {
      totalScore += gameSession.getScore(this);
    }
    return totalScore;
  }*/

}


