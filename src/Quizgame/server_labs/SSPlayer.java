package Quizgame.server_labs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SSPlayer {
    String name;
    SSPlayer opponent;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    int state = 0;


    public SSPlayer(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        try {
            output = new PrintWriter(this.socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String receive()  {
        try {
            return input.readLine();
        } catch (IOException e) {
            System.out.println("Player "+name+" could not receive data " + e);
            throw new RuntimeException(e);
        }
    }


    public void send(String mess){
        output.println(mess);
    }


    /**
     * Accepts notification of who the opponent is.
     */
    public void setOpponent(SSPlayer opponent) {
        this.opponent = opponent;
    }

    /**
     * Returns the opponent.
     */
    public SSPlayer getOpponent() {
        return opponent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
