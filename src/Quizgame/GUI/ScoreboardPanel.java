package Quizgame.GUI;

import javax.swing.*;
import java.awt.*;

public class ScoreboardPanel {


    static JPanel createScorePanel() {
        JPanel mainPanel = new JPanel();
        JLabel label = new JLabel("Score: 0", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(label);
        return mainPanel;
    }
}
