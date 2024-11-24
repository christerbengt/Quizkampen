package Quizgame.client.GUI;

import Quizgame.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class QuizCampenGUI extends JFrame implements ActionListener {
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private GameClient client;

    public QuizCampenGUI(GameClient client) {
        this.client = client;
        setTitle("Quiz Campen");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        int[] player1Scores = {10, 15}; // Example scores for player 1
        int[] player2Scores = {12, 14}; // Example scores for player 2

        WelcomePanel welcomePanel = new WelcomePanel(this);
        CategoryPanel categoryPanel = new CategoryPanel(this,"category 1", "category 2", "category 3");
        QuestionPanel questionPanel = new QuestionPanel(this,"Question 1: What is 2 + 2?", "5", "3", "2", "4", "4");
        ScoreboardPanel scoreboardPanel = new ScoreboardPanel("Charlie", "Christer", player1Scores, player2Scores);

        // Create four distinct panels
        // Like constructors, we need to create a method to read, categorys, questions and answers from files
        // These can then be used to create the panels insted of the strings

      // Add the panels to the main panel
        mainPanel.add(welcomePanel, "Welcome Panel");
        mainPanel.add(categoryPanel, "Category");
        mainPanel.add(questionPanel, "Question");
        mainPanel.add(scoreboardPanel, "Score");

        add(mainPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void showNextPanel(String panelName) {
        cardLayout.show(mainPanel, panelName); // Show the next panel
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    public GameClient getClient() {
        return client;
    }


    public static void main(String[] args) {
        GameClient client = new GameClient("localhost", 5000, null);
        QuizCampenGUI gui = new QuizCampenGUI(client);
        client.setGui(gui);
        if (client.connect()) {
            SwingUtilities.invokeLater(() -> gui.setVisible(true));
        } else {
            System.err.println("Could not connect to server");
            System.exit(1);
        }
    }
}
