package Quizgame.server_database;

public class Question {
    private final String question;
    private final String category;
    private final String answerOption1;
    private final String answerOption2;
    private final String answerOption3;
    private final String answerOption4;
    private final String correctAnswer;

    public Question(String question, String category, String answerOption1, String answerOption2, String answerOption3, String answerOption4, String correctAnswer) {
        this.question = question;
        this.category = category;
        this.answerOption1 = answerOption1;
        this.answerOption2 = answerOption2;
        this.answerOption3 = answerOption3;
        this.answerOption4 = answerOption4;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCategory() {
        return category;
    }

    public String getAnswerOption1() {
        return answerOption1;
    }

    public String getAnswerOption2() {
        return answerOption2;
    }

    public String getAnswerOption3() {
        return answerOption3;
    }

    public String getAnswerOption4() {
        return answerOption4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
