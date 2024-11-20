package Quizgame.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizCampenGUI extends JFrame implements ActionListener {
    private static CardLayout cardLayout;
    private static JPanel mainPanel;

    public QuizCampenGUI() {
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
        JPanel panel1 = welcomePanel;
        JPanel panel2 = categoryPanel;
        JPanel panel3 = questionPanel;
        JPanel panel4 = scoreboardPanel;

        // Add the panels to the main panel
        mainPanel.add(panel1, "Welcome Panel");
        mainPanel.add(panel2, "Category");
        mainPanel.add(panel3, "Question");
        mainPanel.add(panel4, "Score");

        add(mainPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void showNextPanel() {
        cardLayout.next(mainPanel); // Show the next panel
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizCampenGUI::new);
    }
}