package Quizgame.server.database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDatabase {
    private final List<Question> questions = new ArrayList<>();

    public QuestionDatabase(String path) {
        initDataBase(path);
    }


    ////////////////////////////////////////////////////////
    //
    //  Input: path to working directory.
    //
    ////////////////////////////////////////////////////////
    private void initDataBase(String path) {
        File[] files = new File(path).listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                populateDatabase(path + file.getName());
            }
        }
    }


    ////////////////////////////////////////////////////////
    //
    //  Adds data to database based on file path.
    //  Input: String path to file.
    //
    ////////////////////////////////////////////////////////
    public void populateDatabase(String path) {
        File f = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] i = line.split(",");
                questions.add(new Question(i[0], i[1], i[2], i[3], i[4], i[5], i[6]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //////////////////////////////////////////////////////////////////////////
    //
    //  Input: String category
    //  Returns: a List of Questions based on category.
    //
    //////////////////////////////////////////////////////////////////////////
    public List<Question> getQuestionsByCategory(String category) {
        return questions.stream()
                .filter(e -> e.getCategory().contains(category))
                .map(e -> new Question(e.getQuestion(), e.getCategory(), e.getAnswerOption1(), e.getAnswerOption2(), e.getAnswerOption3(), e.getAnswerOption4(), e.getCorrectAnswer()))
                .collect(Collectors.toList());
    }

    ////////////////////////////////////////////////////////
    //
    //  Input: (String) User answer and correct answer.
    //  Return: boolean
    //
    ////////////////////////////////////////////////////////
    public boolean isQuestionCorrect(String answer, String correctAnswer) {
        return answer.equalsIgnoreCase(correctAnswer);
    }
}
