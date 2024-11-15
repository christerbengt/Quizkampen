package Quizgame.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {

    Panel p1 = new Panel();
    Panel p2 = new Panel();
    Panel p3 = new Panel();
    Panel p4 = new Panel();

    JButton startGameButton;

    JButton colorChooserButton;

    JButton CategoryButton1;
    JButton CategoryButton2;
    JButton CategoryButton3;

    JButton AnswerButton1;
    JButton AnswerButton2;
    JButton AnswerButton3;
    JButton AnswerButton4;
    ArrayList<JButton> answerButtons;
    ArrayList<JButton> categoryButtons;

    public GUI() {
        answerButtons = new ArrayList<>(AnswerButton1, AnswerButton2, AnswerButton3, AnswerButton4);
        categoryButtons = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            JButton button = new JButton(String.valueOf(i));
            categoryButtons.add(button);
            p3.add(button);
        }

        for (int i = 1; i <= 4; i++) {
            JButton button = new JButton(String.valueOf(i));
            answerButtons.add(button);
            p2.add(button);
        }

        this.add(p1);
        p1.add(p2, BorderLayout.CENTER);
        p1.add(p3, BorderLayout.NORTH);
        p1.setLayout(new BorderLayout());
        p2.setLayout(new GridLayout(2, 2));
        p3.setLayout(new GridLayout(1,3));

        startGameButton = new JButton("Start Game");

        AnswerButton1 = new JButton("Answer 1");
        AnswerButton2 = new JButton("Answer 2");
        AnswerButton3 = new JButton("Answer 3");
        AnswerButton4 = new JButton("Answer 4");

        JColorChooser colorChooser = new JColorChooser();

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
