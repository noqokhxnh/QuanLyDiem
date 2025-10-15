# Ứng dụng Quản lý Điểm (Student Grade Manager)

## Tổng quan

Ứng dụng Android quản lý điểm số cho giáo viên và học sinh, được xây dựng bằng Java với SQLite làm cơ sở dữ liệu. Ứng dụng hỗ trợ phân quyền giữa giáo viên và học sinh với các tính năng riêng biệt cho từng vai trò.

## Tính năng chính

### Đối với Giáo viên
- ✅ **Đăng nhập hệ thống** - Xác thực với tài khoản giáo viên
- ✅ **Quản lý học sinh** - Xem danh sách học sinh
- ✅ **Đăng ký học sinh** - Tạo tài khoản mới cho học sinh (chỉ giáo viên được phép)
- ✅ **Quản lý điểm số** - Nhập, chỉnh sửa điểm cho học sinh
- ✅ **Xem báo cáo** - Thống kê điểm số
- ✅ **Đổi mật khẩu** - Cập nhật mật khẩu cá nhân
- ✅ **Đăng xuất** - Kết thúc phiên làm việc

### Đối với Học sinh  
- ✅ **Đăng nhập hệ thống** - Xác thực với tài khoản học sinh
- ✅ **Xem điểm của mình** - Tra cứu bảng điểm cá nhân
- ✅ **Xem báo cáo** - Thống kê điểm số cá nhân
- ✅ **Đổi mật khẩu** - Cập nhật mật khẩu cá nhân
- ✅ **Đăng xuất** - Kết thúc phiên làm việc

## Kiến trúc ứng dụng

### Công nghệ sử dụng
- **Ngôn ngữ lập trình**: Java
- **Cơ sở dữ liệu**: SQLite
- **Giao diện người dùng**: XML Layout
- **Kiến trúc**: MVC đơn giản

### Cấu trúc cơ sở dữ liệu
1. **users** - Lưu thông tin người dùng (giáo viên và học sinh)
2. **students** - Lưu thông tin chi tiết của học sinh
3. **subjects** - Danh sách các môn học
4. **scores** - Bảng điểm của học sinh

### Cấu trúc thư mục
```
app/src/main/java/com/example/qld/
├── models/           # Các model class
│   ├── User.java
│   ├── Student.java  
│   ├── Subject.java
│   └── Score.java
├── database/         # Database helper và manager
│   ├── DatabaseHelper.java
│   └── DatabaseManager.java
├── activities/       # Các activity chính
│   ├── LoginActivity.java
│   ├── TeacherMainActivity.java
│   ├── StudentMainActivity.java
│   ├── ManageStudentsActivity.java
│   ├── ManageScoresActivity.java
│   ├── ViewScoresActivity.java
│   ├── AddStudentActivity.java
│   ├── AddScoreActivity.java
│   ├── ChangePasswordActivity.java
│   └── StatisticsActivity.java
├── adapters/         # RecyclerView adapters
│   ├── StudentAdapter.java
│   └── ScoreAdapter.java
└── utils/            # Utility classes
    ├── SessionManager.java
    └── Constants.java
```

## Tính năng đặc biệt

### 1. Phân quyền người dùng
- **Giáo viên (Role = 1)**: Có thể quản lý học sinh, nhập điểm, xem báo cáo
- **Học sinh (Role = 0)**: Chỉ xem điểm của mình, không thể truy cập tính năng giáo viên

### 2. Đăng ký học sinh
- Chỉ giáo viên mới có thể đăng ký tài khoản cho học sinh mới
- Học sinh không thể tự đăng ký (chức năng này đã được vô hiệu hóa)

### 3. Đổi mật khẩu
- Cả giáo viên và học sinh đều có thể đổi mật khẩu của mình
- Yêu cầu xác nhận mật khẩu hiện tại trước khi đổi

### 4. Quản lý điểm linh hoạt
- Hỗ trợ nhiều loại điểm: điểm miệng, 15 phút, 1 tiết, thi học kỳ
- Tính điểm trung bình tự động

## Cài đặt

1. Clone hoặc tải mã nguồn về
2. Mở dự án trong Android Studio
3. Đồng bộ Gradle
4. Build và chạy ứng dụng

## Tên đăng nhập mặc định

### Giáo viên
- **Tên đăng nhập**: `teacher1`
- **Mật khẩu**: `123456`

### Học sinh
- **Tên đăng nhập**: `student1`
- **Mật khẩu**: `123456`
- **Tên đăng nhập 2**: `student2`
- **Mật khẩu 2**: `123456`

## Bảo trì

Dự án này được xây dựng phục vụ mục đích học tập với các tiêu chí:
- Đơn giản, dễ hiểu
- Tính năng đầy đủ
- Tuân thủ quy tắc lập trình Android
- Không sử dụng công nghệ quá phức tạp

## Giấy phép

Dự án học tập - được sử dụng cho môn Lập trình Mobile.