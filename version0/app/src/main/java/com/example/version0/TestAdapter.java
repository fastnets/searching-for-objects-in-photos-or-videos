package com.example.version0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class TestAdapter extends ArrayAdapter<Map.Entry<String, List<QuestionData>>> {
    public TestAdapter(@NonNull Context context, List<Map.Entry<String, List<QuestionData>>> testSets) {
        super(context, R.layout.tests_item, testSets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Map.Entry<String, List<QuestionData>> testSet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tests_item, parent, false);
        }

        TextView testName = convertView.findViewById(R.id.testName);
        TextView numberOfQuestions = convertView.findViewById(R.id.numberQuestions);

        testName.setText(testSet.getKey());
        numberOfQuestions.setText(String.valueOf(testSet.getValue().size()));

        return convertView;
    }
}
