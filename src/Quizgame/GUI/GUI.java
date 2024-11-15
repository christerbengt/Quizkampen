package Quizgame.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {

    Panel mainPanel = new Panel();
    Panel answerPanel = new Panel();
    Panel categoryPanel = new Panel();
    Panel p4 = new Panel();

    JButton startGameButton;

    JButton CategoryButton1 = new JButton("Answer 1");;
    JButton CategoryButton2 = new JButton("Answer 1");
    JButton CategoryButton3 = new JButton("Answer 1");

    JButton AnswerButton1 = new JButton("Answer 1");
    JButton AnswerButton2 = new JButton("Answer 1");
    JButton AnswerButton3 = new JButton("Answer 1");
    JButton AnswerButton4 = new JButton("Answer 1");

    ArrayList<JButton> answerButtons;
    ArrayList<JButton> categoryButtons;

    public GUI() {
        answerButtons = new ArrayList<>();
        answerButtons.add(AnswerButton1);
        answerButtons.add(AnswerButton2);
        answerButtons.add(AnswerButton3);
        answerButtons.add(AnswerButton4);

        categoryButtons = new ArrayList<>();
        categoryButtons.add(CategoryButton1);
        categoryButtons.add(CategoryButton2);
        categoryButtons.add(CategoryButton3);

        for (JButton button : answerButtons) {
            button.addActionListener(this);
            answerPanel.add(button);
        }
        for (JButton button : categoryButtons) {
            button.addActionListener(this);
            categoryPanel.add(button);
        }

        this.add(mainPanel);
        p4.setLayout(new GridBagLayout());
        p4.add(answerPanel);
        mainPanel.setLayout(new BorderLayout(20,20));
        mainPanel.add(p4, BorderLayout.CENTER);
        mainPanel.add(categoryPanel, BorderLayout.NORTH);

        answerPanel.setLayout(new GridLayout(2,2,20,20));
        categoryPanel.setLayout(new GridLayout(3,1,0,20));

        startGameButton = new JButton("Start Game");

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public static void main(String[] args) {
        GUI gui = new GUI();
    }

}
