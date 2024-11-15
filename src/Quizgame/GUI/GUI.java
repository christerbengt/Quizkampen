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
            p2.add(button);
        }
        for (JButton button : categoryButtons) {
            button.addActionListener(this);
            p3.add(button);
        }

        this.add(p1);
        p1.setLayout(new BorderLayout());
        p1.add(p2, BorderLayout.CENTER);
        p1.add(p3, BorderLayout.NORTH);

        p2.setLayout(new GridLayout(2, 2));
        p3.setLayout(new GridLayout(3,1));

        startGameButton = new JButton("Start Game");

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
