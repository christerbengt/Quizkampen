package Quizgame.client.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePanel extends JPanel implements ActionListener {
    private QuizCampenGUI parent;

    public WelcomePanel(QuizCampenGUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        JPanel mainPanel = createWelcomePanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    public JTextField createTextField() {
        JTextField textField = new JTextField(10);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setToolTipText("Enter you name here");
        textField.setMargin(new Insets(5, 10, 5, 10));
        textField.setMaximumSize(new Dimension(180, 30));
        return textField;
    }


    private JPanel createWelcomePanel() {
        JPanel mainPanel = new JPanel();
        JPanel centerPanel1 = new JPanel();
        JPanel inputPanel = new JPanel();
        JPanel centerPanel2 = new JPanel();
        JPanel startPanel = new JPanel();
        JPanel titlePanel = new JPanel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        centerPanel2.setLayout(new GridBagLayout());
        centerPanel2.add(titlePanel);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 0));
        JLabel questionLabel = new JLabel("Welcome to the Quiz!");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(questionLabel);
        centerPanel2.add(titlePanel);
        mainPanel.add(centerPanel2, BorderLayout.NORTH);


        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
        JLabel nameLabel = new JLabel("Please enter your name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(nameLabel);
        inputPanel.add(createTextField());
        mainPanel.add(inputPanel, BorderLayout.CENTER);


        JButton startButton = new JButton("START");
        startPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        startPanel.add(startButton);
        startButton.addActionListener(this);
        centerPanel1.setLayout(new GridBagLayout());
        centerPanel1.add(startPanel);
        mainPanel.add(centerPanel1, BorderLayout.SOUTH);


        return mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.showNextPanel();

    }
}
