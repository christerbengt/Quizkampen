package Quizgame.game_classes;

import java.util.List;

public class GameSession {
  private final String sessionId;
  private final Player player1;
  private final Player player2;
  private final int rounds;
  private int currentRoundIndex;
  private int scorePlayer1 = 0;
  private int scorePlayer2 = 0;
  // private final List<Round> roundList;

  public GameSession(String sessionId, Player player1, Player player2, int rounds, int currentRoundIndex) {
    this.sessionId = sessionId;
    this.player1 = player1;
    this.player2 = player2;
    this.rounds = rounds;
    this.currentRoundIndex = currentRoundIndex;

  }

  public String getSessionId() {
    return sessionId;
  }

  public void addPoint(Player player) {
    if (player.equals(player1)) {
      scorePlayer1++;
    } else if (player.equals(player2)) {
      scorePlayer2++;
    }
  }

  public boolean isRoundComplete() {
    return currentRoundIndex >= rounds;
  }

  public int getScore(Player player) {
    if (player.equals(player1)) {
      return scorePlayer1;
    } else {
      return scorePlayer2;
    }
  }

  public String getScoreBoard() {
      return "Score" + player1.getName() + ":" + scorePlayer1 + "Score" + player2.getName() + ":" + scorePlayer2;
  }

  public Player getPlayer1() {
    return player1;
  }
  public Player getPlayer2() {
    return player2;
  }
}


