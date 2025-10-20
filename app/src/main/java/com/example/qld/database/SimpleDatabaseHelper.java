package com.example.qld.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SimpleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SimpleStudentManager.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_USERS = "users";

    // Common column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_ROLE = "role"; // 'ADMIN', 'TEACHER'

    // Create Users table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL,"
            + COLUMN_PASSWORD + " TEXT NOT NULL,"
            + COLUMN_FULL_NAME + " TEXT NOT NULL,"
            + COLUMN_ROLE + " TEXT CHECK(" + COLUMN_ROLE + " IN ('ADMIN', 'TEACHER')) NOT NULL DEFAULT 'TEACHER'"
            + ")";

    public SimpleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USERS);
            
            // Insert sample data
            insertSampleData(db);
            
            Log.d("SimpleDatabaseHelper", "Database tables created successfully");
        } catch (Exception e) {
            Log.e("SimpleDatabaseHelper", "Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        
        // Create tables again
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        try {
            // Insert sample users with plain text passwords for simplicity
            String insertUsers = "INSERT INTO " + TABLE_USERS + " VALUES " +
                "(1, 'admin1', '123456', 'Nguyễn Văn Quản Trị', 'ADMIN'), " +
                "(2, 'teacher1', '123456', 'Nguyễn Văn Giáo', 'TEACHER');";
            db.execSQL(insertUsers);
            
            Log.d("SimpleDatabaseHelper", "Sample data inserted successfully");
        } catch (Exception e) {
            Log.e("SimpleDatabaseHelper", "Error inserting sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}