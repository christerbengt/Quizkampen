package Quizgame.server.database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDatabase {
    private final List<Question> questions;

    public QuestionDatabase(String path) {
        questions = initDataBase(path);
    }

    private List<Question> initDataBase(String path) {
        // todo: SKapa textDoc och skapa questions utifrån det!
        List<Question> q = new ArrayList<>();
        File f = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] i = line.split(",");
                q.add(new Question(i[0], i[6], i[3], i[4], i[5], i[6], i[2]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return q;
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questions.stream().
                filter(e -> e.getCategory().contains(category))
                .map(e -> new Question(e.getQuestion(), e.getCategory(), e.getAnswerOption1(), e.getAnswerOption2(), e.getAnswerOption3(), e.getAnswerOption4(), e.getCorrectAnswer()))
                .collect(Collectors.toList());
    }
}
