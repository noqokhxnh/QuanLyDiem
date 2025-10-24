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

    /**
     * Constructor để khởi tạo DatabaseManager
     * @param context Context của ứng dụng
     */
    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Mở kết nối đến cơ sở dữ liệu
     * @throws SQLException Nếu có lỗi khi mở cơ sở dữ liệu
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Đóng kết nối đến cơ sở dữ liệu
     */
    public void close() {
        dbHelper.close();
    }

    // User operations
    /**
     * Xác thực người dùng dựa trên tên đăng nhập và mật khẩu
     * @param username Tên đăng nhập của người dùng
     * @param password Mật khẩu của người dùng
     * @return Đối tượng User nếu xác thực thành công, ngược lại trả về null
     */
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

    /**
     * Lấy thông tin người dùng dựa trên ID
     * @param userId ID của người dùng cần lấy
     * @return Đối tượng User nếu tìm thấy, ngược lại trả về null
     */
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

    /**
     * Thêm người dùng mới vào cơ sở dữ liệu
     * @param user Đối tượng User chứa thông tin người dùng cần thêm
     * @return ID của người dùng vừa được thêm, hoặc -1 nếu thêm thất bại
     */
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

    /**
     * Cập nhật thông tin người dùng trong cơ sở dữ liệu
     * @param user Đối tượng User chứa thông tin người dùng cần cập nhật
     * @return Số lượng hàng bị ảnh hưởng sau khi cập nhật
     */
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
    /**
     * Lấy danh sách tất cả học sinh
     * @return Danh sách các đối tượng Student
     */
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
    
    /**
     * Lấy danh sách tất cả học sinh cùng với thông tin người dùng
     * @return Danh sách các đối tượng Student
     */
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

    /**
     * Lấy thông tin học sinh dựa trên ID
     * @param studentId ID của học sinh cần lấy
     * @return Đối tượng Student nếu tìm thấy, ngược lại trả về null
     */
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

    /**
     * Lấy danh sách học sinh dựa trên ID người dùng
     * @param userId ID của người dùng
     * @return Danh sách các đối tượng Student
     */
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
    
    /**
     * Lấy học sinh dựa trên ID người dùng (trả về một học sinh duy nhất)
     * @param userId ID của người dùng
     * @return Đối tượng Student nếu tìm thấy, ngược lại trả về null
     */
    public Student getStudentByUserId(int userId) {
        List<Student> students = getStudentsByUserId(userId);
        return students != null && !students.isEmpty() ? students.get(0) : null;
    }

    /**
     * Thêm học sinh mới vào cơ sở dữ liệu
     * @param student Đối tượng Student chứa thông tin học sinh cần thêm
     * @return ID của học sinh vừa được thêm, hoặc -1 nếu thêm thất bại
     */
    public long createStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_CODE, student.getStudentCode());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, student.getFullName());
        values.put(DatabaseHelper.COLUMN_CLASS_NAME, student.getClassName());
        values.put(DatabaseHelper.COLUMN_AVERAGE, student.getAverage());

        return database.insert(DatabaseHelper.TABLE_STUDENTS, null, values);
    }

    /**
     * Cập nhật thông tin học sinh trong cơ sở dữ liệu
     * @param student Đối tượng Student chứa thông tin học sinh cần cập nhật
     * @return Số lượng hàng bị ảnh hưởng sau khi cập nhật
     */
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

    /**
     * Xóa học sinh khỏi cơ sở dữ liệu
     * @param studentId ID của học sinh cần xóa
     * @return Số lượng hàng bị ảnh hưởng sau khi xóa
     */
    public int deleteStudent(int studentId) {
        return database.delete(DatabaseHelper.TABLE_STUDENTS, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(studentId)});
    }

    // Subject operations
    /**
     * Lấy danh sách tất cả môn học
     * @return Danh sách các đối tượng Subject
     */
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

    /**
     * Lấy thông tin môn học dựa trên ID
     * @param subjectId ID của môn học cần lấy
     * @return Đối tượng Subject nếu tìm thấy, ngược lại trả về null
     */
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

    /**
     * Thêm môn học mới vào cơ sở dữ liệu
     * @param subject Đối tượng Subject chứa thông tin môn học cần thêm
     * @return ID của môn học vừa được thêm, hoặc -1 nếu thêm thất bại
     */
    public long createSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUBJECT_NAME, subject.getSubjectName());
        values.put(DatabaseHelper.COLUMN_SUBJECT_CODE, subject.getSubjectCode());

        return database.insert(DatabaseHelper.TABLE_SUBJECTS, null, values);
    }

    /**
     * Cập nhật thông tin môn học trong cơ sở dữ liệu
     * @param subject Đối tượng Subject chứa thông tin môn học cần cập nhật
     * @return Số lượng hàng bị ảnh hưởng sau khi cập nhật
     */
    public int updateSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUBJECT_NAME, subject.getSubjectName());
        values.put(DatabaseHelper.COLUMN_SUBJECT_CODE, subject.getSubjectCode());

        return database.update(DatabaseHelper.TABLE_SUBJECTS, values, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(subject.getId())});
    }

    /**
     * Xóa môn học khỏi cơ sở dữ liệu
     * @param subjectId ID của môn học cần xóa
     * @return Số lượng hàng bị ảnh hưởng sau khi xóa
     */
    public int deleteSubject(int subjectId) {
        return database.delete(DatabaseHelper.TABLE_SUBJECTS, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(subjectId)});
    }

    // Score operations
    /**
     * Lấy danh sách tất cả điểm
     * @return Danh sách các đối tượng Score
     */
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

    /**
     * Lấy danh sách điểm của học sinh dựa trên ID học sinh
     * @param studentId ID của học sinh
     * @return Danh sách các đối tượng Score
     */
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

    /**
     * Lấy danh sách điểm của học sinh cho môn học cụ thể dựa trên ID học sinh và ID môn học
     * @param studentId ID của học sinh
     * @param subjectId ID của môn học
     * @return Danh sách các đối tượng Score
     */
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

    /**
     * Thêm điểm mới vào cơ sở dữ liệu
     * @param score Đối tượng Score chứa thông tin điểm cần thêm
     * @return ID của điểm vừa được thêm, hoặc -1 nếu thêm thất bại
     */
    public long createScore(Score score) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SCORE_STUDENT_ID, score.getStudentId());
        values.put(DatabaseHelper.COLUMN_SUBJECT_ID, score.getSubjectId());
        values.put(DatabaseHelper.COLUMN_SCORE_TYPE, score.getScoreType());
        values.put(DatabaseHelper.COLUMN_SCORE, score.getScore());
        values.put(DatabaseHelper.COLUMN_TEACHER_ID, score.getTeacherId());

        return database.insert(DatabaseHelper.TABLE_SCORES, null, values);
    }

    /**
     * Cập nhật thông tin điểm trong cơ sở dữ liệu
     * @param score Đối tượng Score chứa thông tin điểm cần cập nhật
     * @return Số lượng hàng bị ảnh hưởng sau khi cập nhật
     */
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

    /**
     * Xóa điểm khỏi cơ sở dữ liệu
     * @param scoreId ID của điểm cần xóa
     * @return Số lượng hàng bị ảnh hưởng sau khi xóa
     */
    public int deleteScore(int scoreId) {
        return database.delete(DatabaseHelper.TABLE_SCORES, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(scoreId)});
    }

    // Utility methods
    /**
     * Tính điểm trung bình của học sinh cho môn học cụ thể
     * @param studentId ID của học sinh
     * @param subjectId ID của môn học
     * @return Điểm trung bình của học sinh cho môn học cụ thể
     */
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

    /**
     * Tính điểm trung bình tổng kết của học sinh
     * @param studentId ID của học sinh
     * @return Điểm trung bình tổng kết của học sinh
     */
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