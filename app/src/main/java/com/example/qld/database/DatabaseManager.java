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
     * @param context Context của ứng dụng, dùng để truy cập vào tài nguyên và thiết lập kết nối cơ sở dữ liệu
     */
    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Mở kết nối đến cơ sở dữ liệu
     * Mở cơ sở dữ liệu ở chế độ có thể ghi (writable) để thực hiện các thao tác thêm, sửa, xóa
     * @throws SQLException Nếu có lỗi khi mở cơ sở dữ liệu, ví dụ như không đủ quyền truy cập hoặc cơ sở dữ liệu bị khóa
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Đóng kết nối đến cơ sở dữ liệu
     * Giải phóng tài nguyên và đóng kết nối để tránh rò rỉ tài nguyên
     */
    public void close() {
        dbHelper.close();
    }

    // User operations
    /**
     * Xác thực người dùng dựa trên tên đăng nhập và mật khẩu
     * So sánh thông tin đăng nhập với dữ liệu trong cơ sở dữ liệu
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn cơ sở dữ liệu để tìm người dùng theo tên đăng nhập
     * 2. Lấy mật khẩu đã được mã hóa từ cơ sở dữ liệu
     * 3. Dùng hàm verifyPassword để so sánh mật khẩu đã mã hóa với mật khẩu do người dùng cung cấp
     * 4. Nếu khớp, trả về đối tượng User; nếu không, trả về null
     * 
     * @param username Tên đăng nhập của người dùng cần xác thực (không được null hoặc rỗng)
     * @param password Mật khẩu của người dùng cần xác thực (trước khi mã hóa, không được null)
     * @return Đối tượng User nếu xác thực thành công, ngược lại trả về null
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * Trước tiên kiểm tra trong cache, sau đó truy vấn cơ sở dữ liệu nếu không tìm thấy
     * 
     * Cách thức hoạt động:
     * 1. Kiểm tra xem người dùng có trong cache hay không
     * 2. Nếu có, trả về người dùng từ cache
     * 3. Nếu không có trong cache, thực hiện truy vấn cơ sở dữ liệu
     * 4. Lưu người dùng vào cache để sử dụng sau này
     * 
     * @param userId ID của người dùng cần lấy (phải là số nguyên dương)
     * @return Đối tượng User nếu tìm thấy, ngược lại trả về null
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * Trước khi lưu, mật khẩu sẽ được mã hóa để bảo mật
     * 
     * Cách thức hoạt động:
     * 1. Chuẩn bị dữ liệu người dùng vào ContentValues để chèn vào cơ sở dữ liệu
     * 2. Mã hóa mật khẩu trước khi lưu (dùng PasswordUtil.hashPassword)
     * 3. Chèn dữ liệu vào bảng users
     * 4. Trả về ID của người dùng vừa được thêm
     * 
     * @param user Đối tượng User chứa thông tin người dùng cần thêm (không được null)
     * @return ID của người dùng vừa được thêm (số nguyên dương), hoặc -1 nếu thêm thất bại
     * @throws Exception Nếu có lỗi xảy ra trong quá trình chèn cơ sở dữ liệu
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
     * Nếu mật khẩu thay đổi, sẽ được mã hóa lại; nếu không thay đổi, giữ nguyên mật khẩu cũ
     * 
     * Cách thức hoạt động:
     * 1. Nếu mật khẩu mới được cung cấp, mã hóa mật khẩu đó
     * 2. Nếu mật khẩu không được cập nhật, lấy mật khẩu cũ từ cơ sở dữ liệu
     * 3. Chuẩn bị dữ liệu cập nhật vào ContentValues
     * 4. Cập nhật bản ghi trong bảng users dựa trên ID người dùng
     * 5. Nếu cập nhật thành công, cập nhật lại cache
     * 
     * @param user Đối tượng User chứa thông tin người dùng cần cập nhật (không được null, phải có ID hợp lệ)
     * @return Số lượng hàng bị ảnh hưởng sau khi cập nhật (thường là 1 nếu thành công, 0 nếu thất bại)
     * @throws Exception Nếu có lỗi xảy ra trong quá trình cập nhật cơ sở dữ liệu
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
     * Lấy danh sách tất cả học sinh từ cơ sở dữ liệu
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn toàn bộ bản ghi trong bảng students
     * 2. Duyệt qua từng dòng kết quả trong Cursor
     * 3. Tạo đối tượng Student từ dữ liệu trong mỗi dòng
     * 4. Thêm đối tượng Student vào danh sách kết quả
     * 
     * @return Danh sách các đối tượng Student, danh sách rỗng nếu không có học sinh nào
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn các thông tin học sinh trong bảng students
     * 2. Duyệt qua từng dòng kết quả trong Cursor
     * 3. Tạo đối tượng Student từ dữ liệu trong mỗi dòng
     * 4. Thêm đối tượng Student vào danh sách kết quả
     * 
     * @return Danh sách các đối tượng Student, danh sách rỗng nếu không có học sinh nào
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn học sinh trong bảng students với điều kiện ID
     * 2. Nếu tìm thấy, tạo đối tượng Student từ dữ liệu trong kết quả
     * 3. Nếu không tìm thấy, trả về null
     * 
     * @param studentId ID của học sinh cần lấy (phải là số nguyên dương)
     * @return Đối tượng Student nếu tìm thấy, ngược lại trả về null
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Lấy thông tin người dùng từ ID người dùng
     * 2. Nếu người dùng có ID học sinh liên kết, sử dụng ID đó để tìm học sinh
     * 3. Truy vấn học sinh trong bảng students với điều kiện ID học sinh
     * 4. Trả về danh sách học sinh tìm được (thường chỉ có một)
     * 
     * @param userId ID của người dùng (phải là số nguyên dương)
     * @return Danh sách các đối tượng Student liên kết với người dùng, danh sách rỗng nếu không có
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Gọi phương thức getStudentsByUserId để lấy danh sách học sinh liên kết với người dùng
     * 2. Nếu danh sách không rỗng, trả về học sinh đầu tiên
     * 3. Nếu danh sách rỗng, trả về null
     * 
     * @param userId ID của người dùng (phải là số nguyên dương)
     * @return Đối tượng Student nếu tìm thấy, ngược lại trả về null
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
     */
    public Student getStudentByUserId(int userId) {
        List<Student> students = getStudentsByUserId(userId);
        return students != null && !students.isEmpty() ? students.get(0) : null;
    }

    /**
     * Thêm học sinh mới vào cơ sở dữ liệu
     * 
     * Cách thức hoạt động:
     * 1. Chuẩn bị dữ liệu học sinh vào ContentValues để chèn vào cơ sở dữ liệu
     * 2. Chèn dữ liệu vào bảng students
     * 3. Trả về ID của học sinh vừa được thêm
     * 
     * @param student Đối tượng Student chứa thông tin học sinh cần thêm (không được null)
     * @return ID của học sinh vừa được thêm (số nguyên dương), hoặc -1 nếu thêm thất bại
     * @throws Exception Nếu có lỗi xảy ra trong quá trình chèn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Chuẩn bị dữ liệu cập nhật vào ContentValues
     * 2. Cập nhật bản ghi trong bảng students dựa trên ID học sinh
     * 3. Trả về số lượng hàng bị ảnh hưởng
     * 
     * @param student Đối tượng Student chứa thông tin học sinh cần cập nhật (không được null, phải có ID hợp lệ)
     * @return Số lượng hàng bị ảnh hưởng sau khi cập nhật (thường là 1 nếu thành công, 0 nếu thất bại)
     * @throws Exception Nếu có lỗi xảy ra trong quá trình cập nhật cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Xóa bản ghi trong bảng students dựa trên ID học sinh
     * 2. Trả về số lượng hàng bị ảnh hưởng
     * 
     * @param studentId ID của học sinh cần xóa (phải là số nguyên dương)
     * @return Số lượng hàng bị ảnh hưởng sau khi xóa (thường là 1 nếu thành công, 0 nếu không tìm thấy)
     * @throws Exception Nếu có lỗi xảy ra trong quá trình xóa cơ sở dữ liệu
     */
    public int deleteStudent(int studentId) {
        return database.delete(DatabaseHelper.TABLE_STUDENTS, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(studentId)});
    }

    // Subject operations
    /**
     * Lấy danh sách tất cả môn học từ cơ sở dữ liệu
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn toàn bộ bản ghi trong bảng subjects
     * 2. Duyệt qua từng dòng kết quả trong Cursor
     * 3. Tạo đối tượng Subject từ dữ liệu trong mỗi dòng
     * 4. Thêm đối tượng Subject vào danh sách kết quả
     * 
     * @return Danh sách các đối tượng Subject, danh sách rỗng nếu không có môn học nào
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn môn học trong bảng subjects với điều kiện ID
     * 2. Nếu tìm thấy, tạo đối tượng Subject từ dữ liệu trong kết quả
     * 3. Nếu không tìm thấy, trả về null
     * 
     * @param subjectId ID của môn học cần lấy (phải là số nguyên dương)
     * @return Đối tượng Subject nếu tìm thấy, ngược lại trả về null
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Chuẩn bị dữ liệu môn học vào ContentValues để chèn vào cơ sở dữ liệu
     * 2. Chèn dữ liệu vào bảng subjects
     * 3. Trả về ID của môn học vừa được thêm
     * 
     * @param subject Đối tượng Subject chứa thông tin môn học cần thêm (không được null)
     * @return ID của môn học vừa được thêm (số nguyên dương), hoặc -1 nếu thêm thất bại
     * @throws Exception Nếu có lỗi xảy ra trong quá trình chèn cơ sở dữ liệu
     */
    public long createSubject(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SUBJECT_NAME, subject.getSubjectName());
        values.put(DatabaseHelper.COLUMN_SUBJECT_CODE, subject.getSubjectCode());

        return database.insert(DatabaseHelper.TABLE_SUBJECTS, null, values);
    }

    /**
     * Cập nhật thông tin môn học trong cơ sở dữ liệu
     * 
     * Cách thức hoạt động:
     * 1. Chuẩn bị dữ liệu cập nhật vào ContentValues
     * 2. Cập nhật bản ghi trong bảng subjects dựa trên ID môn học
     * 3. Trả về số lượng hàng bị ảnh hưởng
     * 
     * @param subject Đối tượng Subject chứa thông tin môn học cần cập nhật (không được null, phải có ID hợp lệ)
     * @return Số lượng hàng bị ảnh hưởng sau khi cập nhật (thường là 1 nếu thành công, 0 nếu thất bại)
     * @throws Exception Nếu có lỗi xảy ra trong quá trình cập nhật cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Xóa bản ghi trong bảng subjects dựa trên ID môn học
     * 2. Trả về số lượng hàng bị ảnh hưởng
     * 
     * @param subjectId ID của môn học cần xóa (phải là số nguyên dương)
     * @return Số lượng hàng bị ảnh hưởng sau khi xóa (thường là 1 nếu thành công, 0 nếu không tìm thấy)
     * @throws Exception Nếu có lỗi xảy ra trong quá trình xóa cơ sở dữ liệu
     */
    public int deleteSubject(int subjectId) {
        return database.delete(DatabaseHelper.TABLE_SUBJECTS, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(subjectId)});
    }

    // Score operations
    /**
     * Lấy danh sách tất cả điểm từ cơ sở dữ liệu
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn toàn bộ bản ghi trong bảng scores
     * 2. Duyệt qua từng dòng kết quả trong Cursor
     * 3. Tạo đối tượng Score từ dữ liệu trong mỗi dòng
     * 4. Thêm đối tượng Score vào danh sách kết quả
     * 
     * @return Danh sách các đối tượng Score, danh sách rỗng nếu không có điểm nào
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn điểm trong bảng scores với điều kiện ID học sinh
     * 2. Duyệt qua từng dòng kết quả trong Cursor
     * 3. Tạo đối tượng Score từ dữ liệu trong mỗi dòng
     * 4. Thêm đối tượng Score vào danh sách kết quả
     * 
     * @param studentId ID của học sinh (phải là số nguyên dương)
     * @return Danh sách các đối tượng Score của học sinh đó, danh sách rỗng nếu không có điểm nào
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn điểm trong bảng scores với điều kiện ID học sinh và ID môn học
     * 2. Duyệt qua từng dòng kết quả trong Cursor
     * 3. Tạo đối tượng Score từ dữ liệu trong mỗi dòng
     * 4. Thêm đối tượng Score vào danh sách kết quả
     * 
     * @param studentId ID của học sinh (phải là số nguyên dương)
     * @param subjectId ID của môn học (phải là số nguyên dương)
     * @return Danh sách các đối tượng Score của học sinh cho môn học cụ thể, danh sách rỗng nếu không có điểm nào
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Chuẩn bị dữ liệu điểm vào ContentValues để chèn vào cơ sở dữ liệu
     * 2. Chèn dữ liệu vào bảng scores
     * 3. Trả về ID của điểm vừa được thêm
     * 
     * @param score Đối tượng Score chứa thông tin điểm cần thêm (không được null)
     * @return ID của điểm vừa được thêm (số nguyên dương), hoặc -1 nếu thêm thất bại
     * @throws Exception Nếu có lỗi xảy ra trong quá trình chèn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Chuẩn bị dữ liệu cập nhật vào ContentValues
     * 2. Cập nhật bản ghi trong bảng scores dựa trên ID điểm
     * 3. Trả về số lượng hàng bị ảnh hưởng
     * 
     * @param score Đối tượng Score chứa thông tin điểm cần cập nhật (không được null, phải có ID hợp lệ)
     * @return Số lượng hàng bị ảnh hưởng sau khi cập nhật (thường là 1 nếu thành công, 0 nếu thất bại)
     * @throws Exception Nếu có lỗi xảy ra trong quá trình cập nhật cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Xóa bản ghi trong bảng scores dựa trên ID điểm
     * 2. Trả về số lượng hàng bị ảnh hưởng
     * 
     * @param scoreId ID của điểm cần xóa (phải là số nguyên dương)
     * @return Số lượng hàng bị ảnh hưởng sau khi xóa (thường là 1 nếu thành công, 0 nếu không tìm thấy)
     * @throws Exception Nếu có lỗi xảy ra trong quá trình xóa cơ sở dữ liệu
     */
    public int deleteScore(int scoreId) {
        return database.delete(DatabaseHelper.TABLE_SCORES, 
                              DatabaseHelper.COLUMN_ID + " = ?", 
                              new String[]{String.valueOf(scoreId)});
    }

    // Utility methods
    /**
     * Tính điểm trung bình của học sinh cho môn học cụ thể
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn bảng scores để lấy các điểm của học sinh cho môn học cụ thể
     * 2. Sử dụng hàm AVG của SQL để tính điểm trung bình
     * 3. Trả về điểm trung bình đã tính được
     * 
     * @param studentId ID của học sinh (phải là số nguyên dương)
     * @param subjectId ID của môn học (phải là số nguyên dương)
     * @return Điểm trung bình của học sinh cho môn học cụ thể, trả về 0 nếu không có điểm nào
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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
     * 
     * Cách thức hoạt động:
     * 1. Truy vấn bảng scores để lấy tất cả điểm của học sinh
     * 2. Sử dụng hàm AVG của SQL để tính điểm trung bình tổng kết
     * 3. Trả về điểm trung bình tổng kết đã tính được
     * 
     * @param studentId ID của học sinh (phải là số nguyên dương)
     * @return Điểm trung bình tổng kết của học sinh, trả về 0 nếu không có điểm nào
     * @throws Exception Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
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