# 📊 Hướng dẫn xem Database - StudentScoreManager

## 🎯 **Cách xem Database trong ứng dụng:**

### **1. Thông qua ứng dụng (Dễ nhất):**

#### **Bước 1: Đăng nhập**
- Mở ứng dụng
- Nhập: `admin` / `123`

#### **Bước 2: Vào Cài đặt**
- Từ trang chủ → Nhấn **"Cài đặt"**
- Hoặc dùng Bottom Navigation → **"Đăng xuất"** (icon cài đặt)

#### **Bước 3: Xem Database**
- Trong phần **"Hỗ trợ"** → Nhấn **"Xem Database"**
- Sẽ mở màn hình Database Viewer với 3 nút:
  - **Users** - Xem bảng người dùng
  - **Students** - Xem bảng sinh viên
  - **Scores** - Xem bảng điểm số

#### **Bước 4: Khám phá dữ liệu**
- Nhấn từng nút để xem dữ liệu tương ứng
- Dữ liệu hiển thị dạng text dễ đọc
- Có thể scroll để xem tất cả

---

## 📋 **Dữ liệu mẫu trong Database:**

### **Bảng Users:**
```
=== BẢNG USERS ===

Số lượng users: 1

ID: 1
Username: admin
Password: 123
Email: admin@test.com
---
```

### **Bảng Students:**
```
=== BẢNG STUDENTS ===

Số lượng students: 1

Mã SV: 20201234
Họ tên: Nguyễn Văn A
Lớp: D20CQCN01
Khoa: Công nghệ thông tin
SĐT: 0123456789
Email: nguyenvana@university.edu
---
```

### **Bảng Scores:**
```
=== BẢNG SCORES ===

Số lượng scores: 4

ID: 1
Mã SV: 20201234
Môn: Lập trình Java
Giữa kỳ: 8.5
Cuối kỳ: 9.0
Trung bình: 8.8
Học kỳ: HK1
Năm: 2024-2025
---

ID: 2
Mã SV: 20201234
Môn: Cơ sở dữ liệu
Giữa kỳ: 7.5
Cuối kỳ: 8.0
Trung bình: 7.8
Học kỳ: HK1
Năm: 2024-2025
---

ID: 3
Mã SV: 20201234
Môn: Mạng máy tính
Giữa kỳ: 8.0
Cuối kỳ: 8.5
Trung bình: 8.3
Học kỳ: HK1
Năm: 2024-2025
---

ID: 4
Mã SV: 20201234
Môn: Phân tích thiết kế hệ thống
Giữa kỳ: 9.0
Cuối kỳ: 8.5
Trung bình: 8.8
Học kỳ: HK1
Năm: 2024-2025
---
```

---

## 🔧 **Các cách khác để xem Database:**

### **2. Android Studio Database Inspector:**
1. Mở **Android Studio**
2. **View** → **Tool Windows** → **App Inspection**
3. Chọn tab **Database Inspector**
4. Chạy ứng dụng và đăng nhập
5. Tìm **StudentScoreDB** trong danh sách

### **3. ADB Shell:**
```bash
# Kết nối thiết bị
adb devices

# Mở shell
adb shell

# Vào thư mục database
cd /data/data/com.example.studentscoremanager/databases/

# Mở SQLite
sqlite3 StudentScoreDB

# Xem dữ liệu
SELECT * FROM Users;
SELECT * FROM Students;
SELECT * FROM Scores;

# Thoát
.quit
```

### **4. SQLite Browser:**
1. Tải **SQLite Browser** từ: https://sqlitebrowser.org/
2. Copy database file từ thiết bị:
   ```bash
   adb pull /data/data/com.example.studentscoremanager/databases/StudentScoreDB ./StudentScoreDB
   ```
3. Mở file bằng SQLite Browser

---

## 🚨 **Lưu ý quan trọng:**

1. **Database chỉ tồn tại** sau khi chạy ứng dụng và đăng nhập
2. **Xóa dữ liệu ứng dụng** sẽ xóa database
3. **Database Viewer** chỉ đọc dữ liệu, không thể chỉnh sửa
4. **Cần quyền root** để truy cập trực tiếp database file

---

## 🎯 **Tính năng Database Viewer:**

### **✅ Đã hoàn thiện:**
- ✅ Xem bảng Users
- ✅ Xem bảng Students  
- ✅ Xem bảng Scores
- ✅ Hiển thị số lượng records
- ✅ Giao diện đẹp, dễ đọc
- ✅ Nút quay lại tiện lợi

### **🔧 Có thể mở rộng:**
- Thêm chức năng chỉnh sửa dữ liệu
- Thêm chức năng thêm/xóa records
- Thêm bộ lọc và tìm kiếm
- Thêm export dữ liệu

---

**🎊 Bây giờ bạn có thể dễ dàng xem database của ứng dụng!**
