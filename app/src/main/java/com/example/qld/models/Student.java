package com.example.qld.models;

public class Student {
    private int id;
    private String studentCode;
    private String fullName;
    private String className;
    private double average;

    public Student() {}

    public Student(int id, String studentCode, String fullName, String className, double average) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.className = className;
        this.average = average;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public double getAverage() { return average; }
    public void setAverage(double average) { this.average = average; }
}