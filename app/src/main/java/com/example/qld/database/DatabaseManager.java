package com.example.qld.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    
    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    public void open() throws SQLException {
        Log.d(TAG, "Đang mở kết nối database");
        database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
        Log.d(TAG, "Đang đóng kết nối database");
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
    
    // Các thao tác với người dùng
    public User authenticateUser(String username, String password) {
        String selection = DatabaseHelper.COLUMN_USERNAME + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD)));
            user.setRole(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
            user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE)));
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return user;
    }
    
    public User getUserByUsername(String username) {
        String selection = DatabaseHelper.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD)));
            user.setRole(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
            user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE)));
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return user;
    }
    
    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_ROLE, user.getRole());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, user.getFullName());
        
        return database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD)));
                user.setRole(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE)));
                user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
                user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE)));
                
                users.add(user);
            } while (cursor.moveToNext());
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return users;
    }
    
    public User getUserById(int userId) {
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD)));
            user.setRole(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
            user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE)));
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return user;
    }
    
    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COLUMN_ROLE, user.getRole());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, user.getFullName());
        
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(user.getId())};
        
        return database.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs);
    }
    
    public int deleteUser(int userId) {
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        return database.delete(DatabaseHelper.TABLE_USERS, selection, selectionArgs);
    }
    
    // Các thao tác với học sinh
    public long addStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_USER_ID, student.getUserId());
        values.put(DatabaseHelper.COLUMN_STUDENT_CODE, student.getStudentCode());
        values.put(DatabaseHelper.COLUMN_CLASS_NAME, student.getClassName());
        values.put(DatabaseHelper.COLUMN_BIRTH_DATE, student.getBirthDate());
        
        return database.insert(DatabaseHelper.TABLE_STUDENTS, null, values);
    }
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_STUDENTS,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_ID)));
                student.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_USER_ID)));
                student.setStudentCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE)));
                student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_NAME)));
                student.setBirthDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIRTH_DATE)));
                
                students.add(student);
            } while (cursor.moveToNext());
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return students;
    }
    
    public Student getStudentById(int studentId) {
        String selection = DatabaseHelper.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_STUDENTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            student = new Student();
            student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_ID)));
            student.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_USER_ID)));
            student.setStudentCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE)));
            student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_NAME)));
            student.setBirthDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIRTH_DATE)));
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return student;
    }
    
    public Student getStudentByUserId(int userId) {
        String selection = DatabaseHelper.COLUMN_STUDENT_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_STUDENTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            student = new Student();
            student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_ID)));
            student.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_USER_ID)));
            student.setStudentCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE)));
            student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_NAME)));
            student.setBirthDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BIRTH_DATE)));
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return student;
    }
    
    public int updateStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_USER_ID, student.getUserId());
        values.put(DatabaseHelper.COLUMN_STUDENT_CODE, student.getStudentCode());
        values.put(DatabaseHelper.COLUMN_CLASS_NAME, student.getClassName());
        values.put(DatabaseHelper.COLUMN_BIRTH_DATE, student.getBirthDate());
        
        String selection = DatabaseHelper.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(student.getId())};
        
        return database.update(DatabaseHelper.TABLE_STUDENTS, values, selection, selectionArgs);
    }
    
    public int deleteStudent(int studentId) {
        String selection = DatabaseHelper.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};
        
        return database.delete(DatabaseHelper.TABLE_STUDENTS, selection, selectionArgs);
    }
    
    // Các thao tác với môn học
    public long addSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUBJECT_NAME, subject.getSubjectName());
        values.put(DatabaseHelper.COLUMN_SUBJECT_CODE, subject.getSubjectCode());
        
        return database.insert(DatabaseHelper.TABLE_SUBJECTS, null, values);
    }
    
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_SUBJECTS,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Subject subject = new Subject();
                subject.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_ID)));
                subject.setSubjectName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_NAME)));
                subject.setSubjectCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_CODE)));
                
                subjects.add(subject);
            } while (cursor.moveToNext());
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return subjects;
    }
    
    public Subject getSubjectById(int subjectId) {
        String selection = DatabaseHelper.COLUMN_SUBJECT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(subjectId)};
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_SUBJECTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        Subject subject = null;
        if (cursor != null && cursor.moveToFirst()) {
            subject = new Subject();
            subject.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_ID)));
            subject.setSubjectName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_NAME)));
            subject.setSubjectCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_CODE)));
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return subject;
    }
    
    // Các thao tác với điểm số
    public long addScore(Score score) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SCORE_STUDENT_ID, score.getStudentId());
        values.put(DatabaseHelper.COLUMN_SCORE_SUBJECT_ID, score.getSubjectId());
        values.put(DatabaseHelper.COLUMN_SCORE_TYPE, score.getScoreType());
        values.put(DatabaseHelper.COLUMN_SCORE_VALUE, score.getScore());
        values.put(DatabaseHelper.COLUMN_TEACHER_ID, score.getTeacherId());
        
        return database.insert(DatabaseHelper.TABLE_SCORES, null, values);
    }
    
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_SCORES,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_ID)));
                score.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_STUDENT_ID)));
                score.setSubjectId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_SUBJECT_ID)));
                score.setScoreType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_TYPE)));
                score.setScore(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_VALUE)));
                score.setDateCreated(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_CREATED)));
                score.setTeacherId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID)));
                
                scores.add(score);
            } while (cursor.moveToNext());
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return scores;
    }
    
    public List<Score> getScoresByStudentId(int studentId) {
        String selection = DatabaseHelper.COLUMN_SCORE_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentId)};
        
        List<Score> scores = new ArrayList<>();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_SCORES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_ID)));
                score.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_STUDENT_ID)));
                score.setSubjectId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_SUBJECT_ID)));
                score.setScoreType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_TYPE)));
                score.setScore(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_VALUE)));
                score.setDateCreated(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_CREATED)));
                score.setTeacherId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID)));
                
                scores.add(score);
            } while (cursor.moveToNext());
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return scores;
    }
    
    public Score getScoreById(int scoreId) {
        String selection = DatabaseHelper.COLUMN_SCORE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(scoreId)};
        
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_SCORES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        Score score = null;
        if (cursor != null && cursor.moveToFirst()) {
            score = new Score();
            score.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_ID)));
            score.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_STUDENT_ID)));
            score.setSubjectId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_SUBJECT_ID)));
            score.setScoreType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_TYPE)));
            score.setScore(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_VALUE)));
            score.setDateCreated(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_CREATED)));
            score.setTeacherId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID)));
            
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return score;
    }
    
    public int updateScore(Score score) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SCORE_STUDENT_ID, score.getStudentId());
        values.put(DatabaseHelper.COLUMN_SCORE_SUBJECT_ID, score.getSubjectId());
        values.put(DatabaseHelper.COLUMN_SCORE_TYPE, score.getScoreType());
        values.put(DatabaseHelper.COLUMN_SCORE_VALUE, score.getScore());
        values.put(DatabaseHelper.COLUMN_TEACHER_ID, score.getTeacherId());
        
        String selection = DatabaseHelper.COLUMN_SCORE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(score.getId())};
        
        return database.update(DatabaseHelper.TABLE_SCORES, values, selection, selectionArgs);
    }
    
    public int deleteScore(int scoreId) {
        String selection = DatabaseHelper.COLUMN_SCORE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(scoreId)};
        
        return database.delete(DatabaseHelper.TABLE_SCORES, selection, selectionArgs);
    }
    
    // Các phương thức tiện ích
    public double calculateAverageScore(int studentId) {
        String query = "SELECT AVG(" + DatabaseHelper.COLUMN_SCORE_VALUE + ") as average " +
                "FROM " + DatabaseHelper.TABLE_SCORES + " " +
                "WHERE " + DatabaseHelper.COLUMN_SCORE_STUDENT_ID + " = ?";
        
        String[] selectionArgs = {String.valueOf(studentId)};
        Cursor cursor = database.rawQuery(query, selectionArgs);
        
        double average = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            average = cursor.getDouble(cursor.getColumnIndexOrThrow("average"));
            cursor.close();
        } else if (cursor != null) {
            cursor.close();
        }
        
        return average;
    }
}