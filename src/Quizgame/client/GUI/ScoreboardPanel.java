package Quizgame.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreboardPanel extends JFrame implements ActionListener {

    static JPanel createScorePanel() {
        JPanel mainPanel = new JPanel();
        JLabel label = new JLabel("Score: 0", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(label);
        return mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
