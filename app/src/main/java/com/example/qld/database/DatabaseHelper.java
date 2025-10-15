package com.example.qld.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentManager.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_SUBJECTS = "subjects";
    public static final String TABLE_SCORES = "scores";

    // Common column names
    public static final String COLUMN_ID = "id";

    // Users table columns
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_CREATED_DATE = "created_date";

    // Students table columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_STUDENT_CODE = "student_code";
    public static final String COLUMN_CLASS_NAME = "class_name";
    public static final String COLUMN_BIRTH_DATE = "birth_date";

    // Subjects table columns
    public static final String COLUMN_SUBJECT_NAME = "subject_name";
    public static final String COLUMN_SUBJECT_CODE = "subject_code";

    // Scores table columns
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_SUBJECT_ID = "subject_id";
    public static final String COLUMN_SCORE_TYPE = "score_type";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_DATE_CREATED = "date_created";
    public static final String COLUMN_TEACHER_ID = "teacher_id";

    // Create Users table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL,"
            + COLUMN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_ROLE + " INTEGER NOT NULL," // 0: học sinh, 1: giáo viên
            + COLUMN_FULL_NAME + " TEXT NOT NULL,"
            + COLUMN_CREATED_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP"
            + ")";

    // Create Students table
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_STUDENT_CODE + " TEXT UNIQUE NOT NULL,"
            + COLUMN_CLASS_NAME + " TEXT NOT NULL,"
            + COLUMN_BIRTH_DATE + " TEXT,"
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
            + ")";

    // Create Subjects table
    private static final String CREATE_TABLE_SUBJECTS = "CREATE TABLE " + TABLE_SUBJECTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SUBJECT_NAME + " TEXT NOT NULL,"
            + COLUMN_SUBJECT_CODE + " TEXT UNIQUE NOT NULL"
            + ")";

    // Create Scores table
    private static final String CREATE_TABLE_SCORES = "CREATE TABLE " + TABLE_SCORES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_STUDENT_ID + " INTEGER,"
            + COLUMN_SUBJECT_ID + " INTEGER,"
            + COLUMN_SCORE_TYPE + " TEXT NOT NULL," // 'mieng', '15phut', '1tiet', 'hocky'
            + COLUMN_SCORE + " REAL NOT NULL,"
            + COLUMN_DATE_CREATED + " TEXT DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_TEACHER_ID + " INTEGER,"
            + "FOREIGN KEY (" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY (" + COLUMN_SUBJECT_ID + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_ID + "),"
            + "FOREIGN KEY (" + COLUMN_TEACHER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_STUDENTS);
            db.execSQL(CREATE_TABLE_SUBJECTS);
            db.execSQL(CREATE_TABLE_SCORES);
            
            // Insert sample data
            insertSampleData(db);
            
            Log.d("DatabaseHelper", "Database tables created successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        
        // Create tables again
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        try {
            // Insert sample users (teacher and students)
            String insertUsers = "INSERT INTO " + TABLE_USERS + " VALUES " +
                "(1, 'teacher1', '123456', 1, 'Nguyễn Văn Giáo', datetime('now')), " +
                "(2, 'student1', '123456', 0, 'Trần Thị Học', datetime('now')), " +
                "(3, 'student2', '123456', 0, 'Lê Văn Sinh', datetime('now'));";
            db.execSQL(insertUsers);

            // Insert sample subjects
            String insertSubjects = "INSERT INTO " + TABLE_SUBJECTS + " VALUES " +
                "(1, 'Toán', 'TOAN'), " +
                "(2, 'Lý', 'LY'), " +
                "(3, 'Hóa', 'HOA'), " +
                "(4, 'Văn', 'VAN');";
            db.execSQL(insertSubjects);

            // Insert sample students
            String insertStudents = "INSERT INTO " + TABLE_STUDENTS + " VALUES " +
                "(1, 2, 'HS001', '10A1', '2005-05-15'), " +
                "(2, 3, 'HS002', '10A2', '2005-06-20');";
            db.execSQL(insertStudents);

            // Insert sample scores
            String insertScores = "INSERT INTO " + TABLE_SCORES + " VALUES " +
                "(1, 1, 1, 'mieng', 8.5, datetime('now'), 1), " +
                "(2, 1, 1, '15phut', 7.0, datetime('now'), 1), " +
                "(3, 1, 1, '1tiet', 8.0, datetime('now'), 1), " +
                "(4, 1, 1, 'hocky', 7.5, datetime('now'), 1);";
            db.execSQL(insertScores);
            
            Log.d("DatabaseHelper", "Sample data inserted successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting sample data: " + e.getMessage());
        }
    }
}