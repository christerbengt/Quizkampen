package Quizgame.client.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        panel.setLayout(new GridLayout(4, 1)); // 2 category scores, 1 total score, 1 name

        // Username Label
        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        nameLabel.setBorder(new EmptyBorder(10, 20, 10, 20));  // Add some padding
        panel.add(nameLabel);

        // Category Scores
        for (int score : scores) {
            JLabel scoreLabel = new JLabel("Category Score: " + score, SwingConstants.CENTER);
            scoreLabel.setBorder(new EmptyBorder(10, 20, 10, 20));  // Add some padding
            panel.add(scoreLabel);
        }

        // Total Score
        int totalScore = calculateTotalScore(scores);
        JLabel totalScoreLabel = new JLabel("Total Score: " + totalScore, SwingConstants.CENTER);
        totalScoreLabel.setBorder(new EmptyBorder(10, 20, 10, 20));  // Add some padding
        panel.add(totalScoreLabel);

        return panel;
    }

    private static int calculateTotalScore(int[] scores) {
        int total = 0;
        for (int score : scores) {
            total += score;
        }
        return total;
    }
}