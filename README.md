# Ứng dụng Quản lý Học sinh

## Tổng quan
Đây là một ứng dụng Android để quản lý điểm số của học sinh, với các vai trò riêng biệt cho giáo viên và học sinh. Ứng dụng cho phép giáo viên quản lý học sinh và nhập điểm, trong khi học sinh có thể xem điểm và điểm trung bình của mình.

## Tính năng
- Xác thực người dùng với quyền truy cập dựa trên vai trò (giáo viên/học sinh)
- Tính năng dành cho giáo viên:
  - Quản lý học sinh (thêm, sửa, xóa)
  - Quản lý điểm (thêm, sửa, xóa)
  - Thống kê điểm theo lớp/môn
  - Xuất báo cáo điểm
- Tính năng dành cho học sinh:
  - Xem điểm cá nhân
  - Xem điểm trung bình
- Cơ sở dữ liệu MySQL để lưu trữ dữ liệu trên server
- Giao diện người dùng sạch sẽ, trực quan

## Công nghệ sử dụng
- Java
- Cơ sở dữ liệu MySQL với REST API
- Android SDK
- Thư viện Volley để giao tiếp mạng
- Thành phần Thiết kế Material

## Yêu cầu hệ thống
Trước khi bắt đầu, đảm bảo bạn đã cài đặt:
- Android Studio (phiên bản mới nhất được khuyến nghị)
- XAMPP (hoặc bất kỳ môi trường server nào có Apache và MySQL)
- Git
- Android SDK (API level 21 hoặc cao hơn)

## Cài đặt và cấu hình

### 1. Cài đặt XAMPP
1. Tải XAMPP từ https://www.apachefriends.org/
2. Cài đặt XAMPP theo hướng dẫn
3. Khởi động Apache và MySQL services trong XAMPP Control Panel

### 2. Cấu hình cơ sở dữ liệu MySQL
1. Truy cập phpMyAdmin thông qua http://localhost/phpmyadmin
2. Tạo database mới có tên "student_manager"
3. Chạy script SQL từ file `student_manager_database.sql` để tạo các bảng và chèn dữ liệu mẫu

### 3. Cấu hình REST API
1. Sao chép tất cả các file trong thư mục `api/` vào thư mục `htdocs/api/` của XAMPP
2. Mở file `config.php` trong thư mục `api/` và cập nhật thông tin kết nối database nếu cần

### 4. Cấu hình ứng dụng Android
1. Mở project trong Android Studio
2. Kiểm tra và cập nhật URL API trong file `ApiClient.java` nếu cần
3. Đảm bảo thiết bị Android hoặc emulator có thể truy cập được server

## Làm thế nào để clone project

### Phương pháp 1: Sử dụng Android Studio (Khuyến nghị)
1. Mở Android Studio
2. Chọn "Get from VCS" hoặc "Get from Version Control"
3. Nhập URL repository
4. Chọn thư mục nơi bạn muốn clone project
5. Click "Clone"

### Phương pháp 2: Sử dụng dòng lệnh
1. Mở terminal/dòng lệnh
2. Điều hướng đến thư mục nơi bạn muốn clone project:
   ```bash
   cd /đường/dẫn/đến/thư/mục/mong/muốn
   ```
3. Clone repository:
   ```bash
   git clone [URL_REPOSITORY]
   ```
4. Mở Android Studio
5. Chọn "Open an existing Android Studio project"
6. Điều hướng đến thư mục project đã clone và chọn nó

## Cấu trúc project
```
app/src/main/java/com/example/qld/
├── activities/
│   ├── LoginActivity.java
│   ├── TeacherMainActivity.java
│   ├── StudentMainActivity.java
│   ├── ManageStudentsActivity.java
│   ├── ManageScoresActivity.java
│   ├── ViewScoresActivity.java
│   └── ... (các activity khác)
├── adapters/
│   ├── StudentAdapter.java
│   └── ScoreAdapter.java
├── database/
│   └── mysql/
│       ├── MySQLManager.java
│       └── ... (các class liên quan đến MySQL)
├── models/
│   ├── User.java
│   ├── Student.java
│   ├── Subject.java
│   └── Score.java
├── network/
│   └── ApiClient.java
└── utils/
    ├── SessionManager.java
    └── ... (các utility class)
```

