package com.example.version0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<GroupData> {
    public GroupAdapter(@NonNull Context context, ArrayList<GroupData> dataArrayList) {
        super(context, R.layout.groups_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        GroupData groupData = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.groups_item, parent, false);
        }
        TextView groupName = view.findViewById(R.id.groupName);
        TextView numberStudents = view.findViewById(R.id.numberStudents);

        groupName.setText(groupData.getName()); // Используем метод getName(), чтобы получить имя группы
        numberStudents.setText(String.valueOf(groupData.getNumber())); // Преобразуем число в строку перед установкой в TextView

        return view;
    }
}

