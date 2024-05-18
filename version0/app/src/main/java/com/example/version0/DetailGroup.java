package com.example.version0;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.version0.databinding.ActivityDetailGroupBinding;

import java.util.ArrayList;

public class DetailGroup extends AppCompatActivity {

    ActivityDetailGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailGroupBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String number = intent.getStringExtra("number");
            ArrayList<String> studentsList = intent.getStringArrayListExtra("students");

            binding.detailName.setText(name);
            binding.detailNumber.setText(number);
            if (studentsList != null) {
                StringBuilder studentsStringBuilder = new StringBuilder();
                for (String student : studentsList) {
                    studentsStringBuilder.append(student).append("\n");
                }
                binding.detailStudentsNames.setText(studentsStringBuilder.toString());
            }
        }

    }
}