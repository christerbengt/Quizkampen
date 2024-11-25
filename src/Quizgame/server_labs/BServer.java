package Quizgame.server_labs;

import Quizgame.server.database.QuestionDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BServer extends Thread {
    ServerProtocol serverProtocol = new ServerProtocol();
    SSPlayer p1;
    SSPlayer p2;
    SSPlayer currentPlayer;

    public BServer(SSPlayer p1, SSPlayer p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.currentPlayer = p1;
        p1.setOpponent(p2);
        p2.setOpponent(p1);
    }



    public void run (){

        p1.send("Player One make your move Bitch!");
        p2.send("Player Two..... waiting for the slow fucker player One");
        String command = "";
        currentPlayer = p1;
        while (true){
            command = currentPlayer.receive().trim();
            if (command.equals("topic")){
                currentPlayer.send(serverProtocol.getTopics());
                currentPlayer = p2;
            } else if (command.equals("Music")){
                currentPlayer.send(serverProtocol.getQuestion(command));
                currentPlayer = p1;
            } else {
                currentPlayer.send("Invalid command");
            }
        }

/*        try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ){
            out.println("Welcome to the server!");
            String fromClient = "";

            while((fromClient = in.readLine()) != null){
                System.out.println(fromClient);
                String protocoll = serverProtocol.output(fromClient);
                System.out.println(protocoll);
                out.println(serverProtocol.output("Hejsan"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}
