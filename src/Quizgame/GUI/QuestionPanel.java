package Quizgame.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuestionPanel extends JFrame implements ActionListener {

    static JPanel createQuestionPanel(String questionText, String answerText1, String answerText2, String answerText3, String answerText4) {
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

        ArrayList<JButton> answerButtons = new ArrayList<>();
        answerButtons.add(AnswerButton1);
        answerButtons.add(AnswerButton2);
        answerButtons.add(AnswerButton3);
        answerButtons.add(AnswerButton4);

        answerPanel.setLayout(new GridLayout(2,2,20,20));
        centerPanel1.setLayout(new GridBagLayout());
        centerPanel1.add(answerPanel);
        mainPanel.add(centerPanel1, BorderLayout.CENTER);

        for (JButton button : answerButtons) {
            button.addActionListener(new QuizCampenGUI.NextPanelAction()); //dessa måste göra något, just nu går dom bara vidare
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

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
