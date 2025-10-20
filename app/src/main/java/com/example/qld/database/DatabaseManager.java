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
import com.example.qld.utils.CacheManager;
import com.example.qld.utils.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // User operations
    public User authenticateUser(String username, String password) {
        User user = null;
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + 
                          " WHERE " + DatabaseHelper.COLUMN_USERNAME + " = ?";
            
            cursor = database.rawQuery(query, new String[]{username});
            
            if (cursor.moveToFirst()) {
                // Get the stored hashed password
                String storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                
                // Verify the provided password against the stored hash
                if (PasswordUtil.verifyPassword(password, storedHashedPassword)) {
                    user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                    user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)));
                    user.setPassword(storedHashedPassword); // Store the hashed password
                    user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
                    user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE)));
                    user.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_STUDENT_ID)));
                    user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE)));
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error authenticating user: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    public User getUserById(int userId) {
        // First check if user is in cache
        User cachedUser = CacheManager.getInstance().getCachedUser(userId);
        if (cachedUser != null) {
            return cachedUser;
        }

        User user = null;
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + 
                          " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";
            
            cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});
            
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD)));
                user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE)));
                user.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_STUDENT_ID)));
                user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE)));
                
                // Cache the user for future use
                CacheManager.getInstance().cacheUser(user);
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting user by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    public long createUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        // Hash the password before storing it
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        values.put(DatabaseHelper.COLUMN_PASSWORD, hashedPassword);
        values.put(DatabaseHelper.COLUMN_FULL_NAME, user.getFullName());
        values.put(DatabaseHelper.COLUMN_ROLE, user.getRole());
        values.put(DatabaseHelper.COLUMN_USER_STUDENT_ID, user.getStudentId());

        return database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, user.getUsername());
        // Hash the password before storing it (if password has changed)
        String passwordToStore;
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            passwordToStore = PasswordUtil.hashPassword(user.getPassword());
        } else {
            // If password is not updated, keep the existing password hash
            User existingUser = getUserById(user.getId());
            passwordToStore = existingUser != null ? existingUser.getPassword() : "";
        }
        values.put(DatabaseHelper.COLUMN_PASSWORD, passwordToStore);
        values.put(DatabaseHelper.COLUMN_FULL_NAME, user.getFullName());
        values.put(DatabaseHelper.COLUMN_ROLE, user.getRole());
        values.put(DatabaseHelper.COLUMN_USER_STUDENT_ID, user.getStudentId());

        int result = database.update(DatabaseHelper.TABLE_USERS, values, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(user.getId())});
        
        // If update was successful, refresh the cache
        if (result > 0) {
            CacheManager.getInstance().cacheUser(user);
        }
        
        // If update was successful, refresh the cache
        if (result > 0) {
            CacheManager.getInstance().cacheUser(user);
        }
        
        return result;
    }

    // Student operations
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_STUDENTS;
            
            cursor = database.rawQuery(query, null);
            
            if (cursor.moveToFirst()) {
                do {
                    Student student = new Student();
                    student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                    student.setStudentCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE)));
                    student.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
                    student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_NAME)));
                    student.setAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AVERAGE)));
                    
                    students.add(student);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting all students: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return students;
    }
    
    public List<Student> getAllStudentsWithUserInfo() {
        List<Student> students = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT s." + DatabaseHelper.COLUMN_ID + ", " +
                          "s." + DatabaseHelper.COLUMN_STUDENT_CODE + ", " +
                          "s." + DatabaseHelper.COLUMN_FULL_NAME + ", " +
                          "s." + DatabaseHelper.COLUMN_CLASS_NAME + ", " +
                          "s." + DatabaseHelper.COLUMN_AVERAGE + " " +
                          "FROM " + DatabaseHelper.TABLE_STUDENTS + " s ";
            
            cursor = database.rawQuery(query, null);
            
            if (cursor.moveToFirst()) {
                do {
                    Student student = new Student();
                    student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                    student.setStudentCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE)));
                    student.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
                    student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_NAME)));
                    student.setAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AVERAGE)));
                    
                    students.add(student);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting all students: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return students;
    }

    public Student getStudentById(int studentId) {
        Student student = null;
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_STUDENTS + 
                          " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";
            
            cursor = database.rawQuery(query, new String[]{String.valueOf(studentId)});
            
            if (cursor.moveToFirst()) {
                student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                student.setStudentCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE)));
                student.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
                student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_NAME)));
                student.setAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AVERAGE)));
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting student by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return student;
    }

    public List<Student> getStudentsByUserId(int userId) {
        List<Student> students = new ArrayList<>();
        Cursor cursor = null;

        try {
            // In the new structure, a user (student) is linked directly to a student record
            // so we get that specific student
            User user = getUserById(userId);
            if (user != null && user.getStudentId() > 0) {
                String query = "SELECT * FROM " + DatabaseHelper.TABLE_STUDENTS + 
                              " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";
                
                cursor = database.rawQuery(query, new String[]{String.valueOf(user.getStudentId())});
                
                if (cursor.moveToFirst()) {
                    Student student = new Student();
                    student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                    student.setStudentCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE)));
                    student.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME)));
                    student.setClassName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLASS_NAME)));
                    student.setAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AVERAGE)));
                    
                    students.add(student);
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting students by user ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return students;
    }
    
    // New method to get a specific student by user ID (returns single student)
    public Student getStudentByUserId(int userId) {
        List<Student> students = getStudentsByUserId(userId);
        return students != null && !students.isEmpty() ? students.get(0) : null;
    }

    public long createStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_CODE, student.getStudentCode());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, student.getFullName());
        values.put(DatabaseHelper.COLUMN_CLASS_NAME, student.getClassName());
        values.put(DatabaseHelper.COLUMN_AVERAGE, student.getAverage());

        return database.insert(DatabaseHelper.TABLE_STUDENTS, null, values);
    }

    public int updateStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_CODE, student.getStudentCode());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, student.getFullName());
        values.put(DatabaseHelper.COLUMN_CLASS_NAME, student.getClassName());
        values.put(DatabaseHelper.COLUMN_AVERAGE, student.getAverage());

        return database.update(DatabaseHelper.TABLE_STUDENTS, values, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(student.getId())});
    }

    public int deleteStudent(int studentId) {
        return database.delete(DatabaseHelper.TABLE_STUDENTS, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(studentId)});
    }

    // Subject operations
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_SUBJECTS;
            
            cursor = database.rawQuery(query, null);
            
            if (cursor.moveToFirst()) {
                do {
                    Subject subject = new Subject();
                    subject.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                    subject.setSubjectName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_NAME)));
                    subject.setSubjectCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_CODE)));
                    
                    subjects.add(subject);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting all subjects: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return subjects;
    }

    public Subject getSubjectById(int subjectId) {
        Subject subject = null;
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_SUBJECTS + 
                          " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";
            
            cursor = database.rawQuery(query, new String[]{String.valueOf(subjectId)});
            
            if (cursor.moveToFirst()) {
                subject = new Subject();
                subject.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                subject.setSubjectName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_NAME)));
                subject.setSubjectCode(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_CODE)));
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting subject by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return subject;
    }

    public long createSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUBJECT_NAME, subject.getSubjectName());
        values.put(DatabaseHelper.COLUMN_SUBJECT_CODE, subject.getSubjectCode());

        return database.insert(DatabaseHelper.TABLE_SUBJECTS, null, values);
    }

    public int updateSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUBJECT_NAME, subject.getSubjectName());
        values.put(DatabaseHelper.COLUMN_SUBJECT_CODE, subject.getSubjectCode());

        return database.update(DatabaseHelper.TABLE_SUBJECTS, values, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(subject.getId())});
    }

    public int deleteSubject(int subjectId) {
        return database.delete(DatabaseHelper.TABLE_SUBJECTS, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(subjectId)});
    }

    // Score operations
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_SCORES;
            
            cursor = database.rawQuery(query, null);
            
            if (cursor.moveToFirst()) {
                do {
                    Score score = new Score();
                    score.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                    score.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_STUDENT_ID)));
                    score.setSubjectId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_ID)));
                    score.setScoreType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_TYPE)));
                    score.setScore(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE)));
                    score.setDateCreated(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_CREATED)));
                    score.setTeacherId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID)));
                    
                    scores.add(score);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting all scores: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return scores;
    }

    public List<Score> getScoresByStudentId(int studentId) {
        List<Score> scores = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_SCORES + 
                          " WHERE " + DatabaseHelper.COLUMN_SCORE_STUDENT_ID + " = ?";
            
            cursor = database.rawQuery(query, new String[]{String.valueOf(studentId)});
            
            if (cursor.moveToFirst()) {
                do {
                    Score score = new Score();
                    score.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                    score.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_STUDENT_ID)));
                    score.setSubjectId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_ID)));
                    score.setScoreType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_TYPE)));
                    score.setScore(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE)));
                    score.setDateCreated(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_CREATED)));
                    score.setTeacherId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID)));
                    
                    scores.add(score);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting scores by student ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return scores;
    }

    public List<Score> getScoresByStudentIdAndSubjectId(int studentId, int subjectId) {
        List<Score> scores = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_SCORES + 
                          " WHERE " + DatabaseHelper.COLUMN_SCORE_STUDENT_ID + " = ? AND " +
                          DatabaseHelper.COLUMN_SUBJECT_ID + " = ?";
            
            cursor = database.rawQuery(query, new String[]{String.valueOf(studentId), String.valueOf(subjectId)});
            
            if (cursor.moveToFirst()) {
                do {
                    Score score = new Score();
                    score.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                    score.setStudentId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_STUDENT_ID)));
                    score.setSubjectId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SUBJECT_ID)));
                    score.setScoreType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE_TYPE)));
                    score.setScore(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE)));
                    score.setDateCreated(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_CREATED)));
                    score.setTeacherId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEACHER_ID)));
                    
                    scores.add(score);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error getting scores by student and subject ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return scores;
    }

    public long createScore(Score score) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SCORE_STUDENT_ID, score.getStudentId());
        values.put(DatabaseHelper.COLUMN_SUBJECT_ID, score.getSubjectId());
        values.put(DatabaseHelper.COLUMN_SCORE_TYPE, score.getScoreType());
        values.put(DatabaseHelper.COLUMN_SCORE, score.getScore());
        values.put(DatabaseHelper.COLUMN_TEACHER_ID, score.getTeacherId());

        return database.insert(DatabaseHelper.TABLE_SCORES, null, values);
    }

    public int updateScore(Score score) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SCORE_STUDENT_ID, score.getStudentId());
        values.put(DatabaseHelper.COLUMN_SUBJECT_ID, score.getSubjectId());
        values.put(DatabaseHelper.COLUMN_SCORE_TYPE, score.getScoreType());
        values.put(DatabaseHelper.COLUMN_SCORE, score.getScore());
        values.put(DatabaseHelper.COLUMN_TEACHER_ID, score.getTeacherId());

        return database.update(DatabaseHelper.TABLE_SCORES, values, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(score.getId())});
    }

    public int deleteScore(int scoreId) {
        return database.delete(DatabaseHelper.TABLE_SCORES, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(scoreId)});
    }

    // Utility methods
    public double calculateAverageScore(int studentId, int subjectId) {
        double average = 0.0;
        Cursor cursor = null;

        try {
            String query = "SELECT AVG(" + DatabaseHelper.COLUMN_SCORE + ") as avg_score " +
                          "FROM " + DatabaseHelper.TABLE_SCORES + 
                          " WHERE " + DatabaseHelper.COLUMN_SCORE_STUDENT_ID + " = ? AND " +
                          DatabaseHelper.COLUMN_SUBJECT_ID + " = ?";
            
            cursor = database.rawQuery(query, new String[]{String.valueOf(studentId), String.valueOf(subjectId)});
            
            if (cursor.moveToFirst()) {
                average = cursor.getDouble(cursor.getColumnIndexOrThrow("avg_score"));
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error calculating average score: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return average;
    }

    public double calculateOverallAverageScore(int studentId) {
        double average = 0.0;
        Cursor cursor = null;

        try {
            String query = "SELECT AVG(" + DatabaseHelper.COLUMN_SCORE + ") as avg_score " +
                          "FROM " + DatabaseHelper.TABLE_SCORES + 
                          " WHERE " + DatabaseHelper.COLUMN_SCORE_STUDENT_ID + " = ?";
            
            cursor = database.rawQuery(query, new String[]{String.valueOf(studentId)});
            
            if (cursor.moveToFirst()) {
                average = cursor.getDouble(cursor.getColumnIndexOrThrow("avg_score"));
            }
        } catch (Exception e) {
            Log.e("DatabaseManager", "Error calculating overall average score: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return average;
    }
}