# Ứng dụng Quản lý Điểm Học Sinh

## 📋 Tổng quan
Đây là một ứng dụng Android quản lý điểm số cho giáo viên và học sinh, được phát triển như một phần của môn Lập trình Mobile. Ứng dụng cho phép giáo viên quản lý học sinh và nhập điểm, trong khi học sinh có thể xem điểm và điểm trung bình của mình.

## 🚀 Tính năng

### Cốt lõi
- **Xác thực người dùng** với phân quyền theo vai trò (giáo viên/học sinh)
- **CRUD học sinh** (chỉ giáo viên)
- **CRUD điểm số** (giáo viên nhập, học sinh xem)
- **Tính điểm trung bình** tự động
- **Giao diện người dùng thân thiện** với thiết kế Material

### Tính năng giáo viên
- ✅ Xem danh sách tất cả học sinh
- ✅ Thêm/sửa/xóa học sinh
- ✅ Nhập điểm cho học sinh (tất cả loại điểm: miệng, 15 phút, 1 tiết, học kỳ)
- ✅ Sửa/xóa điểm đã nhập
- ✅ Xem thống kê điểm theo lớp/môn
- ✅ Xuất báo cáo điểm
- ✅ Đổi mật khẩu

### Tính năng học sinh
- ✅ Xem điểm của chính mình
- ✅ Xem điểm trung bình cá nhân
- ✅ Tra cứu lịch sử điểm
- ✅ Đổi mật khẩu

## 🛠️ Công nghệ sử dụng

### Công nghệ chính (theo yêu cầu)
- **Ngôn ngữ**: Java (không dùng Kotlin)
- **IDE**: Android Studio
- **Database**: SQLite với SQLiteOpenHelper
- **UI**: XML Layout (không dùng Jetpack Compose)
- **Kiến trúc**: MVC đơn giản
- **Lưu trữ session**: SharedPreferences
- **UI Framework**: Thiết kế Material

### Không sử dụng
- ❌ Kotlin
- ❌ Jetpack Compose  
- ❌ Room Database
- ❌ Retrofit/API calls
- ❌ Firebase
- ❌ MVP/MVVM pattern phức tạp
- ❌ Dependency Injection frameworks

## 🏗️ Cấu trúc Project

```
app/src/main/java/com/yourname/studentmanager/
├── models/
│   ├── User.java
│   ├── Student.java  
│   ├── Subject.java
│   └── Score.java
├── database/
│   ├── DatabaseHelper.java
│   └── DatabaseManager.java
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
└── utils/
    ├── SessionManager.java
    └── Constants.java
```

## 📊 Schema Cơ sở dữ liệu

### Bảng users
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role INTEGER NOT NULL, -- 0: học sinh, 1: giáo viên
    full_name TEXT NOT NULL,
    created_date TEXT DEFAULT CURRENT_TIMESTAMP
);
```

### Bảng students  
```sql
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    student_code TEXT UNIQUE NOT NULL,
    class_name TEXT NOT NULL,
    birth_date TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Bảng subjects
```sql
CREATE TABLE subjects (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    subject_name TEXT NOT NULL,
    subject_code TEXT UNIQUE NOT NULL
);
```

### Bảng scores
```sql
CREATE TABLE scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER,
    subject_id INTEGER,
    score_type TEXT NOT NULL, -- 'mieng', '15phut', '1tiet', 'hocky'
    score REAL NOT NULL,
    date_created TEXT DEFAULT CURRENT_TIMESTAMP,
    teacher_id INTEGER,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id),
    FOREIGN KEY (teacher_id) REFERENCES users(id)
);
```

## 📱 Cài đặt và chạy ứng dụng

### Yêu cầu hệ thống
- Android Studio (bản mới nhất)
- Android SDK API level 21 trở lên
- Thiết bị hoặc emulator Android

### Cài đặt
1. Clone hoặc tải project về máy
2. Mở project trong Android Studio
3. Đồng bộ Gradle
4. Build và chạy ứng dụng trên thiết bị hoặc emulator

### Dữ liệu mẫu
Khi khởi động lần đầu, ứng dụng sẽ tạo các tài khoản mẫu:

Tài khoản giáo viên:
- Username: `teacher1`
- Password: `123456`
- Họ tên: `Nguyễn Văn Giáo`

Tài khoản học sinh:
- Username: `student1`
- Password: `123456`
- Họ tên: `Trần Thị Học`

## 👥 Phân quyền người dùng

### Role = 1: Giáo viên
Có toàn quyền quản lý học sinh và điểm số

### Role = 0: Học sinh  
Chỉ được xem điểm cá nhân

## 🎯 Quy tắc Code

### Naming Convention
- Classes: PascalCase (ví dụ: `LoginActivity.java`)
- Methods: camelCase (ví dụ: `authenticateUser()`)
- Variables: camelCase (ví dụ: `etUsername`)
- Constants: UPPER_SNAKE_CASE (ví dụ: `DATABASE_NAME`)
- Database fields: snake_case (ví dụ: `student_id`)

### Ngôn ngữ
- Code: 100% tiếng Anh
- Giao diện người dùng: 100% tiếng Việt

## 🔧 Cách đóng góp

1. Fork repository
2. Tạo nhánh mới cho tính năng của bạn
3. Commit thay đổi với mô tả rõ ràng
4. Push lên nhánh của bạn
5. Tạo Pull Request

## 📝 Giấy phép

Dự án này dành cho mục đích học tập, ưu tiên sự đơn giản và dễ hiểu hơn là công nghệ mới nhất.