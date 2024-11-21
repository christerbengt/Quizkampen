package Quizgame.client.GUI;

import Quizgame.game_classes.Round;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizCampenGUI extends JFrame implements ActionListener {
    CardLayout cardLayout;
    JPanel mainPanel;

    public QuizCampenGUI(String category1, String category2, String category3,
                         String questionText, String answerText1, String answerText2, String answerText3, String answerText4, String correctAnswer,
                         String player1Name, String player2Name, int[] player1Scores, int[] player2Scores) {


        JPanel panel = createQuizCampenGUI(category1, category2, category3,
                    questionText, answerText1, answerText2, answerText3, answerText4, correctAnswer,
                    player1Name, player2Name, player1Scores, player2Scores);
        add(panel);
    }


    public JPanel createQuizCampenGUI(String category1, String category2, String category3,
                                      String questionText, String answerText1, String answerText2, String answerText3, String answerText4, String correctAnswer,
                                      String player1Name, String player2Name, int[] player1Scores, int[] player2Scores) {
        setTitle("Quiz Campen");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);


        WelcomePanel welcomePanel = new WelcomePanel(this);
        CategoryPanel categoryPanel = new CategoryPanel(this, category1, category2, category3);
        QuestionPanel questionPanel = new QuestionPanel(this,questionText, answerText1, answerText2, answerText3, answerText4, correctAnswer);
        ScoreboardPanel scoreboardPanel = new ScoreboardPanel(player1Name, player2Name, player1Scores, player2Scores);

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