package com.example.qld.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    
    // Thông tin database
    private static final String DATABASE_NAME = "StudentManager.db";
    private static final int DATABASE_VERSION = 1;
    
    // Tên các bảng
    public static final String TABLE_USERS = "users";
    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_SUBJECTS = "subjects";
    public static final String TABLE_SCORES = "scores";
    
    // Cột của bảng users
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_CREATED_DATE = "created_date";
    
    // Cột của bảng students
    public static final String COLUMN_STUDENT_ID = "id";
    public static final String COLUMN_STUDENT_USER_ID = "user_id";
    public static final String COLUMN_STUDENT_CODE = "student_code";
    public static final String COLUMN_CLASS_NAME = "class_name";
    public static final String COLUMN_BIRTH_DATE = "birth_date";
    
    // Cột của bảng subjects
    public static final String COLUMN_SUBJECT_ID = "id";
    public static final String COLUMN_SUBJECT_NAME = "subject_name";
    public static final String COLUMN_SUBJECT_CODE = "subject_code";
    
    // Cột của bảng scores
    public static final String COLUMN_SCORE_ID = "id";
    public static final String COLUMN_SCORE_STUDENT_ID = "student_id";
    public static final String COLUMN_SCORE_SUBJECT_ID = "subject_id";
    public static final String COLUMN_SCORE_TYPE = "score_type";
    public static final String COLUMN_SCORE_VALUE = "score";
    public static final String COLUMN_DATE_CREATED = "date_created";
    public static final String COLUMN_TEACHER_ID = "teacher_id";
    
    // Truy vấn tạo bảng users
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
            COLUMN_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_ROLE + " INTEGER NOT NULL, " +
            COLUMN_FULL_NAME + " TEXT NOT NULL, " +
            COLUMN_CREATED_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP" +
            ")";
    
    // Truy vấn tạo bảng students
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + " (" +
            COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_STUDENT_USER_ID + " INTEGER, " +
            COLUMN_STUDENT_CODE + " TEXT UNIQUE NOT NULL, " +
            COLUMN_CLASS_NAME + " TEXT NOT NULL, " +
            COLUMN_BIRTH_DATE + " TEXT, " +
            "FOREIGN KEY (" + COLUMN_STUDENT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
            ")";
    
    // Truy vấn tạo bảng subjects
    private static final String CREATE_TABLE_SUBJECTS = "CREATE TABLE " + TABLE_SUBJECTS + " (" +
            COLUMN_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SUBJECT_NAME + " TEXT NOT NULL, " +
            COLUMN_SUBJECT_CODE + " TEXT UNIQUE NOT NULL" +
            ")";
    
    // Truy vấn tạo bảng scores
    private static final String CREATE_TABLE_SCORES = "CREATE TABLE " + TABLE_SCORES + " (" +
            COLUMN_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SCORE_STUDENT_ID + " INTEGER, " +
            COLUMN_SCORE_SUBJECT_ID + " INTEGER, " +
            COLUMN_SCORE_TYPE + " TEXT NOT NULL, " +
            COLUMN_SCORE_VALUE + " REAL NOT NULL, " +
            COLUMN_DATE_CREATED + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            COLUMN_TEACHER_ID + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_SCORE_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_STUDENT_ID + "), " +
            "FOREIGN KEY (" + COLUMN_SCORE_SUBJECT_ID + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_SUBJECT_ID + "), " +
            "FOREIGN KEY (" + COLUMN_TEACHER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
            ")";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Đang tạo các bảng trong database");
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_SUBJECTS);
        db.execSQL(CREATE_TABLE_SCORES);
        
        // Chèn dữ liệu mẫu
        insertSampleData(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Đang nâng cấp database từ phiên bản " + oldVersion + " lên " + newVersion);
        // Xóa các bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        
        // Tạo lại các bảng
        onCreate(db);
    }
    
    private void insertSampleData(SQLiteDatabase db) {
        Log.d(TAG, "Đang chèn dữ liệu mẫu");
        
        // Chèn người dùng mẫu (giáo viên và học sinh)
        db.execSQL("INSERT INTO " + TABLE_USERS + " VALUES (1, 'tc1', '123456', 1, 'Nguyễn Văn Giáo', datetime('now'))");
        db.execSQL("INSERT INTO " + TABLE_USERS + " VALUES (2, 'st1', '123456', 0, 'Trần Thị Học', datetime('now'))");
        db.execSQL("INSERT INTO " + TABLE_USERS + " VALUES (3, 'st2', '123456', 0, 'Lê Văn Sinh', datetime('now'))");
        
        // Chèn học sinh mẫu
        db.execSQL("INSERT INTO " + TABLE_STUDENTS + " VALUES (1, 2, 'STU001', 'Class 10A1', '2005-05-15')");
        db.execSQL("INSERT INTO " + TABLE_STUDENTS + " VALUES (2, 3, 'STU002', 'Class 10A1', '2005-08-22')");
        
        // Chèn môn học mẫu
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " VALUES (1, 'Toán', 'TOAN')");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " VALUES (2, 'Lý', 'LY')");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " VALUES (3, 'Hóa', 'HOA')");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " VALUES (4, 'Văn', 'VAN')");
        
        // Chèn điểm mẫu
        db.execSQL("INSERT INTO " + TABLE_SCORES + " VALUES (1, 1, 1, 'mieng', 8.5, datetime('now'), 1)");
        db.execSQL("INSERT INTO " + TABLE_SCORES + " VALUES (2, 1, 2, '15phut', 7.0, datetime('now'), 1)");
        db.execSQL("INSERT INTO " + TABLE_SCORES + " VALUES (3, 2, 1, 'mieng', 9.0, datetime('now'), 1)");
        db.execSQL("INSERT INTO " + TABLE_SCORES + " VALUES (4, 2, 3, '1tiet', 8.0, datetime('now'), 1)");
    }
}