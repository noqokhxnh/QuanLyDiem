# Ứng dụng Quản lý Học sinh

## Tổng quan
Đây là một ứng dụng Android để quản lý điểm số của học sinh, với các vai trò riêng biệt cho giáo viên và học sinh. Ứng dụng cho phép giáo viên quản lý học sinh và nhập điểm, trong khi học sinh có thể xem điểm và điểm trung bình của mình.

## Tính năng
- Xác thực người dùng với quyền truy cập dựa trên vai trò (giáo viên/học sinh)
- Tính năng dành cho giáo viên:
  - Quản lý học sinh (thêm, sửa, xóa)
  - Quản lý điểm (thêm, sửa, xóa)
- Tính năng dành cho học sinh:
  - Xem điểm cá nhân
  - Xem điểm trung bình
- Cơ sở dữ liệu SQLite để lưu trữ dữ liệu cục bộ
- Giao diện người dùng sạch sẽ, trực quan

## Công nghệ sử dụng
- Java
- Cơ sở dữ liệu SQLite
- Android SDK
- Thành phần Thiết kế Material

## Điều kiện tiên quyết
Trước khi bắt đầu, hãy đảm bảo bạn đã cài đặt:
- Android Studio (khuyến nghị phiên bản mới nhất)
- Git
- Android SDK (API level 21 trở lên)

## Cách sao chép dự án

### Phương pháp 1: Sử dụng Android Studio (Khuyến nghị)
1. Mở Android Studio
2. Chọn "Get from VCS" hoặc "Get from Version Control"
3. Nhập URL repository: `https://github.com/your-username/QLD.git` (thay thế bằng URL thực tế)
4. Chọn thư mục nơi bạn muốn sao chép dự án
5. Nhấp "Clone"

### Phương pháp 2: Sử dụng dòng lệnh
1. Mở terminal/dòng lệnh
2. Điều hướng đến thư mục nơi bạn muốn sao chép dự án:
   ```bash
   cd /đường/dẫn/đến/thư/mục/mong/muốn
   ```
3. Sao chép repository:
   ```bash
   git clone https://github.com/your-username/QLD.git
   ```
4. Mở Android Studio
5. Chọn "Open an existing Android Studio project"
6. Điều hướng đến thư mục dự án đã sao chép và chọn nó

## Cấu trúc dự án
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
│   ├── DatabaseHelper.java
│   └── DatabaseManager.java
├── models/
│   ├── User.java
│   ├── Student.java
│   ├── Subject.java
│   └── Score.java
└── utils/
    ├── SessionManager.java
    └── Constants.java
