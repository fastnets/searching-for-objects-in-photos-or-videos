package com.example.version0;

public class OptionData {
    private String option;
    private boolean isCorrect;

    public OptionData(String option, boolean isCorrect) {
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public String getOption() {
        return option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
