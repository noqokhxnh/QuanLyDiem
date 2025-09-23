# 🔍 Hướng dẫn Debug Login

## ✅ **Đã sửa lỗi đăng nhập:**

1. **Tăng Database Version** từ 2 → 3 để đảm bảo database được tạo mới
2. **Thêm Logging** để debug quá trình đăng nhập
3. **Xóa database cũ** trong onUpgrade để tránh xung đột

---

## 🔐 **Thông tin đăng nhập:**

- **Username**: `admin`
- **Password**: `123`

---

## 📱 **Cách kiểm tra:**

### **1. Xóa dữ liệu ứng dụng (Quan trọng!):**
1. Mở **Settings** trên điện thoại
2. Tìm **Apps** hoặc **Application Manager**
3. Tìm ứng dụng **StudentScoreManager**
4. Chọn **Storage** → **Clear Data**
5. Hoặc **Uninstall** và **Install lại**

### **2. Chạy ứng dụng:**
1. Mở ứng dụng
2. Nhập: `admin` / `123`
3. Nhấn **Đăng nhập**

### **3. Kiểm tra Logcat:**
Mở **Logcat** trong Android Studio và tìm các log sau:

```
DatabaseHelper: onCreate called - creating tables
DatabaseHelper: Users table created
DatabaseHelper: Students table created
DatabaseHelper: Scores table created
DatabaseHelper: Inserting sample data
DatabaseHelper: Admin user inserted with result: 1
LoginActivity: Attempting login with username: admin
DatabaseHelper: Checking user: admin
DatabaseHelper: User check result: true (count: 1)
LoginActivity: Login successful, navigating to StudentMainActivity
```

---

## 🚨 **Nếu vẫn không được:**

### **Kiểm tra Logcat để tìm lỗi:**

1. **Mở Android Studio**
2. **View** → **Tool Windows** → **Logcat**
3. **Filter** theo tag: `LoginActivity` hoặc `DatabaseHelper`
4. **Chạy ứng dụng** và thử đăng nhập
5. **Copy log** và gửi cho tôi

### **Các log cần tìm:**
- ✅ `DatabaseHelper: onCreate called`
- ✅ `DatabaseHelper: Admin user inserted with result: 1`
- ❌ Nếu không thấy → Database không được tạo
- ❌ Nếu thấy `result: -1` → Lỗi insert user

---

## 🔧 **Nếu vẫn lỗi:**

### **Thử cách khác:**
1. **Uninstall** ứng dụng hoàn toàn
2. **Restart** điện thoại/máy tính
3. **Clean Project** trong Android Studio
4. **Rebuild Project**
5. **Install lại** ứng dụng

### **Hoặc tạo tài khoản mới:**
- Nhập username/password bất kỳ
- Nhập email khi được hỏi
- Ứng dụng sẽ tự động đăng ký và đăng nhập

---

**🎯 Hãy thử và cho tôi biết kết quả!**
