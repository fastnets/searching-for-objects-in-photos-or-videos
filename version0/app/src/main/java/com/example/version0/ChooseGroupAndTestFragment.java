package com.example.version0;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseGroupAndTestFragment extends Fragment {

    private Spinner groupSpinner;
    private Spinner testSpinner;
    private Button startTestButton;
    private String selectedTestName;

    private GroupData selectedGroup;
    private TestSet selectedTestSet;

    private ArrayList<GroupData> groupDataList;
    private ArrayList<TestSet> testDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_group_and_test, container, false);

        groupSpinner = view.findViewById(R.id.spinnerGroup);
        testSpinner = view.findViewById(R.id.spinnerTest);
        startTestButton = view.findViewById(R.id.buttonConfirmSelection);

        // Загружаем данные из JSON файлов
        groupDataList = AssetManagerUtil.readGroupsFromJSON(requireContext(), "db_groups.json");
        ArrayList<String> groupNames = AssetManagerUtil.readGroupNamesFromJSON(requireContext(), "db_groups.json");
        ArrayList<String> testNames = AssetManagerUtil.readTestNamesFromJSON(requireContext(), "db_tests.json");
        testDataList = AssetManagerUtil.readTestsFromJSON(requireContext(), "db_tests.json");

        // Настройка адаптеров для спиннеров
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, groupNames);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);

        ArrayAdapter<String> testAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, testNames);
        testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testSpinner.setAdapter(testAdapter);

        // Обработка выбора группы из спиннера
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedGroup = groupDataList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedGroup = null;
            }
        });

        // Обработка выбора теста из спиннера
        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedTestName = testNames.get(position);
                selectedTestSet = getTestSetByName(selectedTestName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedTestSet = null;
            }
        });

        // Обработка нажатия кнопки
        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedGroup != null && selectedTestSet != null) {
                    String groupName = selectedGroup.getName();
                     Intent intent = new Intent(getActivity(), RunningTest.class);
                     intent.putExtra("groupName", groupName);
                     intent.putExtra("testName", selectedTestName);

                     startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Not chosen group or test", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

//    private TestSet getTestSetByName(String testName) {
//        for (TestSet testSet : testDataList) {
//            if (testSet.getTests().containsKey(testName)) {
//                return testSet;
//            }
//        }
//        return null;
//    }
    private TestSet getTestSetByName(String testName) {
        ArrayList<TestSet> testDataList = AssetManagerUtil.readTestsFromJSON(requireContext(), "db_tests.json");
        for (TestSet testSet : testDataList) {
            Map<String, List<QuestionData>> tests = testSet.getTests();
            if (tests.containsKey(testName)) {
                return testSet;
            }
        }
        return null;
    }

}
