package Quizgame.game_classes;

import java.util.List;

public class GameSession {
  private final String sessionId;
  private final Player player1;
  private final Player player2;
  private final List<Round> rounds;
  private int currentRoundIndex;

  public GameSession(String sessionId, Player player1, Player player2, int rounds, int currentRoundIndex) {
    this.sessionId = sessionId;
    this.player1 = player1;
    this.player2 = player2;
    this.rounds = rounds;
    this.currentRoundIndex = 0;
  }

  /*

  public void handlePlayerMove(Player, Answer) {
  }

  public GameState getCurrentState() {
    return currentState;
  }

  public void setCurrentState(GameState currentState) {
    this.currentState = currentState;
  }

  */
  public boolean isRoundComplete() {
    return currentRoundIndex >= rounds.size();
  }

  public void getScoreBoard() {
  }

  public Player getPlayer1() {
    return player1;
  }

  public Player getPlayer2() {
    return player2;
  }

  public String getSessionId() {
    return sessionId;
  }


  public int getScore(Player player) {
    int score = 0;
    for (Round round : rounds) {
      if (round.getPlayer().equals(player)) {
        score += round.getScore(); // Assuming each Round has a method getScore() and getPlayer()
      }
    }
    return score;
  }
}
