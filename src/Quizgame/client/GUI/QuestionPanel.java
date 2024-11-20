package Quizgame.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionPanel extends JPanel implements ActionListener {

    private String correctAnswer;
    private QuizCampenGUI parent;

    public QuestionPanel(QuizCampenGUI parent, String questionText, String answerText1, String answerText2, String answerText3, String answerText4, String correctAnswer) {
        this.correctAnswer = correctAnswer;
        this.parent = parent;
        setLayout(new BorderLayout());

        JPanel mainPanel = createQuestionPanel(questionText, answerText1, answerText2, answerText3, answerText4);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createQuestionPanel(String questionText, String answerText1, String answerText2, String answerText3, String answerText4) {
        JButton AnswerButton1 = new JButton(answerText1);
        JButton AnswerButton2 = new JButton(answerText2);
        JButton AnswerButton3 = new JButton(answerText3);
        JButton AnswerButton4 = new JButton(answerText4);

        AnswerButton1.setActionCommand(answerText1);
        AnswerButton2.setActionCommand(answerText2);
        AnswerButton3.setActionCommand(answerText3);
        AnswerButton4.setActionCommand(answerText4);

        JPanel mainPanel = new JPanel();
        JPanel centerPanel1 = new JPanel();
        JPanel centerPanel2 = new JPanel();
        JPanel answerPanel = new JPanel();
        JPanel questionPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());

        ArrayList<JButton> answerButtons = new ArrayList<>();
        answerButtons.add(AnswerButton1);
        answerButtons.add(AnswerButton2);
        answerButtons.add(AnswerButton3);
        answerButtons.add(AnswerButton4);
        Collections.shuffle(answerButtons);

        answerPanel.setLayout(new GridLayout(2,2,20,20));
        centerPanel1.setLayout(new GridBagLayout());
        centerPanel1.add(answerPanel);
        mainPanel.add(centerPanel1, BorderLayout.CENTER);

        for (JButton button : answerButtons) {
            button.addActionListener(this); //dessa måste göra något, just nu går dom bara vidare
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

    private boolean checkAnswer(String answer) {
        return answer.equals(correctAnswer);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String answer = button.getActionCommand();

        // Check the answer and set the button color
        if (checkAnswer(answer)) {
            button.setBackground(Color.GREEN);
        } else {
            button.setBackground(Color.RED);
        }

        // Set a timer to wait before proceeding to the next panel
        Timer timer = new Timer(1000, evt -> {
            // After a delay, transition to the next panel
            parent.showNextPanel();
            // Reset the background to default (for other uses)
            button.setBackground(null);
        });
        timer.setRepeats(false); // Make sure the timer only runs once
        timer.start();
    }
}
