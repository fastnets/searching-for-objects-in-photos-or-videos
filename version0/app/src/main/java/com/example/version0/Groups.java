package com.example.version0;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.version0.databinding.FragmentGroupsBinding;

import java.util.ArrayList;

public class Groups extends Fragment {
    private FragmentGroupsBinding binding;
    private GroupAdapter groupAdapter;
    private ArrayList<GroupData> dataArrayList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("GroupsFragment", "onCreateView()");
        binding = FragmentGroupsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ListView listView = binding.listView;
        dataArrayList = AssetManagerUtil.readGroupsFromJSON(requireContext(), "db_groups.json");

        groupAdapter = new GroupAdapter(getActivity(), dataArrayList);
        listView.setAdapter(groupAdapter);

        groupAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroupData selectedGroup = dataArrayList.get(i);

                Intent intent = new Intent(getActivity(), DetailGroup.class);
                intent.putExtra("name", selectedGroup.getName());
                intent.putExtra("number", selectedGroup.getNumber());
                intent.putStringArrayListExtra("students", selectedGroup.getStudents());
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
