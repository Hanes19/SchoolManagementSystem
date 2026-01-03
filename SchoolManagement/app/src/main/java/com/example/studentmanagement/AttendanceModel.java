package com.example.studentmanagement;

public class AttendanceModel {
    private String id;
    private String name;
    private String status;
    private String date;
    private String remarks;

    // 1. Empty Constructor (For Student View)
    public AttendanceModel() {
    }

    // 2. Teacher Constructor (For Teacher View)
    public AttendanceModel(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}