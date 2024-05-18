package com.example.version0;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailTest extends AppCompatActivity {

    TextView testNameTextView, numberOfQuestionsTextView, questionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_test);

        testNameTextView = findViewById(R.id.detailNameTest);
        numberOfQuestionsTextView = findViewById(R.id.detailNumberTest);
        questionsTextView = findViewById(R.id.detailQuestions);

        // Получаем данные о тесте из интента
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String testName = extras.getString("testName");
            int numberOfQuestions = extras.getInt("numberQuestions");
            ArrayList<String> questions = extras.getStringArrayList("questions");
            ArrayList<OptionData> options = (ArrayList<OptionData>) getIntent().getSerializableExtra("options");

            // Устанавливаем полученные данные в соответствующие TextView
            testNameTextView.setText(testName);
            numberOfQuestionsTextView.setText(String.valueOf(numberOfQuestions));

            StringBuilder questionsStringBuilder = new StringBuilder();
            if (questions != null) {
                for (String question : questions) {
                    questionsStringBuilder.append(question).append("\n");
                }
            }
            questionsTextView.setText(questionsStringBuilder.toString());

            // Добавляем варианты ответов в текст вопросов
            if (options != null) {
                for (OptionData option : options) {
                    SpannableStringBuilder optionText = new SpannableStringBuilder(option.getOption());
                    // Помечаем правильный вариант ответа
                    if (option.isCorrect()) {
                        optionText.append(" (+)");
                        optionText.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.holo_green_dark)), optionText.length() - 4, optionText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    questionsTextView.append("\n" + optionText);
                }
            }
        }
    }
}
