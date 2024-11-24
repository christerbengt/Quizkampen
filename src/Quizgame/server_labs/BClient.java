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
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ){
            String fromUser = "";
            String fromServer = "";
            String name = "Bosse";

            fromServer = in.readLine();
            System.out.println(fromServer);

            while (true){
                fromServer = in.readLine();
                if(fromServer.startsWith("Topic")){
                    System.out.println("Choosing a topic " + fromServer);
                    String topic = userInput.readLine();
                    out.println("T " + topic); // Sends request for topic
                }

                else if(fromServer.startsWith("StartP2")){ // Start point for player 2
                    String[] a = fromServer.split(" ");
                    System.out.println(stringRemake(a));
                    String answer = userInput.readLine();
                    out.println("A1 " + answer); // Sends request for answer 1
                }

                else if(fromServer.startsWith("Q1")){
                    String[] a = fromServer.split(" ");
                    System.out.println(stringRemake(a));
                    String answer = userInput.readLine();
                    out.println("A1 " + answer); // Sends request for answer 1
                }

                else if(fromServer.startsWith("Q2")){
                    String[] a = fromServer.split(" ");
                    System.out.println(stringRemake(a));
                    String answer = userInput.readLine();
                    out.println("A2 " + answer); // Sends request for answer 1
                }

                else if (fromServer.startsWith("A1")){
                    System.out.println(stringRemake(fromServer.split(" ")));
                    out.println("Q2"); // Sends request for question 2
                }

                else if(fromServer.startsWith("A2")){
                    System.out.println(fromServer);
                    out.println("N");
                }

                else if (fromServer.startsWith("S")){
                    System.out.println(fromServer);
                    out.println("GameOver");
                }

                else if (fromServer.startsWith("E")){
                    out.println(fromServer);


                }

                else if (fromServer.startsWith("Waiting")){
                    System.out.println(fromServer);
                }
            }
        }
    }

    public String stringRemake(String[] arry){
        StringBuilder retVal = new StringBuilder();
        for (int i = 1; i < arry.length; i++) {
            retVal.append(arry[i]).append(" ");
        }
        return retVal.toString();
    }
}
