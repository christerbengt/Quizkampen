package Quizgame.server_labs;

import Quizgame.server.database.QuestionDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BServer {
    ServerProtocol serverProtocol = new ServerProtocol();
    public BServer(){
        try (ServerSocket serverSocket = new ServerSocket(5001);
             Socket socket = serverSocket.accept();
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
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
