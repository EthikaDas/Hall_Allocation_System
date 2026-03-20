package com.example.myapplication;

public class ComplaintResponse {
    private int id;
    private int studentRoll;
    private String studentName;
    private String category;
    private String details;
    private String status;

    public ComplaintResponse(int id, int studentRoll, String studentName, String category, String details, String status) {
        this.id = id;
        this.studentRoll = studentRoll;
        this.studentName = studentName;
        this.category = category;
        this.details = details;
        this.status = status;
    }


    public int getId() { return id; }
    public int getStudentRoll() { return studentRoll; }
    public String getStudentName() { return studentName; }
    public String getCategory() { return category; }
    public String getDetails() { return details; }
    public String getStatus() { return status; }

}