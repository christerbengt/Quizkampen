package Quizgame.server_labs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BServer {
    public BServer(){
        try (ServerSocket ss = new ServerSocket(5001);
             Socket s = ss.accept();
             PrintWriter out = new PrintWriter(s.getOutputStream(), true);
             BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
             ){

            out.println("Welcome to the server!");
            String clientRequest = "";
            while ((clientRequest = br.readLine()) != null) {
                System.out.println("stuff");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
