package com.example.version0;

import java.util.List;
import java.util.Map;

public class TestSet {
    private Map<String, List<QuestionData>> tests;

    public TestSet(Map<String, List<QuestionData>> tests) {
        this.tests = tests;
    }

    public Map<String, List<QuestionData>> getTests() {
        return tests;
    }
}
