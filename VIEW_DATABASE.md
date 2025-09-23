# 📊 Hướng dẫn xem Database

## 🔍 **Cách xem Database SQLite:**

### **1. Sử dụng Android Studio Database Inspector:**

#### **Bước 1: Mở Database Inspector**
1. Mở **Android Studio**
2. **View** → **Tool Windows** → **App Inspection**
3. Chọn tab **Database Inspector**

#### **Bước 2: Chạy ứng dụng**
1. **Run** ứng dụng trên thiết bị/emulator
2. **Đăng nhập** với `admin`/`123`
3. Quay lại **Database Inspector**

#### **Bước 3: Xem Database**
1. Tìm **StudentScoreDB** trong danh sách
2. Click vào để xem các bảng:
   - **Users** - Bảng người dùng
   - **Students** - Bảng sinh viên  
   - **Scores** - Bảng điểm số

---

### **2. Sử dụng ADB Shell:**

#### **Bước 1: Kết nối thiết bị**
```bash
adb devices
```

#### **Bước 2: Mở shell**
```bash
adb shell
```

#### **Bước 3: Tìm database**
```bash
# Tìm package name
pm list packages | grep studentscore

# Vào thư mục database
cd /data/data/com.example.studentscoremanager/databases/

# Xem file database
ls -la
```

#### **Bước 4: Mở SQLite**
```bash
# Mở database
sqlite3 StudentScoreDB

# Xem các bảng
.tables

# Xem cấu trúc bảng Users
.schema Users

# Xem dữ liệu bảng Users
SELECT * FROM Users;

# Xem dữ liệu bảng Students
SELECT * FROM Students;

# Xem dữ liệu bảng Scores
SELECT * FROM Scores;

# Thoát
.quit
```

---

### **3. Sử dụng ứng dụng SQLite Browser:**

#### **Bước 1: Tải SQLite Browser**
- Tải từ: https://sqlitebrowser.org/

#### **Bước 2: Copy database file**
```bash
# Copy database từ thiết bị
adb pull /data/data/com.example.studentscoremanager/databases/StudentScoreDB ./StudentScoreDB
```

#### **Bước 3: Mở bằng SQLite Browser**
1. Mở **SQLite Browser**
2. **Open Database** → Chọn file `StudentScoreDB`
3. Xem dữ liệu trong tab **Browse Data**

---

## 📋 **Cấu trúc Database:**

### **Bảng Users:**
```sql
CREATE TABLE Users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE,
    password TEXT,
    email TEXT
);
```

**Dữ liệu mẫu:**
| id | username | password | email |
|----|----------|----------|-------|
| 1  | admin    | 123      | admin@test.com |

### **Bảng Students:**
```sql
CREATE TABLE Students (
    student_id TEXT PRIMARY KEY,
    full_name TEXT NOT NULL,
    class_name TEXT,
    faculty TEXT,
    phone TEXT,
    student_email TEXT,
    avatar TEXT
);
```

**Dữ liệu mẫu:**
| student_id | full_name | class_name | faculty | phone | student_email |
|------------|-----------|------------|---------|-------|---------------|
| 20201234   | Nguyễn Văn A | D20CQCN01 | Công nghệ thông tin | 0123456789 | nguyenvana@university.edu |

### **Bảng Scores:**
```sql
CREATE TABLE Scores (
    score_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id TEXT,
    subject TEXT NOT NULL,
    midterm_score REAL,
    final_score REAL,
    average_score REAL,
    semester TEXT,
    year TEXT,
    FOREIGN KEY(student_id) REFERENCES Students(student_id)
);
```

**Dữ liệu mẫu:**
| score_id | student_id | subject | midterm_score | final_score | average_score | semester | year |
|----------|------------|---------|---------------|-------------|---------------|----------|------|
| 1 | 20201234 | Lập trình Java | 8.5 | 9.0 | 8.8 | HK1 | 2024-2025 |
| 2 | 20201234 | Cơ sở dữ liệu | 7.5 | 8.0 | 7.8 | HK1 | 2024-2025 |
| 3 | 20201234 | Mạng máy tính | 8.0 | 8.5 | 8.3 | HK1 | 2024-2025 |
| 4 | 20201234 | Phân tích thiết kế hệ thống | 9.0 | 8.5 | 8.8 | HK1 | 2024-2025 |

---

## 🔧 **Các lệnh SQL hữu ích:**

### **Xem tất cả dữ liệu:**
```sql
-- Xem tất cả users
SELECT * FROM Users;

-- Xem tất cả students
SELECT * FROM Students;

-- Xem tất cả scores
SELECT * FROM Scores;

-- Xem điểm của sinh viên cụ thể
SELECT * FROM Scores WHERE student_id = '20201234';
```

### **Thêm dữ liệu mới:**
```sql
-- Thêm user mới
INSERT INTO Users (username, password, email) VALUES ('user2', '456', 'user2@test.com');

-- Thêm điểm mới
INSERT INTO Scores (student_id, subject, midterm_score, final_score, average_score, semester, year) 
VALUES ('20201234', 'Toán cao cấp', 8.0, 8.5, 8.3, 'HK2', '2024-2025');
```

### **Cập nhật dữ liệu:**
```sql
-- Đổi mật khẩu
UPDATE Users SET password = 'newpass' WHERE username = 'admin';

-- Cập nhật điểm
UPDATE Scores SET final_score = 9.5 WHERE student_id = '20201234' AND subject = 'Lập trình Java';
```

---

## 🚨 **Lưu ý quan trọng:**

1. **Database chỉ tồn tại** sau khi chạy ứng dụng và đăng nhập
2. **Cần quyền root** để truy cập trực tiếp database file
3. **Database Inspector** là cách dễ nhất để xem dữ liệu
4. **Backup database** trước khi thay đổi dữ liệu

---

**🎯 Chọn phương pháp phù hợp và khám phá database của bạn!**
