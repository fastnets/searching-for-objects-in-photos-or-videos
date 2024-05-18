package com.example.version0;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AssetManagerUtil {
    public static ArrayList<String> readGroupNamesFromJSON(Context context, String filename) {
        ArrayList<String> groupNames = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String groupName = keys.next();
                groupNames.add(groupName);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("AssetManagerUtil", "Error reading JSON file: " + e.getMessage());
        }
        return groupNames;
    }


    public static ArrayList<String> readTestNamesFromJSON(Context context, String filename) {
        ArrayList<String> testNames = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String testName = keys.next();
                testNames.add(testName);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("AssetManagerUtil", "Error reading JSON file: " + e.getMessage());
        }
        return testNames;
    }


    public static ArrayList<GroupData> readGroupsFromJSON(Context context, String filename) {
        ArrayList<GroupData> dataArrayList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String groupName = keys.next();
                JSONArray groupArray = jsonObject.getJSONArray(groupName);
                for (int i = 0; i < groupArray.length(); i++) {
                    JSONObject groupObject = groupArray.getJSONObject(i);
                    int number = groupObject.getInt("number");
                    JSONArray studentsArray = groupObject.getJSONArray("students");
                    ArrayList<String> students = new ArrayList<>();
                    for (int j = 0; j < studentsArray.length(); j++) {
                        students.add(studentsArray.getString(j));
                    }
                    dataArrayList.add(new GroupData(groupName, number, students));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("AssetManagerUtil", "Error reading JSON file: " + e.getMessage());
        }
        return dataArrayList;
    }

    public static ArrayList<TestSet> readTestsFromJSON(Context context, String filename) {
        ArrayList<TestSet> dataArrayList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String testName = keys.next();
                JSONArray testArray = jsonObject.getJSONArray(testName);

                List<QuestionData> questions = new ArrayList<>();
                for (int i = 0; i < testArray.length(); i++) {
                    JSONObject testObject = testArray.getJSONObject(i);
                    int id = testObject.getInt("id");
                    String questionText = testObject.getString("question");
                    JSONArray optionsArray = testObject.getJSONArray("options");

                    List<OptionData> options = new ArrayList<>();
                    for (int j = 0; j < optionsArray.length(); j++) {
                        JSONObject optionObject = optionsArray.getJSONObject(j);
                        String optionText = optionObject.getString("option");
                        boolean isCorrect = optionObject.getBoolean("isCorrect");
                        options.add(new OptionData(optionText, isCorrect));
                    }

                    questions.add(new QuestionData(id, questionText, options));
                }

                Map<String, List<QuestionData>> testDataMap = new HashMap<>();
                testDataMap.put(testName, questions);
                dataArrayList.add(new TestSet(testDataMap));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("AssetManagerUtil", "Error reading JSON file: " + e.getMessage());
        }
        return dataArrayList;
    }

    public static void writeResponseToJSON(Context context, String filename, String studentName, String testName, int questionID, String answer, boolean option) {
        try {
            // Чтение текущего содержимого файла
            String json = "";
            try (InputStream is = context.getAssets().open(filename)) {
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, StandardCharsets.UTF_8);
            }

            // Преобразование содержимого в JSONObject
            JSONObject jsonObject = new JSONObject(json);

            // Получаем или создаем объект для студента
            JSONObject studentObject = jsonObject.optJSONObject(studentName);
            if (studentObject == null) {
                studentObject = new JSONObject();
                jsonObject.put(studentName, studentObject);
            }

            // Получаем или создаем объект для теста
            JSONObject testObject = studentObject.optJSONObject(testName);
            if (testObject == null) {
                testObject = new JSONObject();
                studentObject.put(testName, testObject);
            }

            // Получаем или создаем массив ответов
            JSONArray testArray = testObject.optJSONArray("answers");
            if (testArray == null) {
                testArray = new JSONArray();
                testObject.put("answers", testArray);
            }

            // Создаем объект для текущего ответа
            JSONObject answerObject = new JSONObject();
            answerObject.put("questionID", questionID);
            answerObject.put("answer", answer);
            answerObject.put("option", option);

            // Добавляем ответ в массив
            testArray.put(answerObject);

            // Запись обновленного JSONObject в файл
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE))) {
                outputStreamWriter.write(jsonObject.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("AssetManagerUtil", "Error writing JSON file: " + e.getMessage());
        }
    }



}
