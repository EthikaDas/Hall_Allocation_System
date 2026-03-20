package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class ApplicationResponse {
    private int id;
    private int studentRoll;
    private String hallName;
    private List<Integer> roommates;
    private String status;
    private String gradesheetPath;

    public ApplicationResponse(int id, int studentRoll, String hallName, List<Integer> roommates, String status, String gradesheetPath) {
        this.id = id;
        this.studentRoll = studentRoll;
        this.hallName = hallName;
        this.roommates = roommates;
        this.status = status;
        this.gradesheetPath = gradesheetPath;
    }


    public int getId() { return id; }
    public int getStudentRoll() { return studentRoll; }
    public String getHallName() { return hallName; }
    public List<Integer> getRoommates() { return roommates; }
    public String getStatus() { return status; }

    public String getGradesheetPath() {
        return gradesheetPath;
    }
}