package com.example.qld.models;

/**
 * Lớp đại diện cho học sinh trong hệ thống
 * Chứa thông tin cá nhân và điểm trung bình của học sinh
 */
public class Student {
    private int id;
    private String studentCode;
    private String fullName;
    private String className;
    private double average;

    /**
     * Constructor mặc định
     */
    public Student() {}

    /**
     * Constructor đầy đủ tham số
     * @param id ID của học sinh
     * @param studentCode Mã học sinh
     * @param fullName Họ tên đầy đủ của học sinh
     * @param className Tên lớp của học sinh
     * @param average Điểm trung bình của học sinh
     */
    public Student(int id, String studentCode, String fullName, String className, double average) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.className = className;
        this.average = average;
    }

    // Getters and Setters
    /**
     * Lấy ID của học sinh
     * @return ID của học sinh
     */
    public int getId() { return id; }
    /**
     * Thiết lập ID cho học sinh
     * @param id ID của học sinh
     */
    public void setId(int id) { this.id = id; }

    /**
     * Lấy mã học sinh
     * @return Mã học sinh
     */
    public String getStudentCode() { return studentCode; }
    /**
     * Thiết lập mã học sinh
     * @param studentCode Mã học sinh
     */
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    /**
     * Lấy họ tên đầy đủ của học sinh
     * @return Họ tên đầy đủ của học sinh
     */
    public String getFullName() { return fullName; }
    /**
     * Thiết lập họ tên đầy đủ cho học sinh
     * @param fullName Họ tên đầy đủ của học sinh
     */
    public void setFullName(String fullName) { this.fullName = fullName; }

    /**
     * Lấy tên lớp của học sinh
     * @return Tên lớp của học sinh
     */
    public String getClassName() { return className; }
    /**
     * Thiết lập tên lớp cho học sinh
     * @param className Tên lớp của học sinh
     */
    public void setClassName(String className) { this.className = className; }

    /**
     * Lấy điểm trung bình của học sinh
     * @return Điểm trung bình của học sinh
     */
    public double getAverage() { return average; }
    /**
     * Thiết lập điểm trung bình cho học sinh
     * @param average Điểm trung bình của học sinh
     */
    public void setAverage(double average) { this.average = average; }
}