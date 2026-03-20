package com.example.myapplication;

public class FindSeniorResponse {
    private int roll;
    private String name;
    private String dept;
    private int series;
    private String phone;
    private String email;
    private String photo;
    private String roomNumber;

    public FindSeniorResponse(int roll, String name, String dept, int series,
                              String phone, String email, String photo, String roomNumber) {
        this.roll = roll;
        this.name = name;
        this.dept = dept;
        this.series = series;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
        this.roomNumber = roomNumber;
    }


    public int getRoll() { return roll; }
    public String getName() { return name; }
    public String getDept() { return dept; }
    public int getSeries() { return series; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPhoto() { return photo; }
    public String getRoomNumber() { return roomNumber; }
}