package com.example.studentmanagement;

public class StudentGradeModel {
    private String id;
    private String name;
    private int currentScore;

    public StudentGradeModel(String id, String name, int currentScore) {
        this.id = id;
        this.name = name;
        this.currentScore = currentScore;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCurrentScore() { return currentScore; }
    public void setCurrentScore(int score) { this.currentScore = score; }
}