package Quizgame.server_labs;

import Quizgame.game_classes.Player;
import Quizgame.server.database.Question;
import Quizgame.server.database.QuestionDatabase;

import java.util.Collections;
import java.util.List;

public class ServerProtocol {
    private final QuestionDatabase questionDatabase = new QuestionDatabase("files/");
    private final Question[] roundQuestions = new Question[2];
    public int round;
    public int numberOfRounds;
    public boolean isGameOver = false;
    private int counterQuestion1 = 0;
    private int counterQuestion2 = 0;
    Player player1;
    Player player2;


    public ServerProtocol(int numberOfRounds) {
        this.round = 0;
        this.numberOfRounds = numberOfRounds;
    }

    public boolean isGameOver(){
        return round == numberOfRounds;
    }

    public boolean isQuestionOneComplete() {
        return counterQuestion1 > 3;
    }

    public boolean isQuestionTwoComplete() {
        return counterQuestion2 > 1;
    }

    public Question question1(){
        counterQuestion1++;
        return roundQuestions[0];
    }

    public Question question2(){
        counterQuestion2++;
        return roundQuestions[1];
    }

    public QuestionDatabase getQuestionDatabase() {
        return questionDatabase;
    }

    public void setRoundQuestions(String topic) {
        List<Question> questions = questionDatabase.getQuestionsByCategory(topic);
        Collections.shuffle(questions);
        roundQuestions[0] = questions.get(0);
        roundQuestions[1] = questions.get(1);
    }

    public String output(String clientInput, SSPlayer currentPlayer, SSPlayer opponentPlayer){
        if (currentPlayer.getState() == 0 && opponentPlayer.getState() == 0){
            currentPlayer.setState(1);
            return questionDatabase.getQuestionCategories();
        } else if (opponentPlayer.getState() == 0){
            opponentPlayer.setState(1);
        }

        return "Invalid input";
    }
}
