package Quizgame.GUI;

import javax.swing.*;
import java.awt.*;

public class WelcomePanel {
    static JPanel createWelcomePanel() {
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
        startButton.addActionListener(new QuizCampenGUI.NextPanelAction());
        centerPanel1.setLayout(new GridBagLayout());
        centerPanel1.add(startPanel);
        mainPanel.add(centerPanel1, BorderLayout.CENTER);

        return mainPanel;
    }
}
