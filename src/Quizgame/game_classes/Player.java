package Quizgame.game_classes;
import java.util.ArrayList;
import java.util.List;

public class Player {
  private String id;
  private String name;
  private List<GameSession> currentGames;

  public Player(String id, String name, List<GameSession> currentGames) {
    this.id = id;
    this.name = name;
    this.currentGames = currentGames;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public List<GameSession> getGameSessions(){
    return currentGames;
  }
}

