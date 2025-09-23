package com.example.studentscoremanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentScoreDB";
    private static final int DATABASE_VERSION = 5;

    // Bảng Users
    public static final String TABLE_USERS = "Users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_EMAIL = "email";

    // Bảng Students
    public static final String TABLE_STUDENTS = "Students";
    public static final String COL_STUDENT_ID = "student_id";
    public static final String COL_FULL_NAME = "full_name";
    public static final String COL_CLASS = "class_name";
    public static final String COL_FACULTY = "faculty";
    public static final String COL_PHONE = "phone";
    public static final String COL_STUDENT_EMAIL = "student_email";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_HOMETOWN = "hometown";
    public static final String COL_BIRTH_YEAR = "birth_year";

    // Bảng Scores
    public static final String TABLE_SCORES = "Scores";
    public static final String COL_SCORE_ID = "score_id";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_MIDTERM_SCORE = "midterm_score";
    public static final String COL_FINAL_SCORE = "final_score";
    public static final String COL_AVERAGE_SCORE = "average_score";
    public static final String COL_SEMESTER = "semester";
    public static final String COL_YEAR = "year";

    // Bảng Schedules (Lịch học)
    public static final String TABLE_SCHEDULES = "Schedules";
    public static final String COL_SCHEDULE_ID = "schedule_id";
    public static final String COL_SCHEDULE_TITLE = "title";
    public static final String COL_SCHEDULE_DESCRIPTION = "description";
    public static final String COL_SCHEDULE_DATE = "schedule_date";
    public static final String COL_SCHEDULE_TIME = "schedule_time";
    public static final String COL_SCHEDULE_LOCATION = "location";
    public static final String COL_SCHEDULE_TYPE = "type"; // "exam", "assignment", "event"

    // Bảng Notifications (Thông báo)
    public static final String TABLE_NOTIFICATIONS = "Notifications";
    public static final String COL_NOTIFICATION_ID = "notification_id";
    public static final String COL_NOTIFICATION_TITLE = "title";
    public static final String COL_NOTIFICATION_CONTENT = "content";
    public static final String COL_NOTIFICATION_DATE = "created_date";
    public static final String COL_NOTIFICATION_PRIORITY = "priority"; // "high", "medium", "low"
    public static final String COL_NOTIFICATION_IS_READ = "is_read";

    // Bảng Timetables (Thời khóa biểu)
    public static final String TABLE_TIMETABLES = "Timetables";
    public static final String COL_TIMETABLE_ID = "timetable_id";
    public static final String COL_TIMETABLE_SUBJECT = "subject";
    public static final String COL_TIMETABLE_DAY = "day_of_week";
    public static final String COL_TIMETABLE_START_TIME = "start_time";
    public static final String COL_TIMETABLE_END_TIME = "end_time";
    public static final String COL_TIMETABLE_ROOM = "room";
    public static final String COL_TIMETABLE_TEACHER = "teacher";
    public static final String COL_TIMETABLE_CLASS = "class_name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "onCreate called - creating tables");
        
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USERNAME + " TEXT UNIQUE,"
                + COL_PASSWORD + " TEXT,"
                + COL_EMAIL + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);
        Log.d("DatabaseHelper", "Users table created");

        String CREATE_STUDENTS_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + COL_STUDENT_ID + " TEXT PRIMARY KEY,"
                + COL_FULL_NAME + " TEXT NOT NULL,"
                + COL_CLASS + " TEXT,"
                + COL_FACULTY + " TEXT,"
                + COL_PHONE + " TEXT,"
                + COL_STUDENT_EMAIL + " TEXT,"
                + COL_AVATAR + " TEXT,"
                + COL_HOMETOWN + " TEXT,"
                + COL_BIRTH_YEAR + " INTEGER)";
        db.execSQL(CREATE_STUDENTS_TABLE);
        Log.d("DatabaseHelper", "Students table created");

        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + COL_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_STUDENT_ID + " TEXT,"
                + COL_SUBJECT + " TEXT NOT NULL,"
                + COL_MIDTERM_SCORE + " REAL,"
                + COL_FINAL_SCORE + " REAL,"
                + COL_AVERAGE_SCORE + " REAL,"
                + COL_SEMESTER + " TEXT,"
                + COL_YEAR + " TEXT,"
                + "FOREIGN KEY(" + COL_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COL_STUDENT_ID + "))";
        db.execSQL(CREATE_SCORES_TABLE);
        Log.d("DatabaseHelper", "Scores table created");

        // Tạo bảng Schedules
        String CREATE_SCHEDULES_TABLE = "CREATE TABLE " + TABLE_SCHEDULES + "("
                + COL_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_SCHEDULE_TITLE + " TEXT NOT NULL,"
                + COL_SCHEDULE_DESCRIPTION + " TEXT,"
                + COL_SCHEDULE_DATE + " TEXT NOT NULL,"
                + COL_SCHEDULE_TIME + " TEXT,"
                + COL_SCHEDULE_LOCATION + " TEXT,"
                + COL_SCHEDULE_TYPE + " TEXT DEFAULT 'event')";
        db.execSQL(CREATE_SCHEDULES_TABLE);
        Log.d("DatabaseHelper", "Schedules table created");

        // Tạo bảng Notifications
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                + COL_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NOTIFICATION_TITLE + " TEXT NOT NULL,"
                + COL_NOTIFICATION_CONTENT + " TEXT,"
                + COL_NOTIFICATION_DATE + " TEXT NOT NULL,"
                + COL_NOTIFICATION_PRIORITY + " TEXT DEFAULT 'medium',"
                + COL_NOTIFICATION_IS_READ + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);
        Log.d("DatabaseHelper", "Notifications table created");

        // Tạo bảng Timetables
        String CREATE_TIMETABLES_TABLE = "CREATE TABLE " + TABLE_TIMETABLES + "("
                + COL_TIMETABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TIMETABLE_SUBJECT + " TEXT NOT NULL,"
                + COL_TIMETABLE_DAY + " TEXT NOT NULL,"
                + COL_TIMETABLE_START_TIME + " TEXT NOT NULL,"
                + COL_TIMETABLE_END_TIME + " TEXT NOT NULL,"
                + COL_TIMETABLE_ROOM + " TEXT,"
                + COL_TIMETABLE_TEACHER + " TEXT,"
                + COL_TIMETABLE_CLASS + " TEXT)";
        db.execSQL(CREATE_TIMETABLES_TABLE);
        Log.d("DatabaseHelper", "Timetables table created");

        // Thêm dữ liệu mẫu
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            // Thêm cột mới cho bảng Students
            db.execSQL("ALTER TABLE " + TABLE_STUDENTS + " ADD COLUMN " + COL_HOMETOWN + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_STUDENTS + " ADD COLUMN " + COL_BIRTH_YEAR + " INTEGER");
        }
        if (oldVersion < 4) {
            // Tạo bảng mới nếu chưa tồn tại
            String CREATE_SCHEDULES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCHEDULES + "("
                    + COL_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_SCHEDULE_TITLE + " TEXT NOT NULL,"
                    + COL_SCHEDULE_DESCRIPTION + " TEXT,"
                    + COL_SCHEDULE_DATE + " TEXT NOT NULL,"
                    + COL_SCHEDULE_TIME + " TEXT,"
                    + COL_SCHEDULE_LOCATION + " TEXT,"
                    + COL_SCHEDULE_TYPE + " TEXT DEFAULT 'event')";
            db.execSQL(CREATE_SCHEDULES_TABLE);

            String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATIONS + "("
                    + COL_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_NOTIFICATION_TITLE + " TEXT NOT NULL,"
                    + COL_NOTIFICATION_CONTENT + " TEXT,"
                    + COL_NOTIFICATION_DATE + " TEXT NOT NULL,"
                    + COL_NOTIFICATION_PRIORITY + " TEXT DEFAULT 'medium',"
                    + COL_NOTIFICATION_IS_READ + " INTEGER DEFAULT 0)";
            db.execSQL(CREATE_NOTIFICATIONS_TABLE);

            String CREATE_TIMETABLES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TIMETABLES + "("
                    + COL_TIMETABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_TIMETABLE_SUBJECT + " TEXT NOT NULL,"
                    + COL_TIMETABLE_DAY + " TEXT NOT NULL,"
                    + COL_TIMETABLE_START_TIME + " TEXT NOT NULL,"
                    + COL_TIMETABLE_END_TIME + " TEXT NOT NULL,"
                    + COL_TIMETABLE_ROOM + " TEXT,"
                    + COL_TIMETABLE_TEACHER + " TEXT,"
                    + COL_TIMETABLE_CLASS + " TEXT)";
            db.execSQL(CREATE_TIMETABLES_TABLE);

            // Thêm dữ liệu mẫu cho bảng mới
            insertSampleDataForNewTables(db);
        }
    }

    // Thêm dữ liệu mẫu cho bảng mới
    private void insertSampleDataForNewTables(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Inserting sample data for new tables");
        
        // Thêm lịch học mẫu
        String[] scheduleTitles = {"Thi giữa kỳ Java", "Nộp bài tập Cơ sở dữ liệu", "Họp lớp"};
        String[] scheduleDescriptions = {"Thi lý thuyết và thực hành", "Nộp bài tập lớn", "Họp lớp định kỳ"};
        String[] scheduleDates = {"2024-10-15", "2024-10-20", "2024-10-25"};
        String[] scheduleTimes = {"08:00", "14:00", "16:00"};
        String[] scheduleLocations = {"Phòng A101", "Phòng B202", "Phòng họp"};
        String[] scheduleTypes = {"exam", "assignment", "event"};
        
        for (int i = 0; i < scheduleTitles.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COL_SCHEDULE_TITLE, scheduleTitles[i]);
            values.put(COL_SCHEDULE_DESCRIPTION, scheduleDescriptions[i]);
            values.put(COL_SCHEDULE_DATE, scheduleDates[i]);
            values.put(COL_SCHEDULE_TIME, scheduleTimes[i]);
            values.put(COL_SCHEDULE_LOCATION, scheduleLocations[i]);
            values.put(COL_SCHEDULE_TYPE, scheduleTypes[i]);
            db.insert(TABLE_SCHEDULES, null, values);
        }

        // Thêm thông báo mẫu
        String[] notificationTitles = {"Thông báo thi cuối kỳ", "Lịch nghỉ lễ", "Cập nhật quy chế"};
        String[] notificationContents = {"Lịch thi cuối kỳ đã được cập nhật", "Nghỉ lễ 2/9", "Quy chế học tập mới"};
        String[] notificationDates = {"2024-10-01", "2024-09-01", "2024-08-15"};
        String[] priorities = {"high", "medium", "low"};
        
        for (int i = 0; i < notificationTitles.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COL_NOTIFICATION_TITLE, notificationTitles[i]);
            values.put(COL_NOTIFICATION_CONTENT, notificationContents[i]);
            values.put(COL_NOTIFICATION_DATE, notificationDates[i]);
            values.put(COL_NOTIFICATION_PRIORITY, priorities[i]);
            values.put(COL_NOTIFICATION_IS_READ, 0);
            db.insert(TABLE_NOTIFICATIONS, null, values);
        }

        // Thêm thời khóa biểu mẫu
        String[] subjects = {"Lập trình Java", "Cơ sở dữ liệu", "Mạng máy tính"};
        String[] days = {"Thứ 2", "Thứ 3", "Thứ 4"};
        String[] startTimes = {"08:00", "10:00", "14:00"};
        String[] endTimes = {"10:00", "12:00", "16:00"};
        String[] rooms = {"A101", "A102", "A103"};
        String[] teachers = {"Thầy Nam", "Cô Hoa", "Thầy Minh"};
        String[] classes = {"D20CQCN01", "D20CQCN01", "D20CQCN01"};
        
        for (int i = 0; i < subjects.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COL_TIMETABLE_SUBJECT, subjects[i]);
            values.put(COL_TIMETABLE_DAY, days[i]);
            values.put(COL_TIMETABLE_START_TIME, startTimes[i]);
            values.put(COL_TIMETABLE_END_TIME, endTimes[i]);
            values.put(COL_TIMETABLE_ROOM, rooms[i]);
            values.put(COL_TIMETABLE_TEACHER, teachers[i]);
            values.put(COL_TIMETABLE_CLASS, classes[i]);
            db.insert(TABLE_TIMETABLES, null, values);
        }
    }

    // Thêm user
    public boolean addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_EMAIL, email);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Kiểm tra user
    public boolean checkUser(String username, String password) {
        Log.d("DatabaseHelper", "Checking user: " + username);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        Log.d("DatabaseHelper", "User check result: " + exists + " (count: " + cursor.getCount() + ")");
        cursor.close();
        db.close();
        return exists;
    }
    // Lấy mật khẩu bằng email
    public String getPasswordByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            String password = cursor.getString(0);
            cursor.close();
            db.close();
            return password;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    // Thêm dữ liệu mẫu
    private void insertSampleData(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Inserting sample data");
        
        // Thêm user mẫu để test đăng nhập
        ContentValues userValues = new ContentValues();
        userValues.put(COL_USERNAME, "admin");
        userValues.put(COL_PASSWORD, "123");
        userValues.put(COL_EMAIL, "admin@test.com");
        long userResult = db.insert(TABLE_USERS, null, userValues);
        Log.d("DatabaseHelper", "Admin user inserted with result: " + userResult);

        // Thêm sinh viên mẫu
        ContentValues studentValues = new ContentValues();
        studentValues.put(COL_STUDENT_ID, "20201234");
        studentValues.put(COL_FULL_NAME, "Nguyễn Văn A");
        studentValues.put(COL_CLASS, "D20CQCN01");
        studentValues.put(COL_FACULTY, "Công nghệ thông tin");
        studentValues.put(COL_PHONE, "0123456789");
        studentValues.put(COL_STUDENT_EMAIL, "nguyenvana@university.edu");
        studentValues.put(COL_AVATAR, "");
        studentValues.put(COL_HOMETOWN, "Hà Nội");
        studentValues.put(COL_BIRTH_YEAR, 2002);
        db.insert(TABLE_STUDENTS, null, studentValues);

        // Thêm điểm số mẫu
        String[] subjects = {"Lập trình Java", "Cơ sở dữ liệu", "Mạng máy tính", "Phân tích thiết kế hệ thống"};
        double[][] scores = {{8.5, 9.0}, {7.5, 8.0}, {8.0, 8.5}, {9.0, 8.5}};
        
        for (int i = 0; i < subjects.length; i++) {
            ContentValues scoreValues = new ContentValues();
            scoreValues.put(COL_STUDENT_ID, "20201234");
            scoreValues.put(COL_SUBJECT, subjects[i]);
            scoreValues.put(COL_MIDTERM_SCORE, scores[i][0]);
            scoreValues.put(COL_FINAL_SCORE, scores[i][1]);
            scoreValues.put(COL_AVERAGE_SCORE, (scores[i][0] + scores[i][1]) / 2);
            scoreValues.put(COL_SEMESTER, "HK1");
            scoreValues.put(COL_YEAR, "2024-2025");
            db.insert(TABLE_SCORES, null, scoreValues);
        }
    }

    // Lấy thông tin sinh viên theo ID
    public Cursor getStudentById(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_STUDENTS, null, COL_STUDENT_ID + "=?", 
                new String[]{studentId}, null, null, null);
    }

    // Lấy danh sách điểm số của sinh viên
    public Cursor getScoresByStudentId(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_SCORES, null, COL_STUDENT_ID + "=?", 
                new String[]{studentId}, null, null, COL_SUBJECT + " ASC");
    }

    // Lấy điểm theo bộ lọc môn/semester/year
    public Cursor getScoresByFilters(String studentId, String subjectQuery, String semester, String year) {
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder selection = new StringBuilder(COL_STUDENT_ID + "=?");
        java.util.List<String> args = new java.util.ArrayList<>();
        args.add(studentId);

        if (subjectQuery != null && !subjectQuery.trim().isEmpty()) {
            selection.append(" AND ").append(COL_SUBJECT).append(" LIKE ?");
            args.add("%" + subjectQuery.trim() + "%");
        }
        if (semester != null && !semester.equals("Tất cả") && !semester.trim().isEmpty()) {
            selection.append(" AND ").append(COL_SEMESTER).append("=?");
            args.add(semester);
        }
        if (year != null && !year.equals("Tất cả") && !year.trim().isEmpty()) {
            selection.append(" AND ").append(COL_YEAR).append("=?");
            args.add(year);
        }

        return db.query(TABLE_SCORES, null, selection.toString(), args.toArray(new String[0]), null, null, COL_SUBJECT + " ASC");
    }

    // Lấy tất cả sinh viên
    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_STUDENTS, null, null, null, null, null, COL_FULL_NAME + " ASC");
    }

    // Xóa sinh viên và toàn bộ điểm của sinh viên đó
    public boolean deleteStudentAndScores(String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedScores = db.delete(TABLE_SCORES, COL_STUDENT_ID + "=?", new String[]{studentId});
        int deletedStudents = db.delete(TABLE_STUDENTS, COL_STUDENT_ID + "=?", new String[]{studentId});
        db.close();
        return deletedStudents > 0; // điểm có thể = 0 nếu chưa có
    }

    // Thêm sinh viên mới
    public boolean addStudent(String studentId, String fullName, String className, 
                             String faculty, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_ID, studentId);
        values.put(COL_FULL_NAME, fullName);
        values.put(COL_CLASS, className);
        values.put(COL_FACULTY, faculty);
        values.put(COL_PHONE, phone);
        values.put(COL_STUDENT_EMAIL, email);
        values.put(COL_AVATAR, "");

        long result = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return result != -1;
    }

    // Thêm điểm số mới
    public boolean addScore(String studentId, String subject, double midtermScore, 
                           double finalScore, String semester, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_ID, studentId);
        values.put(COL_SUBJECT, subject);
        values.put(COL_MIDTERM_SCORE, midtermScore);
        values.put(COL_FINAL_SCORE, finalScore);
        values.put(COL_AVERAGE_SCORE, (midtermScore + finalScore) / 2);
        values.put(COL_SEMESTER, semester);
        values.put(COL_YEAR, year);

        long result = db.insert(TABLE_SCORES, null, values);
        db.close();
        return result != -1;
    }

    // Cập nhật thông tin sinh viên
    public boolean updateStudentInfo(String studentId, String fullName, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FULL_NAME, fullName);
        values.put(COL_PHONE, phone);
        values.put(COL_STUDENT_EMAIL, email);

        int result = db.update(TABLE_STUDENTS, values, COL_STUDENT_ID + "=?", new String[]{studentId});
        db.close();
        return result > 0;
    }

    // Cập nhật thông tin sinh viên đầy đủ
    public boolean updateStudentProfile(String studentId, String fullName, String phone, String email, String hometown, int birthYear) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FULL_NAME, fullName);
        values.put(COL_PHONE, phone);
        values.put(COL_STUDENT_EMAIL, email);
        values.put(COL_HOMETOWN, hometown);
        values.put(COL_BIRTH_YEAR, birthYear);

        int result = db.update(TABLE_STUDENTS, values, COL_STUDENT_ID + "=?", new String[]{studentId});
        db.close();
        return result > 0;
    }

    // Cập nhật mật khẩu
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, newPassword);

        int result = db.update(TABLE_USERS, values, COL_USERNAME + "=?", new String[]{username});
        db.close();
        return result > 0;
    }

    // ========== PHƯƠNG THỨC CHO BẢNG SCHEDULES ==========
    
    // Thêm lịch học mới
    public boolean addSchedule(String title, String description, String date, String time, String location, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SCHEDULE_TITLE, title);
        values.put(COL_SCHEDULE_DESCRIPTION, description);
        values.put(COL_SCHEDULE_DATE, date);
        values.put(COL_SCHEDULE_TIME, time);
        values.put(COL_SCHEDULE_LOCATION, location);
        values.put(COL_SCHEDULE_TYPE, type);

        long result = db.insert(TABLE_SCHEDULES, null, values);
        db.close();
        return result != -1;
    }

    // Lấy tất cả lịch học
    public Cursor getAllSchedules() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_SCHEDULES, null, null, null, null, null, COL_SCHEDULE_DATE + " ASC");
    }

    // Xóa lịch học
    public boolean deleteSchedule(int scheduleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SCHEDULES, COL_SCHEDULE_ID + "=?", new String[]{String.valueOf(scheduleId)});
        db.close();
        return result > 0;
    }

    // ========== PHƯƠNG THỨC CHO BẢNG NOTIFICATIONS ==========
    
    // Thêm thông báo mới
    public boolean addNotification(String title, String content, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTIFICATION_TITLE, title);
        values.put(COL_NOTIFICATION_CONTENT, content);
        values.put(COL_NOTIFICATION_DATE, java.text.SimpleDateFormat.getDateTimeInstance().format(new java.util.Date()));
        values.put(COL_NOTIFICATION_PRIORITY, priority);
        values.put(COL_NOTIFICATION_IS_READ, 0);

        long result = db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
        return result != -1;
    }

    // Lấy tất cả thông báo
    public Cursor getAllNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NOTIFICATIONS, null, null, null, null, null, COL_NOTIFICATION_DATE + " DESC");
    }

    // Đánh dấu thông báo đã đọc
    public boolean markNotificationAsRead(int notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOTIFICATION_IS_READ, 1);

        int result = db.update(TABLE_NOTIFICATIONS, values, COL_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notificationId)});
        db.close();
        return result > 0;
    }

    // Xóa thông báo
    public boolean deleteNotification(int notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NOTIFICATIONS, COL_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notificationId)});
        db.close();
        return result > 0;
    }

    // ========== PHƯƠNG THỨC CHO BẢNG TIMETABLES ==========
    
    // Thêm thời khóa biểu mới
    public boolean addTimetable(String subject, String day, String startTime, String endTime, String room, String teacher, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TIMETABLE_SUBJECT, subject);
        values.put(COL_TIMETABLE_DAY, day);
        values.put(COL_TIMETABLE_START_TIME, startTime);
        values.put(COL_TIMETABLE_END_TIME, endTime);
        values.put(COL_TIMETABLE_ROOM, room);
        values.put(COL_TIMETABLE_TEACHER, teacher);
        values.put(COL_TIMETABLE_CLASS, className);

        long result = db.insert(TABLE_TIMETABLES, null, values);
        db.close();
        return result != -1;
    }

    // Lấy tất cả thời khóa biểu
    public Cursor getAllTimetables() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TIMETABLES, null, null, null, null, null, COL_TIMETABLE_DAY + " ASC, " + COL_TIMETABLE_START_TIME + " ASC");
    }

    // Lấy thời khóa biểu theo lớp
    public Cursor getTimetablesByClass(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TIMETABLES, null, COL_TIMETABLE_CLASS + "=?", new String[]{className}, null, null, COL_TIMETABLE_DAY + " ASC, " + COL_TIMETABLE_START_TIME + " ASC");
    }

    // Xóa thời khóa biểu
    public boolean deleteTimetable(int timetableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_TIMETABLES, COL_TIMETABLE_ID + "=?", new String[]{String.valueOf(timetableId)});
        db.close();
        return result > 0;
    }

}
