package ru.otus.agaryov.dz3.results;


import java.util.Map;

public interface ResultChecker {
    // Check an answer to a question
    void checkAnswer(String question, String answer);
    // All questions in right order
    Object[] getQuestions();
    // get right answers counter
    Integer getResult();
    // reload map if we need to change config file with questions at runtime
    void setMap(Map<String, String> aMap);
}
