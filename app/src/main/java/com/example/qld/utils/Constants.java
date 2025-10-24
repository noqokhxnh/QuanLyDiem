package com.example.qld.utils;

/**
 * Lớp chứa các hằng số dùng chung trong ứng dụng
 */
public class Constants {
    public static final int ROLE_STUDENT = 0;
    public static final int ROLE_TEACHER = 1;
    
    // Các loại điểm
    public static final String SCORE_TYPE_MIENG = "mieng";
    public static final String SCORE_TYPE_15PHUT = "15phut";
    public static final String SCORE_TYPE_1TIET = "1tiet";
    public static final String SCORE_TYPE_HOCKY = "hocky";
    
    // Các khóa Intent
    public static final String INTENT_USER_ID = "user_id";
    public static final String INTENT_USER_ROLE = "user_role";
    public static final String INTENT_STUDENT_ID = "student_id";
    public static final String INTENT_SUBJECT_ID = "subject_id";
    
    // Liên quan đến cơ sở dữ liệu
    public static final String DATABASE_NAME = "StudentManager.db";
    public static final int DATABASE_VERSION = 1;
}