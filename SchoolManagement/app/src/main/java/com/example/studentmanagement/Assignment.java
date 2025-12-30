package com.example.studentmanagement;

public class Assignment {
    private String title;
    private String dueDate;
    private String subject;
    private int maxPoints;
    private boolean isOpen; // To toggle the "OPEN" badge if needed

    public Assignment(String title, String dueDate, String subject, int maxPoints, boolean isOpen) {
        this.title = title;
        this.dueDate = dueDate;
        this.subject = subject;
        this.maxPoints = maxPoints;
        this.isOpen = isOpen;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public String getSubject() { return subject; }
    public int getMaxPoints() { return maxPoints; }
    public boolean isOpen() { return isOpen; }
}