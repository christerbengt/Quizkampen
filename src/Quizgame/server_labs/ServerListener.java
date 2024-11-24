package Quizgame.server_labs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerListener {

    public ServerListener() {
        try (ServerSocket serverSocket = new ServerSocket(5001)) {

            while (true) {
                SSPlayer p1 = new SSPlayer(serverSocket.accept(), "p1");
                SSPlayer p2 = new SSPlayer(serverSocket.accept(), "p2");
                BServer bServer = new BServer(p1, p2, 2);
                bServer.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        ServerListener listener = new ServerListener();
    }
}
