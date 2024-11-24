package Quizgame.server_labs;

import Quizgame.server.database.QuestionDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BServer extends Thread {

    Socket socket;
    public BServer(Socket socket) {
        this.socket = socket;
    }
    ServerProtocol serverProtocol = new ServerProtocol();
    public void run (){
        try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
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
        }
    }
}
