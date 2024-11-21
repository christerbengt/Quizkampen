package Quizgame.game_classes;

import Quizgame.server.database.Question;

import java.util.List;

public class GameSession {
  private final String sessionId;
  private final Player player1;
  private final Player player2;
  private final int rounds;
  private int currentRoundIndex;
  private int scorePlayer1 = 0;
  private int scorePlayer2 = 0;
  private Round currentRound;

  public GameSession(String sessionId, Player player1, Player player2, int rounds, int currentRoundIndex, String topic) {
    this.sessionId = sessionId;
    this.player1 = player1;
    this.player2 = player2;
    this.rounds = rounds;
    this.currentRoundIndex = currentRoundIndex;
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
  public void isAnswerCorrect(Player p, String answer, Question question) {
    if (answer.equals(question.getCorrectAnswer())){
      if (p.equals(player1)) {
        scorePlayer1++;
      } else {
        scorePlayer2++;
      }
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

  public int getScore(Player player) {
    if (player.equals(player1)) {
      return scorePlayer1;
    } else {
      return scorePlayer2;
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

  ////////////////////////////////////////////////////////
  //
  //  Input: (String) User answer and correct answer.
  //  Return: boolean
  //
  ////////////////////////////////////////////////////////
  public boolean isQuestionCorrect(String answer, String correctAnswer) {
    return answer.equalsIgnoreCase(correctAnswer);
  }
}


