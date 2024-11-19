package Quizgame.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizCampenGUI extends JFrame implements ActionListener {
    private static CardLayout cardLayout;
    private static JPanel mainPanel;

    ArrayList<JButton> answerButtons;

    public QuizCampenGUI() {
        setTitle("Quiz Campen");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create four distinct panels
        // Like constructors, we need to create a method to read, categorys, questions and answers from files
        // These can then be used to create the panels insted of the strings
        JPanel panel1 = WelcomePanel.createWelcomePanel();
        JPanel panel2 = CategoryPanel.createCategoryPanel("category 1", "category 2", "category 3");
        JPanel panel3 = QuestionPanel.createQuestionPanel("Question 1: What is 2 + 2?", "Answer 1", "Answer 2", "Answer 3", "Answer 4");
        JPanel panel4 = ScoreboardPanel.createScorePanel();

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

    protected static class NextPanelAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.next(mainPanel); // Show the next panel
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizCampenGUI::new);
    }
}