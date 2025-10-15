package com.example.qld.models;

public class Student {
    private int id;
    private int userId;
    private String studentCode;
    private String className;
    private String birthDate;

    public Student() {}

    public Student(int id, int userId, String studentCode, String className, String birthDate) {
        this.id = id;
        this.userId = userId;
        this.studentCode = studentCode;
        this.className = className;
        this.birthDate = birthDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
}