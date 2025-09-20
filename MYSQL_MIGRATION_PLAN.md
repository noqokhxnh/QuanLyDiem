# Kế hoạch thay thế SQLite bằng MySQL (XAMPP)

## Tổng quan
Hiện tại ứng dụng đang sử dụng SQLite để lưu trữ dữ liệu cục bộ. Để thay thế bằng MySQL với XAMPP, chúng ta cần thực hiện các thay đổi sau:

## 1. Thiết lập môi trường MySQL (XAMPP)

### 1.1. Cài đặt XAMPP
- Tải và cài đặt XAMPP từ https://www.apachefriends.org/
- Khởi động Apache và MySQL services trong XAMPP Control Panel

### 1.2. Tạo database trong MySQL
- Truy cập phpMyAdmin thông qua http://localhost/phpmyadmin
- Tạo database mới có tên "student_manager"
- Tạo các bảng tương tự như trong SQLite:

```sql
-- Bảng users
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng students
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    student_code VARCHAR(50) UNIQUE NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    birth_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Bảng subjects
CREATE TABLE subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) NOT NULL,
    subject_code VARCHAR(50) UNIQUE NOT NULL
);

-- Bảng scores
CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    subject_id INT,
    score_type VARCHAR(20) NOT NULL,
    score DECIMAL(3,1) NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    teacher_id INT,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id),
    FOREIGN KEY (teacher_id) REFERENCES users(id)
);
```

### 1.3. Chèn dữ liệu mẫu
```sql
-- Người dùng mẫu
INSERT INTO users VALUES (1, 'tc1', '123456', 1, 'Nguyễn Văn Giáo', NOW());
INSERT INTO users VALUES (2, 'st1', '123456', 0, 'Trần Thị Học', NOW());
INSERT INTO users VALUES (3, 'st2', '123456', 0, 'Lê Văn Sinh', NOW());

-- Học sinh mẫu
INSERT INTO students VALUES (1, 2, 'STU001', 'Class 10A1', '2005-05-15');
INSERT INTO students VALUES (2, 3, 'STU002', 'Class 10A1', '2005-08-22');

-- Môn học mẫu
INSERT INTO subjects VALUES (1, 'Toán', 'TOAN');
INSERT INTO subjects VALUES (2, 'Lý', 'LY');
INSERT INTO subjects VALUES (3, 'Hóa', 'HOA');
INSERT INTO subjects VALUES (4, 'Văn', 'VAN');

-- Điểm mẫu
INSERT INTO scores VALUES (1, 1, 1, 'mieng', 8.5, NOW(), 1);
INSERT INTO scores VALUES (2, 1, 2, '15phut', 7.0, NOW(), 1);
INSERT INTO scores VALUES (3, 2, 1, 'mieng', 9.0, NOW(), 1);
INSERT INTO scores VALUES (4, 2, 3, '1tiet', 8.0, NOW(), 1);
```

## 2. Thay đổi trong ứng dụng Android

### 2.1. Thêm dependencies cần thiết
Trong file `app/build.gradle`, thêm:
```gradle
dependencies {
    // Các dependencies hiện tại...
    implementation 'mysql:mysql-connector-java:8.0.28'
    implementation 'com.android.volley:volley:1.2.1'
}
```

### 2.2. Tạo lớp DatabaseHelper mới
- Tạo package mới `database.mysql`
- Tạo class `MySQLHelper` để quản lý kết nối đến MySQL server

### 2.3. Tạo lớp DatabaseManager mới
- Tạo class `MySQLManager` trong package `database.mysql`
- Thay thế tất cả các phương thức truy vấn SQLite bằng các phương thức gọi API REST

