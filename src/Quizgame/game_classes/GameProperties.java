package Quizgame.game_classes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameProperties {
    private static Properties props = new Properties();

    public static void loadProperties(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
        }
    }

    public static int getQuestionsPerRound() {
        return Integer.parseInt(props.getProperty("questions.per.round", "3"));
    }

    public static int getTotalRounds() {
        return Integer.parseInt(props.getProperty("total.rounds", "6"));
    }
}
