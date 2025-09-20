# API cho ứng dụng Quản lý Học sinh

## Giới thiệu
Thư mục này chứa các file PHP để tạo REST API kết nối với database MySQL cho ứng dụng Quản lý Học sinh.

## Yêu cầu hệ thống
- XAMPP (bao gồm Apache và MySQL)
- PHP 7.0 hoặc cao hơn

## Cách cài đặt

### 1. Cài đặt XAMPP
1. Tải XAMPP từ https://www.apachefriends.org/
2. Cài đặt XAMPP theo hướng dẫn

### 2. Khởi động services
1. Mở XAMPP Control Panel
2. Khởi động Apache và MySQL

### 3. Tạo database
1. Truy cập phpMyAdmin qua http://localhost/phpmyadmin
2. Tạo database mới tên "student_manager"
3. Chạy các câu lệnh SQL để tạo bảng (xem trong file MYSQL_MIGRATION_PLAN.md)

### 4. Sao chép API files
1. Sao chép tất cả các file trong thư mục này vào thư mục `htdocs` của XAMPP
2. Thường là `C:\xampp\htdocs\api` trên Windows hoặc `/opt/lampp/htdocs/api` trên Linux

### 5. Cấu hình kết nối database
1. Mở file `config.php`
2. Cập nhật thông tin kết nối database nếu cần:
   ```php
   $host = 'localhost';
   $db_name = 'student_manager';
   $username = 'root';
   $password = '';
   ```

## Các endpoint API

### Người dùng (Users)
- `POST /api/login.php` - Xác thực người dùng
- `GET /api/users.php` - Lấy danh sách tất cả người dùng
- `GET /api/users.php?id={id}` - Lấy thông tin người dùng theo ID
- `POST /api/users.php` - Thêm người dùng mới
- `PUT /api/users.php` - Cập nhật thông tin người dùng
- `DELETE /api/users.php?id={id}` - Xóa người dùng

### Học sinh (Students)
- `GET /api/students.php` - Lấy danh sách tất cả học sinh
- `GET /api/students.php?id={id}` - Lấy thông tin học sinh theo ID
- `POST /api/students.php` - Thêm học sinh mới
- `PUT /api/students.php` - Cập nhật thông tin học sinh
- `DELETE /api/students.php?id={id}` - Xóa học sinh

### Môn học (Subjects)
- `GET /api/subjects.php` - Lấy danh sách tất cả môn học
- `POST /api/subjects.php` - Thêm môn học mới

### Điểm (Scores)
- `GET /api/scores.php` - Lấy danh sách tất cả điểm
- `GET /api/scores.php?student_id={id}` - Lấy điểm theo học sinh
- `POST /api/scores.php` - Thêm điểm mới
- `PUT /api/scores.php` - Cập nhật điểm
- `DELETE /api/scores.php?id={id}` - Xóa điểm

## Cấu trúc JSON

### User
```json
{
  "id": 1,
  "username": "tc1",
  "password": "123456",
  "role": 1,
  "full_name": "Nguyễn Văn Giáo",
  "created_date": "2023-01-01 12:00:00"
}
```

### Student
```json
{
  "id": 1,
  "user_id": 2,
  "student_code": "STU001",
  "class_name": "Class 10A1",
  "birth_date": "2005-05-15"
}
```

### Subject
```json
{
  "id": 1,
  "subject_name": "Toán",
  "subject_code": "TOAN"
}
```

### Score
```json
{
  "id": 1,
  "student_id": 1,
  "subject_id": 1,
  "score_type": "mieng",
  "score": 8.5,
  "date_created": "2023-01-01 12:00:00",
  "teacher_id": 1
}
```

## Kiểm thử API
Bạn có thể kiểm thử API bằng các công cụ như Postman hoặc curl.

Ví dụ với curl:
```bash
# Đăng nhập
curl -X POST http://localhost/api/login.php -H "Content-Type: application/json" -d '{"username":"tc1","password":"123456"}'

# Lấy danh sách người dùng
curl http://localhost/api/users.php
```