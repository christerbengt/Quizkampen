package Quizgame.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizCampenGUI extends JFrame implements ActionListener {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    ArrayList<JButton> answerButtons;
    ArrayList<JButton> categoryButtons;

    public QuizCampenGUI() {
        setTitle("Quiz Campen");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create four distinct panels
        // Like constructors, we need to create a method to read, categorys, questions and answers from files
        // These can then be used to create the panels insted of the strings
        JPanel panel1 = createWelcomePanel();
        JPanel panel2 = createCategoryPanel("category 1", "category 2", "category 3");
        JPanel panel3 = createQuestionPanel("Question 1: What is 2 + 2?", "Answer 1", "Answer 2", "Answer 3", "Answer 4");
        JPanel panel4 = createScorePanel();

        // Add the panels to the main panel
        mainPanel.add(panel1, "Welcome Panel");
        mainPanel.add(panel2, "Category");
        mainPanel.add(panel3, "Question");
        mainPanel.add(panel4, "Score");

        add(mainPanel);

        setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel mainPanel = new JPanel();
        JPanel centerPanel1 = new JPanel();
        JPanel centerPanel2 = new JPanel();
        JPanel startPanel = new JPanel();
        JPanel titelPanel = new JPanel();

        centerPanel2.setLayout(new GridBagLayout());
        centerPanel2.add(titelPanel);
        titelPanel.setLayout(new FlowLayout(100, 100, 100));
        JLabel questionLabel = new JLabel("Welcome to the Quiz!", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titelPanel.add(questionLabel);
        centerPanel2.add(titelPanel);
        mainPanel.add(centerPanel2, BorderLayout.NORTH);

        JButton startButton = new JButton("START");
        startPanel.setLayout(new FlowLayout(100, 100, 100));
        startPanel.add(startButton);
        startButton.addActionListener(new NextPanelAction());
        centerPanel1.setLayout(new GridBagLayout());
        centerPanel1.add(startPanel);
        mainPanel.add(centerPanel1, BorderLayout.CENTER);

        return mainPanel;
    }

/*
Discalaimer,
The code for the category panel and the question panel is almost the same, so any all coments will stay
in the category method for simplicity.

It should be doable to do a singel method for both, but I haven't figured out how to do it yet.

it became a bit convoluded, beacuse I had to add a sub panel into each subpanel. Think about this
like russian dolls. Could not figure out a way around this.

 */

    private JPanel createCategoryPanel(String category1, String category2, String category3) {
        //create buttons
        JButton categoryButton1 = new JButton(category1);
        JButton categoryButton2 = new JButton(category2);
        JButton categoryButton3 = new JButton(category3);

        //create panels
        JPanel mainPanel = new JPanel();
        JPanel centerPanel1 = new JPanel();
        JPanel centerPanel2 = new JPanel();
        JPanel answerPanel = new JPanel();
        JPanel questionPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());

        answerButtons = new ArrayList<>();
        answerButtons.add(categoryButton1);
        answerButtons.add(categoryButton2);
        answerButtons.add(categoryButton3);

        int sizeOfCategoryButtons = answerButtons.size();

        //create answer panel and add buttons
        answerPanel.setLayout(new GridLayout(sizeOfCategoryButtons,1,20,20));
        centerPanel1.setLayout(new GridBagLayout());
        centerPanel1.add(answerPanel);
        mainPanel.add(centerPanel1, BorderLayout.CENTER);

        for (JButton button : answerButtons) {
            button.addActionListener(new NextPanelAction());//dessa måste göra något, just nu går dom bara vidare
            answerPanel.add(button);
        }

        centerPanel2.setLayout(new GridBagLayout());
        centerPanel2.add(questionPanel);
        questionPanel.setLayout(new FlowLayout(100, 100, 100));
        JLabel questionLabel = new JLabel("Pick a category:", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        questionPanel.add(questionLabel);
        centerPanel2.add(questionPanel);
        mainPanel.add(centerPanel2, BorderLayout.NORTH);

        return mainPanel;
    }




    private JPanel createQuestionPanel(String questionText, String answerText1, String answerText2, String answerText3, String answerText4) {
        JButton AnswerButton1 = new JButton(answerText1);
        JButton AnswerButton2 = new JButton(answerText2);
        JButton AnswerButton3 = new JButton(answerText3);
        JButton AnswerButton4 = new JButton(answerText4);

        JPanel mainPanel = new JPanel();
        JPanel centerPanel1 = new JPanel();
        JPanel centerPanel2 = new JPanel();
        JPanel answerPanel = new JPanel();
        JPanel questionPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());

        answerButtons = new ArrayList<>();
        answerButtons.add(AnswerButton1);
        answerButtons.add(AnswerButton2);
        answerButtons.add(AnswerButton3);
        answerButtons.add(AnswerButton4);

        answerPanel.setLayout(new GridLayout(2,2,20,20));
        centerPanel1.setLayout(new GridBagLayout());
        centerPanel1.add(answerPanel);
        mainPanel.add(centerPanel1, BorderLayout.CENTER);

        for (JButton button : answerButtons) {
            button.addActionListener(new NextPanelAction()); //dessa måste göra något, just nu går dom bara vidare
            answerPanel.add(button);
        }

        centerPanel2.setLayout(new GridBagLayout());
        centerPanel2.add(questionPanel);
        questionPanel.setLayout(new FlowLayout(100, 100, 100));
        JLabel questionLabel = new JLabel(questionText, SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        questionPanel.add(questionLabel);
        centerPanel2.add(questionPanel);
        mainPanel.add(centerPanel2, BorderLayout.NORTH);

        return mainPanel;
    }



    private JPanel createScorePanel() {
        JPanel mainPanel = new JPanel();
        JLabel label = new JLabel("Score: 0", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(label);
        return mainPanel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class NextPanelAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.next(mainPanel); // Show the next panel
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizCampenGUI::new);
    }
}