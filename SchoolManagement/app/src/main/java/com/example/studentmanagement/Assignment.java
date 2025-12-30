package com.example.studentmanagement;

public class Assignment {
    private String title;
    private String dueDate;
    private String subject;
    private int maxPoints; // Changed from score/maxScore to match your Adapter error
    private boolean isSubmitted;

    public Assignment(String title, String dueDate, String subject, int maxPoints, boolean isSubmitted) {
        this.title = title;
        this.dueDate = dueDate;
        this.subject = subject;
        this.maxPoints = maxPoints;
        this.isSubmitted = isSubmitted;
    }

    public String getTitle() { return title; }
    public String getDueDate() { return dueDate; }
    public String getSubject() { return subject; }
    public int getMaxPoints() { return maxPoints; } // Fixed method name
    public boolean isSubmitted() { return isSubmitted; }
}