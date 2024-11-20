package Quizgame.GUI;

import javax.swing.*;
import java.awt.*;


public class ScoreboardPanel extends JPanel {
    private JLabel player1Name;
    private JLabel player2Name;
    private JLabel player1Score;
    private JLabel player2Score;
    private JLabel player1Total;
    private JLabel player2Total;

    public ScoreboardPanel(String player1Name, String player2Name, int[] player1Scores, int[] player2Scores) {
        setLayout(new BorderLayout());

        JPanel leftPanel = createScorePanel(player1Name, player1Scores);
        JPanel rightPanel = createScorePanel(player2Name, player2Scores);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private static JPanel createScorePanel(String playerName, int[] scores) {
        JPanel panel = new JPanel();
        JPanel bagPanel = new JPanel();
        bagPanel.setLayout(new GridBagLayout());
        panel.setLayout(new GridLayout(4, 1)); // 2 category scores, 1 total score, 1 name
        bagPanel.add(panel);

        // Username Label
        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        panel.add(nameLabel);

        // Category Scores
        for (int score : scores) {
            JLabel scoreLabel = new JLabel("Category Score: " + score, SwingConstants.CENTER);
            panel.add(scoreLabel);
        }

        // Total Score
        int totalScore = calculateTotalScore(scores);
        JLabel totalScoreLabel = new JLabel("Total Score: " + totalScore, SwingConstants.CENTER);
        panel.add(totalScoreLabel);

        return bagPanel;
    }

    private static int calculateTotalScore(int[] scores) {
        int total = 0;
        for (int score : scores) {
            total += score;
        }
        return total;
    }
}