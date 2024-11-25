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

    public String getTopics(){
        return questionDatabase.getQuestionCategories();
    }

    public String getQuestion(String topic){
        return questionDatabase.getQuestionsByCategory(topic).getFirst().getQuestion();
    }

    public String output(String clientInput){
        return "xoxo";

    }
}