## Làm thế nào để build và chạy

### Sử dụng Android Studio (Khuyến nghị)
1. Mở project trong Android Studio
2. Chờ Gradle đồng bộ (có thể mất vài phút)
3. Kết nối thiết bị Android hoặc khởi động emulator
4. Click nút "Run" (tam giác xanh) hoặc nhấn `Shift + F10`

### Sử dụng dòng lệnh
1. Mở terminal/dòng lệnh
2. Điều hướng đến thư mục project:
   ```bash
   cd /đường/dẫn/đến/project
   ```
3. Build project:
   ```bash
   ./gradlew assembleDebug
   ```
4. Cài đặt APK trên thiết bị Android hoặc emulator

## Làm thế nào để thay đổi code

### 1. Tạo nhánh mới (Khuyến nghị)
Trước khi thực hiện bất kỳ thay đổi nào, hãy tạo nhánh mới:
```bash
git checkout -b feature/tên-tính-năng-của-bạn
```

### 2. Sửa đổi code
- Mở project trong Android Studio
- Thực hiện các thay đổi mong muốn trong code
- Kiểm tra kỹ các thay đổi của bạn

### 3. Commit các thay đổi
Sau khi thực hiện thay đổi:

1. Stage các thay đổi:
   ```bash
   git add .
   ```
   Hoặc để thêm các file cụ thể:
   ```bash
   git add đường/dẫn/đến/file.java
   ```

2. Commit các thay đổi với thông báo mô tả:
   ```bash
   git commit -m "Mô tả ngắn gọn những gì bạn đã làm"
   ```

### 4. Đẩy thay đổi lên repository
Đẩy các thay đổi của bạn lên repository:
```bash
git push origin feature/tên-tính-năng-của-bạn
```

## Khắc phục sự cố

### Vấn đề kết nối mạng
Nếu bạn gặp vấn đề kết nối mạng:
1. Kiểm tra xem Apache và MySQL trong XAMPP có đang chạy không
2. Kiểm tra firewall có chặn kết nối không
3. Kiểm tra URL API trong file `ApiClient.java` có đúng không

### Vấn đề database
Nếu bạn gặp vấn đề database:
1. Kiểm tra xem database `student_manager` đã được tạo chưa
2. Kiểm tra các bảng đã được tạo chưa
3. Kiểm tra có dữ liệu mẫu trong các bảng không

### Vấn đề API
Nếu bạn gặp vấn đề API:
1. Kiểm tra các file PHP có nằm đúng thư mục không
2. Kiểm tra có lỗi PHP nào không (Bật display_errors trong php.ini)

## Đóng góp
1. Fork repository
2. Tạo nhánh mới cho tính năng hoặc bản sửa lỗi của bạn
3. Thực hiện các thay đổi
4. Kiểm tra kỹ lưỡng
5. Commit các thay đổi với thông báo mô tả
6. Đẩy lên fork của bạn
7. Tạo pull request

## Tài nguyên bổ sung

### Tài liệu tham khảo
- [Tài liệu chính thức XAMPP](https://www.apachefriends.org/docs/)
- [Hướng dẫn MySQL](https://dev.mysql.com/doc/)
- [Hướng dẫn PHP](https://www.php.net/manual/)
- [Tài liệu Android Developer](https://developer.android.com/docs)

### File quan trọng
- `XAMPP_SETUP_GUIDE.md` - Hướng dẫn chi tiết cài đặt và cấu hình XAMPP
- `student_manager_database.sql` - Script SQL để tạo database và chèn dữ liệu mẫu
- `api/` - Thư mục chứa các file REST API
- `MYSQL_MIGRATION_PLAN.md` - Kế hoạch chuyển đổi từ SQLite sang MySQL

## Giấy phép
Project này dành cho mục đích giáo dục và không có giấy phép cụ thể. Vui lòng kiểm tra với tổ chức của bạn để biết hướng dẫn sử dụng.
