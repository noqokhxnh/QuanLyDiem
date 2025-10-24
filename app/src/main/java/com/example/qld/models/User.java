package com.example.qld.models;

/**
 * Lớp đại diện cho người dùng trong hệ thống
 * Chứa thông tin cá nhân và vai trò của người dùng
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String role; // 'ADMIN', 'TEACHER', 'STUDENT'
    private int studentId; // Foreign key to Students table
    private String createdDate;

    /**
     * Constructor mặc định
     */
    public User() {}

    /**
     * Constructor đầy đủ tham số
     * @param id ID của người dùng
     * @param username Tên đăng nhập của người dùng
     * @param password Mật khẩu của người dùng
     * @param fullName Họ tên đầy đủ của người dùng
     * @param role Vai trò của người dùng ('ADMIN', 'TEACHER', 'STUDENT')
     * @param studentId ID của học sinh liên kết với người dùng (nếu có)
     * @param createdDate Ngày tạo tài khoản
     */
    public User(int id, String username, String password, String fullName, String role, int studentId, String createdDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.studentId = studentId;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    /**
     * Lấy ID của người dùng
     * @return ID của người dùng
     */
    public int getId() { return id; }
    /**
     * Thiết lập ID cho người dùng
     * @param id ID của người dùng
     */
    public void setId(int id) { this.id = id; }

    /**
     * Lấy tên đăng nhập của người dùng
     * @return Tên đăng nhập của người dùng
     */
    public String getUsername() { return username; }
    /**
     * Thiết lập tên đăng nhập cho người dùng
     * @param username Tên đăng nhập của người dùng
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Lấy mật khẩu của người dùng
     * @return Mật khẩu của người dùng
     */
    public String getPassword() { return password; }
    /**
     * Thiết lập mật khẩu cho người dùng
     * @param password Mật khẩu của người dùng
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Lấy vai trò của người dùng
     * @return Vai trò của người dùng ('ADMIN', 'TEACHER', 'STUDENT')
     */
    public String getRole() { return role; }
    /**
     * Thiết lập vai trò cho người dùng
     * @param role Vai trò của người dùng ('ADMIN', 'TEACHER', 'STUDENT')
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Lấy ID học sinh liên kết với người dùng
     * @return ID học sinh liên kết hoặc 0 nếu không có
     */
    public int getStudentId() { return studentId; }
    /**
     * Thiết lập ID học sinh liên kết với người dùng
     * @param studentId ID học sinh liên kết
     */
    public void setStudentId(int studentId) { this.studentId = studentId; }

    /**
     * Lấy họ tên đầy đủ của người dùng
     * @return Họ tên đầy đủ của người dùng
     */
    public String getFullName() { return fullName; }
    /**
     * Thiết lập họ tên đầy đủ cho người dùng
     * @param fullName Họ tên đầy đủ của người dùng
     */
    public void setFullName(String fullName) { this.fullName = fullName; }

    /**
     * Lấy ngày tạo tài khoản
     * @return Ngày tạo tài khoản
     */
    public String getCreatedDate() { return createdDate; }
    /**
     * Thiết lập ngày tạo tài khoản
     * @param createdDate Ngày tạo tài khoản
     */
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}