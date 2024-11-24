package Quizgame.server_labs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerListener {

    public ServerListener() {
        try (ServerSocket serverSocket = new ServerSocket(5001)) {

            while (true) {
                Socket socket = serverSocket.accept();
                BServer bServer = new BServer(socket);
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
