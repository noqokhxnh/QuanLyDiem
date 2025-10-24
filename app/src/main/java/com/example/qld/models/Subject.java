package com.example.qld.models;

/**
 * Lớp đại diện cho môn học trong hệ thống
 * Chứa thông tin về tên và mã môn học
 */
public class Subject {
    private int id;
    private String subjectName;
    private String subjectCode;

    /**
     * Constructor mặc định
     */
    public Subject() {}

    /**
     * Constructor đầy đủ tham số
     * @param id ID của môn học
     * @param subjectName Tên của môn học
     * @param subjectCode Mã của môn học
     */
    public Subject(int id, String subjectName, String subjectCode) {
        this.id = id;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    // Getters and Setters
    /**
     * Lấy ID của môn học
     * @return ID của môn học
     */
    public int getId() { return id; }
    /**
     * Thiết lập ID cho môn học
     * @param id ID của môn học
     */
    public void setId(int id) { this.id = id; }

    /**
     * Lấy tên môn học
     * @return Tên môn học
     */
    public String getSubjectName() { return subjectName; }
    /**
     * Thiết lập tên môn học
     * @param subjectName Tên môn học
     */
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    /**
     * Lấy mã môn học
     * @return Mã môn học
     */
    public String getSubjectCode() { return subjectCode; }
    /**
     * Thiết lập mã môn học
     * @param subjectCode Mã môn học
     */
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
}