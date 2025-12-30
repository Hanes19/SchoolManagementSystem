package com.example.studentmanagement;

public class AttendanceModel {
    private String id;
    private String name;
    private String status;

    public AttendanceModel(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}