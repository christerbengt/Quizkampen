package Quizgame.server_labs;

import Quizgame.game_classes.Player;
import Quizgame.server.database.QuestionDatabase;

public class ServerProtocol {
    private final QuestionDatabase questionDatabase = new QuestionDatabase("files/");
    final protected int gameStart = 0;
    final protected int requestTopics = 1;
    final protected int responseTopics = 2;
    final protected int responseQuestion1 = 3;
    final protected int responseQuestion2 = 4;
    Player player1;
    Player player2;

    protected int state = gameStart;

    public ServerProtocol() {

    }

    public String output(String clientInput){
        if (player1 != null && player2 != null){
            player1 = new Player(clientInput, gameStart);
            return questionDatabase.getQuestionCategories();
        } else if (player1 != null){
            player2 = new Player(clientInput, gameStart);
        }
        if (state == gameStart){
            state = requestTopics;
            return "Welcome to Quizgame!";
        } else if (state == requestTopics){

            return questionDatabase.getQuestionCategories();
        }
        return "Invalid input";
    }
}
