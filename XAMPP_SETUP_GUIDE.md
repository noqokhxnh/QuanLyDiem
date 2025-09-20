# HƯỚNG DẪN CÀI ĐẶT VÀ CẤU HÌNH HỆ THỐNG

## 1. Cài đặt XAMPP

### 1.1. Tải XAMPP
1. Truy cập trang web chính thức: https://www.apachefriends.org/
2. Tải phiên bản XAMPP phù hợp với hệ điều hành của bạn (Windows, macOS, Linux)
3. Chạy file cài đặt và làm theo hướng dẫn

### 1.2. Khởi động XAMPP
1. Mở XAMPP Control Panel
2. Khởi động các dịch vụ sau:
   - Apache (Web Server)
   - MySQL (Database Server)
   
   ![XAMPP Control Panel](https://www.apachefriends.org/images/screenshots/xampp-windows-control-panel.png)

## 2. Cấu hình MySQL Database

### 2.1. Truy cập phpMyAdmin
1. Mở trình duyệt web
2. Truy cập địa chỉ: http://localhost/phpmyadmin

### 2.2. Tạo Database
1. Trong giao diện phpMyAdmin, click vào tab "Databases"
2. Nhập tên database: `student_manager`
3. Click "Create"

### 2.3. Tạo các bảng trong database
Chạy các câu lệnh SQL sau trong tab "SQL" của phpMyAdmin:

```sql
-- Tạo bảng users
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng students
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    student_code VARCHAR(50) UNIQUE NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    birth_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tạo bảng subjects
CREATE TABLE subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) NOT NULL,
    subject_code VARCHAR(50) UNIQUE NOT NULL
);

-- Tạo bảng scores
CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    subject_id INT,
    score_type VARCHAR(20) NOT NULL,
    score DECIMAL(3,1) NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    teacher_id INT,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### 2.4. Chèn dữ liệu mẫu
```sql
-- Người dùng mẫu
INSERT INTO users (id, username, password, role, full_name, created_date) VALUES 
(1, 'tc1', '123456', 1, 'Nguyễn Văn Giáo', NOW()),
(2, 'st1', '123456', 0, 'Trần Thị Học', NOW()),
(3, 'st2', '123456', 0, 'Lê Văn Sinh', NOW());

-- Học sinh mẫu
INSERT INTO students (id, user_id, student_code, class_name, birth_date) VALUES 
(1, 2, 'STU001', 'Class 10A1', '2005-05-15'),
(2, 3, 'STU002', 'Class 10A1', '2005-08-22');

-- Môn học mẫu
INSERT INTO subjects (id, subject_name, subject_code) VALUES 
(1, 'Toán', 'TOAN'),
(2, 'Lý', 'LY'),
(3, 'Hóa', 'HOA'),
(4, 'Văn', 'VAN');

-- Điểm mẫu
INSERT INTO scores (id, student_id, subject_id, score_type, score, date_created, teacher_id) VALUES 
(1, 1, 1, 'mieng', 8.5, NOW(), 1),
(2, 1, 2, '15phut', 7.0, NOW(), 1),
(3, 2, 1, 'mieng', 9.0, NOW(), 1),
(4, 2, 3, '1tiet', 8.0, NOW(), 1);
```

## 3. Cấu hình REST API

### 3.1. Sao chép API files
1. Tạo thư mục `api` trong thư mục `htdocs` của XAMPP:
   - Windows: `C:\xampp\htdocs\api`
   - macOS: `/Applications/XAMPP/xamppfiles/htdocs/api`
   - Linux: `/opt/lampp/htdocs/api`

2. Sao chép các file API vào thư mục này:
   - `config.php`
   - `login.php`
   - `users.php`
   - `students.php`
   - `subjects.php`
   - `scores.php`

### 3.2. Cấu hình kết nối database
Mở file `config.php` và cập nhật thông tin kết nối nếu cần:

```php
<?php
$host = 'localhost';
$db_name = 'student_manager';
$username = 'root';
$password = ''; // Mặc định là trống trong XAMPP

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db_name;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    die("Connection failed: " . $e->getMessage());
}
?>
```

## 4. Kiểm tra kết nối

### 4.1. Kiểm tra Apache và MySQL
1. Đảm bảo cả Apache và MySQL đều đang chạy trong XAMPP Control Panel
2. Mở trình duyệt và truy cập: http://localhost
   - Nếu thấy trang chào mừng của XAMPP thì Apache đang hoạt động

### 4.2. Kiểm tra phpMyAdmin
1. Truy cập: http://localhost/phpmyadmin
2. Kiểm tra xem database `student_manager` đã được tạo chưa
3. Kiểm tra các bảng đã được tạo và có dữ liệu mẫu

### 4.3. Kiểm tra API
1. Truy cập: http://localhost/api/users.php
2. Bạn nên thấy kết quả JSON trả về danh sách người dùng

## 5. Cấu hình ứng dụng Android

### 5.1. Cập nhật URL API
Trong file `ApiClient.java`, đảm bảo URL phù hợp với môi trường của bạn:

```java
// Đối với emulator Android:
private static final String BASE_URL = "http://10.0.2.2/api/";

// Đối với thiết bị thật (thay bằng IP của máy tính):
private static final String BASE_URL = "http://192.168.1.100/api/";
```

### 5.2. Cấp quyền truy cập Internet
Đảm bảo file `AndroidManifest.xml` có dòng sau:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## 6. Khắc phục sự cố thường gặp

### 6.1. Lỗi kết nối
- **Kiểm tra**: Apache và MySQL có đang chạy không?
- **Kiểm tra**: Tường lửa có chặn kết nối không?
- **Kiểm tra**: URL trong ứng dụng có đúng không?

### 6.2. Lỗi database
- **Kiểm tra**: Database `student_manager` đã được tạo chưa?
- **Kiểm tra**: Các bảng đã được tạo chưa?
- **Kiểm tra**: Có dữ liệu mẫu trong các bảng không?

### 6.3. Lỗi API
- **Kiểm tra**: Các file PHP có nằm đúng thư mục không?
- **Kiểm tra**: Có lỗi PHP nào không? (Bật display_errors trong php.ini)

## 7. Mẹo sử dụng

1. **Khởi động XAMPP** mỗi khi muốn sử dụng hệ thống
2. **Kiểm tra kết nối** trước khi chạy ứng dụng Android
3. **Xem log** trong Logcat nếu ứng dụng Android có lỗi
4. **Sử dụng Postman** để test API nếu cần debug

## 8. Tài nguyên bổ sung

- [Tài liệu chính thức XAMPP](https://www.apachefriends.org/docs/)
- [Hướng dẫn MySQL](https://dev.mysql.com/doc/)
- [Hướng dẫn PHP](https://www.php.net/manual/)
- [Tài liệu Android Developer](https://developer.android.com/docs)