```

## Lược đồ cơ sở dữ liệu
Ứng dụng sử dụng SQLite với các bảng sau:
1. `users` - Lưu trữ thông tin người dùng (giáo viên và học sinh)
2. `students` - Lưu trữ thông tin cụ thể của học sinh
3. `subjects` - Lưu trữ thông tin môn học
4. `scores` - Lưu trữ bản ghi điểm

## Dữ liệu mẫu
Ứng dụng đi kèm với dữ liệu mẫu để kiểm thử:
- Tài khoản giáo viên: tên đăng nhập `teacher1`, mật khẩu `123456`
- Tài khoản học sinh: 
  - tên đăng nhập `student1`, mật khẩu `123456`
  - tên đăng nhập `student2`, mật khẩu `123456`

## Cách xây dựng và chạy

### Sử dụng Android Studio (Khuyến nghị)
1. Mở dự án trong Android Studio
2. Chờ Gradle đồng bộ (có thể mất vài phút)
3. Kết nối thiết bị Android hoặc khởi động trình giả lập
4. Nhấp nút "Run" (tam giác xanh) hoặc nhấn `Shift + F10`

### Sử dụng dòng lệnh
1. Mở terminal/dòng lệnh
2. Điều hướng đến thư mục dự án:
   ```bash
   cd /đường/dẫn/đến/QLD
   ```
3. Xây dựng dự án:
   ```bash
   ./gradlew assembleDebug
   ```
4. Cài đặt APK trên thiết bị Android hoặc trình giả lập

## Thực hiện thay đổi trong dự án

### 1. Tạo nhánh mới (Khuyến nghị)
Trước khi thực hiện bất kỳ thay đổi nào, hãy tạo nhánh mới:
```bash
git checkout -b feature/tên-tính-năng-của-bạn
```

### 2. Sửa đổi mã
- Mở dự án trong Android Studio
- Thực hiện các thay đổi mong muốn trong mã
- Kiểm tra kỹ các thay đổi của bạn

### 3. Cam kết thay đổi của bạn
Sau khi thực hiện thay đổi:

1. Chuẩn bị các thay đổi:
   ```bash
   git add .
   ```
   Hoặc để thêm các tệp cụ thể:
   ```bash
   git add đường/dẫn/đến/tệp.java
   ```

2. Cam kết các thay đổi với thông báo mô tả:
   ```bash
   git commit -m "Thêm tính năng: mô tả ngắn gọn những gì bạn đã làm"
   ```

### 4. Đẩy thay đổi lên GitHub
Đẩy các thay đổi của bạn lên GitHub:
```bash
git push origin feature/tên-tính-năng-của-bạn
```

### 5. Tạo Pull Request (nếu làm việc với người khác)
1. Truy cập repository của bạn trên GitHub
2. Nhấp "Compare & pull request"
3. Thêm tiêu đề và mô tả cho các thay đổi của bạn
4. Nhấp "Create pull request"

## Các tác vụ phát triển phổ biến

### Thêm tính năng mới
1. Tạo nhánh mới cho tính năng của bạn
2. Triển khai tính năng
3. Kiểm tra kỹ lưỡng
4. Cam kết và đẩy các thay đổi
5. Tạo pull request nếu cần

### Sửa lỗi
1. Tạo nhánh mới cho bản sửa lỗi
2. Triển khai bản sửa
3. Kiểm tra để đảm bảo lỗi đã được giải quyết
4. Cam kết và đẩy các thay đổi
5. Tạo pull request nếu cần

### Cập nhật phụ thuộc
1. Mở tệp `build.gradle` (Module: app)
2. Sửa đổi phần phụ thuộc nếu cần
3. Đồng bộ dự án với tệp Gradle

## Khắc phục sự cố

### Vấn đề đồng bộ Gradle
Nếu bạn gặp vấn đề đồng bộ Gradle:
1. Thử "File" → "Sync Project with Gradle Files"
2. Nếu không hiệu quả, thử "File" → "Invalidate Caches and Restart"

### Vấn đề cơ sở dữ liệu
Nếu bạn gặp vấn đề cơ sở dữ liệu:
1. Gỡ cài đặt ứng dụng khỏi thiết bị/trình giả lập của bạn
2. Dọn dẹp dự án: "Build" → "Clean Project"
3. Xây dựng lại dự án: "Build" → "Rebuild Project"

### Vấn đề trình giả lập
Nếu trình giả lập không hoạt động:
1. Thử tạo AVD mới (Android Virtual Device)
2. Đảm bảo bạn có đủ tài nguyên hệ thống được phân bổ
3. Thử sử dụng thiết bị thực tế thay thế

## Đóng góp
1. Fork repository
2. Tạo nhánh mới cho tính năng hoặc bản sửa lỗi của bạn
3. Thực hiện các thay đổi
4. Kiểm tra kỹ lưỡng
5. Cam kết các thay đổi với thông báo mô tả
6. Đẩy lên fork của bạn
7. Tạo pull request

## Cải tiến trong tương lai
- Thêm xác thực dữ liệu và xử lý lỗi
- Triển khai mã hóa mật khẩu
- Thêm khả năng tìm kiếm và lọc
- Triển khai thống kê và báo cáo
- Thêm khả năng ngoại tuyến

## Giấy phép
Dự án này dành cho mục đích giáo dục và không có giấy phép cụ thể. Vui lòng kiểm tra với tổ chức của bạn để biết hướng dẫn sử dụng.