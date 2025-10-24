package com.example.qld.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qld.utils.CacheManager;
import com.example.qld.utils.PasswordUtil;

/**
 * Lớp hỗ trợ tạo và quản lý cơ sở dữ liệu của ứng dụng
 * Bao gồm các bảng: users, students, subjects, scores
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentManager.db";
    private static final int DATABASE_VERSION = 3; // Incremented to force database recreation with updated schema

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
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_ROLE = "role"; // 'ADMIN', 'TEACHER', 'STUDENT'
    public static final String COLUMN_USER_STUDENT_ID = "student_id"; // Foreign key to Students table
    public static final String COLUMN_CREATED_DATE = "created_date";

    // Students table columns
    public static final String COLUMN_STUDENT_CODE = "student_code";
    public static final String COLUMN_CLASS_NAME = "class_name";
    public static final String COLUMN_AVERAGE = "average";

    // Subjects table columns
    public static final String COLUMN_SUBJECT_NAME = "subject_name";
    public static final String COLUMN_SUBJECT_CODE = "subject_code";

    // Scores table columns
    public static final String COLUMN_SCORE_STUDENT_ID = "student_id";
    public static final String COLUMN_SUBJECT_ID = "subject_id";
    public static final String COLUMN_SCORE_TYPE = "score_type";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_DATE_CREATED = "date_created";
    public static final String COLUMN_TEACHER_ID = "teacher_id";

    // Create Users table
    private static final String CREATE_TABLE_USERS = "create table " + TABLE_USERS + "("
            + COLUMN_ID + " interger primary key autoincrement,"
            + COLUMN_USERNAME + " text unique not null,"
            + COLUMN_PASSWORD + " text not null,"
            + COLUMN_FULL_NAME + " text not null,"
            + COLUMN_ROLE + " text check(" + COLUMN_ROLE + " in ('Admin', 'Teacher', 'Student')) not null default 'Student',"
            + COLUMN_USER_STUDENT_ID + " interger,"
            + COLUMN_CREATED_DATE + " text default CURRENT_TIMESTAMP"
            + ")";

    // Create Students table
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + "("
            + COLUMN_ID + " interger primary key autoincrement,"
            + COLUMN_STUDENT_CODE + " text unique not null,"
            + COLUMN_FULL_NAME + " text not null,"
            + COLUMN_CLASS_NAME + " text not null,"
            + COLUMN_AVERAGE + " real default 0"
            + ")";

    // Create Subjects table
    private static final String CREATE_TABLE_SUBJECTS = "CREATE TABLE " + TABLE_SUBJECTS + "("
            + COLUMN_ID + " interger primary key autoincrement,"
            + COLUMN_SUBJECT_NAME + " text not null,"
            + COLUMN_SUBJECT_CODE + " text unique not null"
            + ")";

    // Create Scores table
    private static final String CREATE_TABLE_SCORES = "CREATE TABLE " + TABLE_SCORES + "("
            + COLUMN_ID + " interger primary key autoincrement,"
            + COLUMN_SCORE_STUDENT_ID + " interger,"
            + COLUMN_SUBJECT_ID + " interger,"
            + COLUMN_SCORE_TYPE + " text not null," // 'mieng', '15phut', '1tiet', 'hocky'
            + COLUMN_SCORE + " real not null,"
            + COLUMN_DATE_CREATED + " text default CURRENT_TIMESTAMP,"
            + COLUMN_TEACHER_ID + " interger"
            + ")";

    /**
     * Constructor cho DatabaseHelper
     * @param context Context của ứng dụng
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Phương thức được gọi khi cơ sở dữ liệu được tạo lần đầu
     * Tạo các bảng và chèn dữ liệu mẫu
     * @param db Đối tượng SQLiteDatabase để thực hiện các truy vấn
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create tables in correct order to satisfy foreign key constraints
            db.execSQL(CREATE_TABLE_STUDENTS);
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_SUBJECTS);
            db.execSQL(CREATE_TABLE_SCORES);
            
            // Insert sample data
            insertSampleData(db);
            
            Log.d("DatabaseHelper", "Database tables created successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating tables: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    /**
     * Phương thức được gọi khi phiên bản cơ sở dữ liệu được nâng cấp
     * Xóa bảng cũ và tạo lại bảng mới
     * @param db Đối tượng SQLiteDatabase để thực hiện các truy vấn
     * @param oldVersion Phiên bản cũ của cơ sở dữ liệu
     * @param newVersion Phiên bản mới của cơ sở dữ liệu
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Clear cache when database is upgraded
        CacheManager.getInstance().clearUserCache();
        
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        
        // Create tables again
        onCreate(db);
    }

    /**
     * Chèn dữ liệu mẫu vào cơ sở dữ liệu
     * Bao gồm người dùng mẫu, học sinh mẫu, môn học mẫu và điểm mẫu
     * @param db Đối tượng SQLiteDatabase để thực hiện các truy vấn
     */
    private void insertSampleData(SQLiteDatabase db) {
        try {
            // Hash the default passwords
            String adminPassword = PasswordUtil.hashPassword("123456");
            String teacherPassword = PasswordUtil.hashPassword("123456");
            String student1Password = PasswordUtil.hashPassword("123456");
            String student2Password = PasswordUtil.hashPassword("123456");
            
            // Insert sample students first (since users table references them)
            String insertStudents = "INSERT INTO " + TABLE_STUDENTS + " VALUES " +
                "(1, 'HS001', 'Trần Thị Học', '10A1', 8.5), " +
                "(2, 'HS002', 'Lê Văn Sinh', '10A2', 7.8);";
            db.execSQL(insertStudents);

            // Insert sample users (admin, teacher, and students) with hashed passwords
            String insertUsers = "INSERT INTO " + TABLE_USERS + " (id, username, password, full_name, role, student_id, created_date) VALUES " +
                "(1, 'admin1', '" + adminPassword + "', 'Quản Trị Viên', 'Admin', NULL, datetime('now')), " +
                "(2, 'teacher1', '" + teacherPassword + "', 'Nguyễn Văn Giáo', 'Teacher', NULL, datetime('now')), " +
                "(3, 'student1', '" + student1Password + "', 'Trần Thị Học', 'Student', 1, datetime('now')), " +
                "(4, 'student2', '" + student2Password + "', 'Lê Văn Sinh', 'Student', 2, datetime('now'));";
            db.execSQL(insertUsers);

            // Insert sample subjects
            String insertSubjects = "INSERT INTO " + TABLE_SUBJECTS + " VALUES " +
                "(1, 'Toán', 'TOAN'), " +
                "(2, 'Lý', 'LY'), " +
                "(3, 'Hóa', 'HOA'), " +
                "(4, 'Văn', 'VAN');";
            db.execSQL(insertSubjects);

            // Insert sample scores
            String insertScores = "INSERT INTO " + TABLE_SCORES + " VALUES " +
                "(1, 1, 1, 'mieng', 8.5, datetime('now'), 2), " +
                "(2, 1, 1, '15phut', 7.0, datetime('now'), 2), " +
                "(3, 1, 1, '1tiet', 8.0, datetime('now'), 2), " +
                "(4, 1, 1, 'hocky', 7.5, datetime('now'), 2);";
            db.execSQL(insertScores);
            
            Log.d("DatabaseHelper", "Sample data inserted successfully");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting sample data: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
}