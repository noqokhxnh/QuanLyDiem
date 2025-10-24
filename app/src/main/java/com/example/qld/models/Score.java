package com.example.qld.models;

/**
 * Lớp đại diện cho điểm số trong hệ thống
 * Chứa thông tin về học sinh, môn học, loại điểm và điểm số
 */
public class Score {
    private int id;
    private int studentId;
    private int subjectId;
    private String scoreType; // 'mieng', '15phut', '1tiet', 'hocky'
    private double score;
    private String dateCreated;
    private int teacherId;

    /**
     * Constructor mặc định
     */
    public Score() {}

    /**
     * Constructor đầy đủ tham số
     * @param id ID của điểm số
     * @param studentId ID của học sinh
     * @param subjectId ID của môn học
     * @param scoreType Loại điểm ('mieng', '15phut', '1tiet', 'hocky')
     * @param score Điểm số
     * @param dateCreated Ngày tạo điểm
     * @param teacherId ID của giáo viên tạo điểm
     */
    public Score(int id, int studentId, int subjectId, String scoreType, double score, String dateCreated, int teacherId) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.scoreType = scoreType;
        this.score = score;
        this.dateCreated = dateCreated;
        this.teacherId = teacherId;
    }

    // Getters and Setters
    /**
     * Lấy ID của điểm số
     * @return ID của điểm số
     */
    public int getId() { return id; }
    /**
     * Thiết lập ID cho điểm số
     * @param id ID của điểm số
     */
    public void setId(int id) { this.id = id; }

    /**
     * Lấy ID của học sinh
     * @return ID của học sinh
     */
    public int getStudentId() { return studentId; }
    /**
     * Thiết lập ID cho học sinh
     * @param studentId ID của học sinh
     */
    public void setStudentId(int studentId) { this.studentId = studentId; }

    /**
     * Lấy ID của môn học
     * @return ID của môn học
     */
    public int getSubjectId() { return subjectId; }
    /**
     * Thiết lập ID cho môn học
     * @param subjectId ID của môn học
     */
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    /**
     * Lấy loại điểm
     * @return Loại điểm ('mieng', '15phut', '1tiet', 'hocky')
     */
    public String getScoreType() { return scoreType; }
    /**
     * Thiết lập loại điểm
     * @param scoreType Loại điểm ('mieng', '15phut', '1tiet', 'hocky')
     */
    public void setScoreType(String scoreType) { this.scoreType = scoreType; }

    /**
     * Lấy điểm số
     * @return Điểm số
     */
    public double getScore() { return score; }
    /**
     * Thiết lập điểm số
     * @param score Điểm số
     */
    public void setScore(double score) { this.score = score; }

    /**
     * Lấy ngày tạo điểm
     * @return Ngày tạo điểm
     */
    public String getDateCreated() { return dateCreated; }
    /**
     * Thiết lập ngày tạo điểm
     * @param dateCreated Ngày tạo điểm
     */
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }

    /**
     * Lấy ID của giáo viên tạo điểm
     * @return ID của giáo viên tạo điểm
     */
    public int getTeacherId() { return teacherId; }
    /**
     * Thiết lập ID cho giáo viên tạo điểm
     * @param teacherId ID của giáo viên tạo điểm
     */
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
}