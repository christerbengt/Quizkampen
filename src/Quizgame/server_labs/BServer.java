package Quizgame.server_labs;

import Quizgame.server.database.Question;

public class BServer extends Thread {
    ServerProtocol serverProtocol;
    SSPlayer p1;
    SSPlayer p2;
    SSPlayer currentPlayer;

    public BServer(SSPlayer p1, SSPlayer p2, int rounds) {
        serverProtocol = new ServerProtocol(rounds);
        this.p1 = p1;
        this.p2 = p2;
        this.currentPlayer = p1;
        p1.setOpponent(p2);
        p2.setOpponent(p1);
    }



    public void run (){
        p1.send("Welcome to Quizgame");
        p2.send("Welcome to Quizgame");
        String command;
        currentPlayer = p1;
        currentPlayer.send("Topic " + serverProtocol.getQuestionDatabase().getQuestionCategories());
        while (true){
            command = currentPlayer.receive();

            if (command.startsWith("T")){
                setupQuestionsByTopic(stringRemaker(command.split(" ")));
            }

            else if (command.startsWith("Q1")){
                pushQuestion1(command.split(" "));
            }

            else if (command.startsWith("Q2")){
                pushQuestionTwo(command.split(" "));
            }

            else if (command.startsWith("A1")){
                correctFirstQuestion(command.split(" "));
            }

            else if (command.startsWith("A2")){
                correctSecondQuestion(command.split(" "));
            }

            else if (command.startsWith("N")){
                isRoundDone();
             }

            else if (command.startsWith("S")){
                pushSummarize();
            }

            else if (command.startsWith("GameOver")){
                if (serverProtocol.isGameOver()){
                    if (currentPlayer.score > currentPlayer.getOpponent().score){
                        currentPlayer.send("You are a Winner");
                        currentPlayer.getOpponent().send("You are a looser");
                    } else if (currentPlayer.score < currentPlayer.getOpponent().score){
                        currentPlayer.getOpponent().send("You are a Winner");
                        currentPlayer.send("You are a looser");
                    } else {
                        currentPlayer.send("You are a tie");
                        currentPlayer.getOpponent().send("You are a tie");
                    }
                    break;
                } else {
                    serverProtocol.round++;
                    currentPlayer.send("Topic " + serverProtocol.getQuestionDatabase().getQuestionCategories());
                }
            }
        }
    }

    private void pushSummarize() {
        currentPlayer.send("E " + currentPlayer.name + ": " + currentPlayer.score + " " + currentPlayer.getOpponent().name + ": " + currentPlayer.score);
        currentPlayer.opponent.send("E " + currentPlayer.name + ": " + currentPlayer.score + " " + currentPlayer.getOpponent().name + ": " + currentPlayer.score);
    }

    private void isRoundDone() {
        currentPlayer.isQuestionAnswered = true;
        if(currentPlayer.getOpponent().isQuestionAnswered){
            currentPlayer = currentPlayer.getOpponent();
            currentPlayer.send("S");
        } else {
            currentPlayer = currentPlayer.getOpponent();
            currentPlayer.getOpponent().send("Waiting for opponent");
            currentPlayer.send("StartP2 " + serverProtocol.question1().getQuestion());
        }
    }

    private void setupQuestionsByTopic(String s) {
        serverProtocol.setRoundQuestions(s);
        currentPlayer.send("Q1 " + serverProtocol.question1().getQuestion() + " " + serverProtocol.question1().getCorrectAnswer());
    }

    private void pushQuestion1(String[] s) {
        Question q1 = serverProtocol.question1();
        currentPlayer.send("Q1 " + q1.getQuestion() + " " + q1.getCorrectAnswer());
    }

    private void pushQuestionTwo(String[] s) {
        Question q2 = serverProtocol.question2();
        currentPlayer.send("Q2 " + q2.getQuestion() + " " + q2.getCorrectAnswer());
    }

    private void correctFirstQuestion(String[] s) {
        String answer = stringRemaker(s);
        if (answer.equalsIgnoreCase(serverProtocol.question1().getCorrectAnswer())){
            currentPlayer.send("A1 Correct Answer");
            currentPlayer.score = currentPlayer.score + 1;
        } else {
            currentPlayer.send("A1 Wrong Answer");
        }
    }

    private void correctSecondQuestion(String[] s) {
        String answer = stringRemaker(s);
        if (answer.equalsIgnoreCase(serverProtocol.question2().getCorrectAnswer())){
            currentPlayer.send("A2 Correct Answer");
            currentPlayer.score = currentPlayer.score + 1;
        } else {
            currentPlayer.send("A2 Wrong Answer");
        }
    }

    public String stringRemaker(String[] arry){
        StringBuilder retVal = new StringBuilder();
        for (int i = 1; i < arry.length; i++) {
            retVal.append(arry[i]).append(" ");
        }
        return retVal.toString().trim();
    }

}
