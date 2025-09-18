package com.example.qld.models;

/**
 * Lớp mô hình học sinh
 * Đại diện cho một học sinh trong hệ thống
 */
public class Student {
    private int id;
    private int userId;
    private String studentCode;
    private String className;
    private String birthDate;
    
    // Các constructor
    public Student() {
    }
    
    public Student(int userId, String studentCode, String className, String birthDate) {
        this.userId = userId;
        this.studentCode = studentCode;
        this.className = className;
        this.birthDate = birthDate;
    }
    
    public Student(int id, int userId, String studentCode, String className, String birthDate) {
        this.id = id;
        this.userId = userId;
        this.studentCode = studentCode;
        this.className = className;
        this.birthDate = birthDate;
    }
    
    // Các phương thức getter và setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getStudentCode() {
        return studentCode;
    }
    
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}