package Quizgame.server_labs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class BClient {
    public BClient() throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 5001);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        ){
            String fromUser = "";
            String fromServer = "";

            fromServer = in.readLine();
            System.out.println(fromServer);

            String input = "";
            String servierInput = "";
            while (true){
                input = userInput.readLine();
                out.println(input);
                servierInput = in.readLine();
                System.out.println(servierInput);
            }
/*            while ((fromUser = userInput.readLine()) != null) {
                out.println(fromUser);
                System.out.println("From client: " + fromUser);

                // reading from server
                fromServer = in.readLine();
                System.out.println(fromServer);
            }*/
        }
    }
}
