package server_database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDatabase {
    private List<Question> questions;
    public QuestionDatabase(String path) {
        questions = initDataBase(path);
    }

    private List<Question> initDataBase(String path) {
        // todo: SKapa textDoc och skapa questions utifrån det!
        return new ArrayList<>();
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questions.stream().
                filter(e -> e.getCategory().contains(category))
                .map(e -> new Question(e.getQuestion(), e.getCategory(), e.getAnswerOption1(), e.getAnswerOption2(), e.getAnswerOption3(), e.getAnswerOption4(), e.getCorrectAnswer()))
                .collect(Collectors.toList());
    }
}
