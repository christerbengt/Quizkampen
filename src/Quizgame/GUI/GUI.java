package Quizgame.GUI;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    Panel p1 = new Panel();
    Panel p2 = new Panel();
    Panel p3 = new Panel();

    JButton startGameButton;

    JButton colorChooserButton;

    JButton CategoryButton1;
    JButton CategoryButton2;
    JButton CategoryButton3;

    JButton AnswerButton1;
    JButton AnswerButton2;
    JButton AnswerButton3;
    JButton AnswerButton4;

    public GUI() {
        this.add(p1);
        p1.add(p2, BorderLayout.CENTER);
        p1.add(p3, BorderLayout.NORTH);
        p1.setLayout(new BorderLayout());
        p2.setLayout(new GridLayout(4, 4));
        p3.setLayout(new FlowLayout());

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

}
