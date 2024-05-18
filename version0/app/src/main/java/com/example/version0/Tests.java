package com.example.version0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.version0.databinding.FragmentTestsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tests extends Fragment {
    private FragmentTestsBinding binding;
    private TestAdapter testAdapter;
    private ArrayList<Map.Entry<String, List<QuestionData>>> testArrayList; // Change to use Map.Entry

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TestsFragment", "onCreateView()");
        binding = FragmentTestsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ListView listView = binding.listView;
        // Read tests from JSON
        ArrayList<TestSet> testSets = AssetManagerUtil.readTestsFromJSON(requireContext(), "db_tests.json");
        // Convert TestSet objects to Map.Entry objects
        testArrayList = new ArrayList<>();
        for (TestSet testSet : testSets) {
            for (Map.Entry<String, List<QuestionData>> entry : testSet.getTests().entrySet()) {
                testArrayList.add(entry);
            }
        }

        testAdapter = new TestAdapter(getActivity(), testArrayList);
        listView.setAdapter(testAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get selected test
                Map.Entry<String, List<QuestionData>> selectedTestEntry = testArrayList.get(i);
                String testName = selectedTestEntry.getKey();
                List<QuestionData> questions = selectedTestEntry.getValue();

                // Create intent to start DetailTest activity
                Intent intent = new Intent(getActivity(), DetailTest.class);
                intent.putExtra("testName", testName);
                intent.putExtra("numberQuestions", questions.size());
                // Pass question data to DetailTest activity
                ArrayList<String> questionStrings = new ArrayList<>();
                for (QuestionData question : questions) {
                    StringBuilder questionBuilder = new StringBuilder();
                    questionBuilder.append(question.getQuestion()).append("\n");
                    List<OptionData> options = question.getOptions();
                    for (OptionData option : options) {
                        questionBuilder.append(option.getOption());
                        if (option.isCorrect()) {
                            questionBuilder.append(" (+)");
                        }
                        questionBuilder.append("\n");
                    }
                    questionStrings.add(questionBuilder.toString());
                }
                intent.putStringArrayListExtra("questions", questionStrings);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
