package com.example.version0;
import java.util.ArrayList;

public class GroupData {
    private String name;
    private int number;
    private ArrayList<String> students;

    public GroupData(String name, int number, ArrayList<String> students) {
        this.name = name;
        this.number = number;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<String> getStudents() {
        return students;
    }
}


