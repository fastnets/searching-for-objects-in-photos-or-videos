package com.example.version0;

import java.util.List;

public class QuestionData {
    private int id;
    private String question;
    private List<OptionData> options;

    public QuestionData(int id, String question, List<OptionData> options) {
        this.id = id;
        this.question = question;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public List<OptionData> getOptions() {
        return options;
    }
}
