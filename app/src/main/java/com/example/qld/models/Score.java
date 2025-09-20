package com.example.qld.models;

public class Score {
    private int id;
    private int studentId;
    private int subjectId;
    private String scoreType;
    private double score;
    private String dateCreated;
    private int teacherId;
    
    // Các constructor
    public Score() {
    }
    
    public Score(int studentId, int subjectId, String scoreType, double score, int teacherId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.scoreType = scoreType;
        this.score = score;
        this.teacherId = teacherId;
    }
    
    public Score(int id, int studentId, int subjectId, String scoreType, double score, String dateCreated, int teacherId) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.scoreType = scoreType;
        this.score = score;
        this.dateCreated = dateCreated;
        this.teacherId = teacherId;
    }
    
    // Các phương thức getter và setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    
    public String getScoreType() {
        return scoreType;
    }
    
    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }
    
    public double getScore() {
        return score;
    }
    
    public void setScore(double score) {
        this.score = score;
    }
    
    public String getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public int getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
}