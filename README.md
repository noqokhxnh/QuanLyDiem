# Ứng dụng Quản lý Điểm (Student Grade Manager)

## Tổng quan

Ứng dụng Android quản lý điểm số cho giáo viên, học sinh và quản trị viên, được xây dựng bằng Java với SQLite làm cơ sở dữ liệu. Ứng dụng hỗ trợ phân quyền giữa các vai trò với các tính năng riêng biệt: giáo viên, học sinh và quản trị viên.

## Tính năng chính

### Đối với Quản trị viên
- ✅ **Đăng nhập hệ thống** - Xác thực với tài khoản quản trị
- ✅ **Quản lý người dùng** - Xem, thêm, sửa thông tin giáo viên và học sinh
- ✅ **Quản lý môn học** - Tạo, cập nhật danh sách môn học
- ✅ **Xem báo cáo tổng thể** - Thống kê điểm số toàn trường
- ✅ **Sao lưu dữ liệu** - Xuất dữ liệu học sinh và điểm số
- ✅ **Xuất báo cáo** - Xuất báo cáo dưới dạng PDF, Excel, CSV
- ✅ **Đổi mật khẩu** - Cập nhật mật khẩu cá nhân
- ✅ **Đăng xuất** - Kết thúc phiên làm việc

### Đối với Giáo viên
- ✅ **Đăng nhập hệ thống** - Xác thực với tài khoản giáo viên
- ✅ **Quản lý học sinh** - Xem danh sách học sinh
- ✅ **Đăng ký học sinh** - Tạo tài khoản mới cho học sinh
- ✅ **Quản lý điểm số** - Nhập, chỉnh sửa điểm cho học sinh
- ✅ **Xem báo cáo** - Thống kê điểm số theo lớp/môn học
- ✅ **Tìm kiếm** - Tìm kiếm học sinh và điểm số
- ✅ **Xuất báo cáo** - Xuất báo cáo dưới dạng PDF, Excel, CSV
- ✅ **Đổi mật khẩu** - Cập nhật mật khẩu cá nhân
- ✅ **Đăng xuất** - Kết thúc phiên làm việc

### Đối với Học sinh
- ✅ **Đăng nhập hệ thống** - Xác thực với tài khoản học sinh
- ✅ **Xem điểm của mình** - Tra cứu bảng điểm cá nhân
- ✅ **Xem báo cáo** - Thống kê điểm số cá nhân
- ✅ **Tìm kiếm** - Tìm kiếm điểm số theo môn học
- ✅ **Đổi mật khẩu** - Cập nhật mật khẩu cá nhân
- ✅ **Đăng xuất** - Kết thúc phiên làm việc

## Kiến trúc ứng dụng

### Công nghệ sử dụng
- **Ngôn ngữ lập trình**: Java
- **Cơ sở dữ liệu**: SQLite
- **Giao diện người dùng**: XML Layout + Material Design Components
- **Kiến trúc**: MVC đơn giản
- **Thư viện hỗ trợ**: Material Components, GridLayout, OpenCSV, iText7

### Cấu trúc cơ sở dữ liệu
1. **users** - Lưu thông tin người dùng (quản trị, giáo viên và học sinh)
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
│   ├── Score.java
├── database/         # Database helper và manager
│   ├── DatabaseHelper.java
│   └── DatabaseManager.java
├── activities/       # Các activity chính
│   ├── LoginActivity.java
│   ├── AdminMainActivity.java
│   ├── TeacherMainActivity.java
│   ├── StudentMainActivity.java
│   ├── ManageStudentsActivity.java
│   ├── ManageScoresActivity.java
│   ├── ManageSubjectsActivity.java
│   ├── ManageUsersActivity.java
│   ├── ViewScoresActivity.java
│   ├── AddStudentActivity.java
│   ├── AddScoreActivity.java
│   ├── ChangePasswordActivity.java
│   ├── StatisticsActivity.java
│   ├── ExportReportActivity.java
│   └── BackupImportActivity.java
├── adapters/         # RecyclerView adapters
│   ├── StudentAdapter.java
│   └── ScoreAdapter.java
└── utils/            # Utility classes
    ├── SessionManager.java
    ├── PasswordUtil.java
    ├── ErrorHandler.java
    ├── NotificationUtil.java
    └── Constants.java
