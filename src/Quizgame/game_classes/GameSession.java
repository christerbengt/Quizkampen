package Quizgame.game_classes;

import Quizgame.server.database.Question;

import java.util.List;

public class GameSession {
  private final Player player1;
  private final Player player2;
  private final int rounds;
  private int currentRoundIndex;
  private int scorePlayer1 = 0;
  private int scorePlayer2 = 0;
  private Round currentRound;

  public GameSession(int rounds, String p1, String p2) {
    //this.sessionId = sessionId;
    this.player1 = new Player(p1);
    this.player2 = new Player(p2);
    this.rounds = rounds;
    this.currentRoundIndex = 1;
  }

  public void createNewRound(String topic){
    currentRound = new Round(topic);
  }

  /////////////////////////////////////////////////////////////////
  //
  //  If question is correct the current player get a point added.
  //  Input: Player, String answer, Question question
  //
  ////////////////////////////////////////////////////////////////
  public boolean isAnswerCorrect(String playerName, String answer, Question question) {
    if (answer.equalsIgnoreCase(question.getCorrectAnswer())){
      if (playerName.equals(player1.getName())){
        player1.addPoint();
      } else {
        player2.addPoint();
      }
      return true;
    } else {
      return false;
    }
  }

  /////////////////////////////////////////////////////////////
  //
  //  Return: List<Questions> To be passed to server -> Client
  //
  ////////////////////////////////////////////////////////////
  public List<Question> getQuestions() {
    return currentRound.getRandomQuestionList();
  }

  public boolean isRoundComplete() {
    return currentRoundIndex >= rounds;
  }

  /////////////////////////////////////////////////////////////
  //
  //  Input:
  //  Return: int score representation for Player
  //
  ////////////////////////////////////////////////////////////
  public int getScore(String player) {
    if (player.equals(player1.getName())){
      return player1.getScore();
    } else {
      return player2.getScore();
    }
  }

  /////////////////////////////////////////////////////////////
  //
  //  Return: ToString like string för display @Client.
  //
  ////////////////////////////////////////////////////////////
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


