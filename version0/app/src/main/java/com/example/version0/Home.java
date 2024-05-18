package com.example.version0;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView runTestTextView = view.findViewById(R.id.runTest);
        runTestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем экземпляр фрагмента ChooseGroupAndTestFragment
                ChooseGroupAndTestFragment fragment = new ChooseGroupAndTestFragment();

                // Заменяем текущий фрагмент на ChooseGroupAndTestFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null); // Добавляем транзакцию в стек возврата
                transaction.commit();
            }
        });

        return view;
    }
}