```

## Tính năng đặc biệt

### 1. Phân quyền người dùng
- **Quản trị (Role = ADMIN)**: Quản lý người dùng, môn học, xem báo cáo tổng thể
- **Giáo viên (Role = TEACHER)**: Quản lý học sinh, nhập điểm, xem báo cáo
- **Học sinh (Role = STUDENT)**: Xem điểm cá nhân, không thể truy cập tính năng giáo viên

### 2. Đăng ký học sinh
- Chỉ giáo viên mới có thể đăng ký tài khoản cho học sinh mới
- Học sinh không thể tự đăng ký

### 3. Đổi mật khẩu
- Cả ba loại vai trò đều có thể đổi mật khẩu của mình
- Yêu cầu xác nhận mật khẩu hiện tại trước khi đổi
- Mật khẩu mới phải đáp ứng các tiêu chí bảo mật: ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số

### 4. Quản lý điểm linh hoạt
- Hỗ trợ nhiều loại điểm: điểm miệng, 15 phút, 1 tiết, thi học kỳ
- Tính điểm trung bình tự động

### 5. Bảo mật nâng cao
- Mật khẩu được mã hóa bằng thuật toán SHA-256 với salt ngẫu nhiên
- Hạn chế đăng nhập thất bại
- Xác thực người dùng được cải thiện

### 6. Giao diện người dùng hiện đại
- Giao diện Material Design với các thành phần tương tác thân thiện
- Hỗ trợ tìm kiếm học sinh và điểm số
- Giao diện phản hồi trên nhiều kích thước màn hình

### 7. Xuất dữ liệu
- Hỗ trợ xuất dữ liệu ra các định dạng: PDF, Excel, CSV
- Chức năng sao lưu dữ liệu học sinh và điểm số

### 8. Xử lý lỗi và phản hồi người dùng
- Hệ thống xử lý lỗi toàn ứng dụng
- Phản hồi người dùng bằng Snackbar và Toast
- Xác nhận đầu vào được cải thiện

### 9. Thông báo hệ thống
- Hỗ trợ hệ thống thông báo cục bộ cho các sự kiện quan trọng
- Tự động tạo kênh thông báo trên Android O trở lên

## Cài đặt

1. Clone hoặc tải mã nguồn về
2. Mở dự án trong Android Studio
3. Đồng bộ Gradle
4. Cho phép quyền lưu trữ (nếu được hỏi) để sử dụng tính năng xuất dữ liệu
5. Build và chạy ứng dụng

## Tên đăng nhập mặc định

### Quản trị viên
- **Tên đăng nhập**: `admin1`
- **Mật khẩu**: `123456`

### Giáo viên
- **Tên đăng nhập**: `teacher1`
- **Mật khẩu**: `123456`

### Học sinh
- **Tên đăng nhập**: `student1`
- **Mật khẩu**: `123456`
- **Tên đăng nhập 2**: `student2`
- **Mật khẩu 2**: `123456`

## Hướng dẫn sử dụng

### Đối với quản trị viên:
1. Đăng nhập với tài khoản quản trị
2. Sử dụng các nút chức năng để:
   - **Quản lý người dùng**: Xem, thêm, sửa thông tin người dùng
   - **Quản lý môn học**: Tạo, cập nhật danh sách môn học
   - **Xem báo cáo**: Xem thống kê điểm số toàn trường
   - **Sao lưu & Nhập**: Xuất dữ liệu hoặc nhập dữ liệu từ file
   - **Xuất báo cáo**: Xuất báo cáo dưới các định dạng khác nhau
   - **Đổi mật khẩu**: Thay đổi mật khẩu cá nhân

### Đối với giáo viên:
1. Đăng nhập với tài khoản giáo viên
2. Sử dụng các nút chức năng để:
   - **Quản lý học sinh**: Xem, thêm, sửa thông tin học sinh
   - **Đăng ký học sinh**: Tạo tài khoản mới cho học sinh
   - **Quản lý điểm**: Nhập, chỉnh sửa điểm số
   - **Xem báo cáo**: Xem thống kê điểm số
   - **Sao lưu & Nhập**: Xuất dữ liệu hoặc nhập dữ liệu từ file
   - **Xuất báo cáo**: Xuất báo cáo dưới các định dạng khác nhau
   - **Đổi mật khẩu**: Thay đổi mật khẩu cá nhân

### Đối với học sinh:
1. Đăng nhập với tài khoản học sinh
2. Sử dụng các nút chức năng để:
   - **Xem điểm của tôi**: Tra cứu bảng điểm cá nhân
   - **Xem báo cáo**: Xem thống kê điểm số cá nhân
   - **Đổi mật khẩu**: Thay đổi mật khẩu cá nhân

## Bảo trì

Dự án này được xây dựng phục vụ mục đích học tập với các tiêu chí:
- Đơn giản, dễ hiểu
- Tính năng đầy đủ
- Tuân thủ quy tắc lập trình Android
- Không sử dụng công nghệ quá phức tạp

## Giấy phép

Dự án học tập - được sử dụng cho môn Lập trình Mobile.