### 2.4. Tạo REST API
- Tạo một project PHP đơn giản để cung cấp REST API cho các thao tác database
- Các endpoint cần thiết:
  - POST /api/login - Xác thực người dùng
  - GET /api/users - Lấy danh sách người dùng
  - POST /api/users - Thêm người dùng mới
  - PUT /api/users/{id} - Cập nhật người dùng
  - DELETE /api/users/{id} - Xóa người dùng
  - GET /api/students - Lấy danh sách học sinh
  - POST /api/students - Thêm học sinh mới
  - PUT /api/students/{id} - Cập nhật học sinh
  - DELETE /api/students/{id} - Xóa học sinh
  - GET /api/subjects - Lấy danh sách môn học
  - POST /api/subjects - Thêm môn học mới
  - GET /api/scores - Lấy danh sách điểm
  - POST /api/scores - Thêm điểm mới
  - PUT /api/scores/{id} - Cập nhật điểm
  - DELETE /api/scores/{id} - Xóa điểm
  - GET /api/scores/student/{id} - Lấy điểm theo học sinh
  - GET /api/students/user/{id} - Lấy học sinh theo user ID

## 3. Cấu trúc thư mục mới

```
app/src/main/java/com/example/qld/
├── activities/
│   ├── LoginActivity.java
│   ├── TeacherMainActivity.java
│   ├── StudentMainActivity.java
│   ├── ManageStudentsActivity.java
│   ├── ManageScoresActivity.java
│   └── ViewScoresActivity.java
├── adapters/
│   ├── StudentAdapter.java
│   └── ScoreAdapter.java
├── database/
│   ├── sqlite/
│   │   ├── DatabaseHelper.java
│   │   └── DatabaseManager.java
│   └── mysql/
│       ├── MySQLHelper.java
│       └── MySQLManager.java
├── models/
│   ├── User.java
│   ├── Student.java
│   ├── Subject.java
│   └── Score.java
├── utils/
│   ├── SessionManager.java
│   └── Constants.java
└── network/
    └── ApiClient.java
```

## 4. Các bước thực hiện chi tiết

### 4.1. Tuần 1: Thiết lập MySQL và REST API
1. Cài đặt XAMPP và khởi động services
2. Tạo database và các bảng trong MySQL
3. Tạo REST API PHP để kết nối với MySQL
4. Kiểm thử các endpoint API

### 4.2. Tuần 2: Tạo lớp MySQL trong ứng dụng
1. Thêm dependencies cần thiết vào build.gradle
2. Tạo package `database.mysql`
3. Tạo class `MySQLHelper` để quản lý kết nối
4. Tạo class `MySQLManager` với các phương thức tương tự `DatabaseManager`
5. Sử dụng Volley hoặc Retrofit để gọi API

### 4.3. Tuần 3: Tích hợp và kiểm thử
1. Thay thế việc sử dụng `DatabaseManager` bằng `MySQLManager`
2. Kiểm thử tất cả các chức năng
3. Tối ưu hóa hiệu suất nếu cần
4. Xử lý các lỗi kết nối mạng

## 5. Những thay đổi cần lưu ý

### 5.1. Xử lý bất đồng bộ
- Các thao tác database bây giờ sẽ bất đồng bộ do gọi qua mạng
- Cần cập nhật UI để hiển thị loading indicator
- Xử lý các trường hợp lỗi mạng

### 5.2. Bảo mật
- Không nên lưu mật khẩu dưới dạng plain text trong database
- Sử dụng HTTPS cho các kết nối API trong môi trường production
- Thêm xác thực token nếu cần

### 5.3. Hiệu suất
- Kết nối mạng chậm hơn so với database cục bộ
- Cần có cơ chế cache dữ liệu cục bộ nếu cần

## 6. Lợi ích của việc chuyển sang MySQL

1. **Chia sẻ dữ liệu**: Nhiều người dùng có thể truy cập cùng một database
2. **Backup và phục hồi**: Dễ dàng backup và phục hồi dữ liệu
3. **Mở rộng**: Dễ dàng mở rộng sang mô hình client-server
4. **Bảo mật**: Có thể áp dụng các biện pháp bảo mật của server

## 7. Rủi ro và hạn chế

1. **Yêu cầu kết nối mạng**: Ứng dụng cần có kết nối mạng để hoạt động
2. **Hiệu suất**: Có thể chậm hơn so với database cục bộ
3. **Phức tạp hơn**: Kiến trúc client-server phức tạp hơn database cục bộ