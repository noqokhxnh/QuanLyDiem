# 📚 Ứng Dụng Quản Lý Điểm Sinh Viên

## 📋 Mô Tả Tổng Quan

Ứng dụng Quản Lý Điểm Sinh Viên là một ứng dụng di động Android hiện đại, được thiết kế để quản lý thông tin sinh viên và điểm số một cách hiệu quả và trực quan. Ứng dụng cung cấp giao diện người dùng thân thiện với Material Design và hỗ trợ đầy đủ cho cả sinh viên và quản trị viên với các chức năng chuyên biệt.

## ✨ Tính Năng Chính

### 👨‍🎓 Đối Với Sinh Viên
- **🔐 Đăng nhập/Đăng ký**: Tạo tài khoản và đăng nhập bảo mật
- **📊 Xem điểm số**: Truy cập và theo dõi điểm của bản thân theo từng môn học với biểu đồ trực quan
- **👤 Quản lý hồ sơ**: Xem và cập nhật thông tin cá nhân
- **📅 Xem lịch học**: Theo dõi lịch học và thời khóa biểu chi tiết
- **🔔 Nhận thông báo**: Nhận thông báo quan trọng từ quản trị viên
- **📱 Giao diện thân thiện**: Thiết kế Material Design hiện đại và dễ sử dụng

### 👨‍💼 Đối Với Quản Trị Viên
- **🔑 Đăng nhập quản trị**: Hệ thống xác thực riêng cho quản trị viên
- **👥 Quản lý sinh viên**: 
  - Thêm, sửa, xóa thông tin sinh viên
  - Xem danh sách tất cả sinh viên
  - Tìm kiếm và lọc sinh viên
- **📈 Quản lý điểm số**:
  - Cập nhật điểm cho sinh viên theo môn học
  - Xem tổng quan điểm của tất cả sinh viên
  - Xuất báo cáo điểm số
- **📋 Quản lý lịch học**: Tạo và cập nhật lịch học, thời khóa biểu
- **📢 Gửi thông báo**: Gửi thông báo đến toàn bộ hoặc nhóm sinh viên cụ thể
- **📊 Dashboard**: Xem thống kê tổng quan và phân tích dữ liệu với biểu đồ

## 🛠️ Công Nghệ Sử Dụng

- **💻 Ngôn ngữ lập trình**: Java 11
- **📱 Nền tảng**: Android
- **🎯 SDK**: Android SDK (API level 36)
- **📲 Hệ điều hành tối thiểu**: Android 7.0 (API level 24)
- **🗄️ Cơ sở dữ liệu**: SQLite (local database)
- **📦 Thư viện chính**:
  - **Material Design Components 1.9.0**: Giao diện người dùng hiện đại
  - **RecyclerView 1.3.2**: Hiển thị danh sách dữ liệu hiệu quả
  - **CardView 1.0.0**: Thiết kế card layout đẹp mắt
  - **MPAndroidChart v3.1.0**: Biểu đồ thống kê và phân tích dữ liệu
  - **ConstraintLayout**: Layout linh hoạt và responsive

## 🚀 Cài Đặt Và Cấu Hình

### 📋 Yêu Cầu Hệ Thống
- **Android Studio**: Phiên bản mới nhất (khuyến nghị Flamingo trở lên)
- **JDK**: Java Development Kit 11 trở lên
- **Android SDK**: API level 36 với backward compatibility đến API 24
- **Gradle**: Kotlin DSL

### ⚡ Hướng Dẫn Cài Đặt
1. **Clone repository**:
   ```bash
   git clone https://github.com/noqokhxnh/QuanLyDiem.git
   cd QuanLyDiem
   ```

2. **Mở project trong Android Studio**:
   - Chọn `File` > `Open`
   - Navigate đến thư mục project và chọn

3. **Sync Gradle**:
   - Android Studio sẽ tự động sync Gradle
   - Nếu không, chọn `File` > `Sync Project with Gradle Files`

4. **Build project**:
   ```bash
   ./gradlew build
   ```

5. **Chạy ứng dụng**:
   - Kết nối thiết bị Android hoặc khởi động emulator
   - Nhấn `Run` (Shift+F10) hoặc sử dụng:
   ```bash
   ./gradlew installDebug
   ```

### ⚙️ Cấu Hình
- **Database**: Tự động tạo SQLite database khi khởi chạy lần đầu
- **Permissions**: Ứng dụng yêu cầu quyền INTERNET cho các tính năng tương lai
- **Theme**: Sử dụng Material Theme với hỗ trợ dark mode

## 📁 Cấu Trúc Dự Án

```
QuanLyDiem/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/studentscoremanager/
│   │   │   │   ├── 📱 Activities/
│   │   │   │   │   ├── LoginActivity.java                 # Đăng nhập sinh viên
│   │   │   │   │   ├── RegisterActivity.java              # Đăng ký sinh viên  
│   │   │   │   │   ├── StudentMainActivity.java           # Màn hình chính sinh viên
│   │   │   │   │   ├── StudentProfileActivity.java        # Hồ sơ sinh viên
│   │   │   │   │   └── ForgotPassword.java                # Quên mật khẩu
│   │   │   │   ├── 👨‍💼 Admin Activities/
│   │   │   │   │   ├── AdminLoginActivity.java            # Đăng nhập quản trị
│   │   │   │   │   ├── AdminRegisterActivity.java         # Đăng ký quản trị
│   │   │   │   │   ├── AdminDashboardActivity.java        # Dashboard quản trị
│   │   │   │   │   ├── AdminAddStudentActivity.java       # Thêm sinh viên
│   │   │   │   │   ├── AdminStudentListActivity.java      # Danh sách sinh viên
│   │   │   │   │   ├── AdminAddScoreActivity.java         # Thêm điểm số
│   │   │   │   │   ├── AdminAllScoresActivity.java        # Xem tất cả điểm số
│   │   │   │   │   ├── AdminScheduleActivity.java         # Quản lý lịch học
│   │   │   │   │   ├── AdminTimetableActivity.java        # Quản lý thời khóa biểu
│   │   │   │   │   ├── AdminNotificationActivity.java     # Gửi thông báo
│   │   │   │   │   └── AdminForgotPasswordActivity.java   # Quên mật khẩu admin
│   │   │   │   ├── 🧩 Fragments/
│   │   │   │   │   ├── HomeFragment.java                  # Trang chủ sinh viên
│   │   │   │   │   ├── ScoresFragment.java                # Điểm số sinh viên
│   │   │   │   │   ├── ProfileFragment.java               # Hồ sơ cá nhân
│   │   │   │   │   ├── ScheduleFragment.java              # Lịch học
│   │   │   │   │   ├── TimetableFragment.java             # Thời khóa biểu
│   │   │   │   │   ├── NotificationsFragment.java         # Thông báo
│   │   │   │   │   ├── StudentsFragment.java              # Danh sách sinh viên
│   │   │   │   │   └── Fragments.java                     # Fragment utilities
│   │   │   │   ├── 🔧 Adapters/
│   │   │   │   │   ├── ScoreAdapter.java                  # Adapter cho điểm số
│   │   │   │   │   ├── AdminAllScoresAdapter.java         # Adapter quản lý điểm
│   │   │   │   │   └── AdminStudentAdapter.java           # Adapter sinh viên admin
│   │   │   │   ├── 🗄️ Database/
│   │   │   │   │   └── DatabaseHelper.java                # SQLite database helper
│   │   │   │   └── 🛠️ Utilities/
│   │   │   │       └── SharedPreferencesHelper.java       # Quản lý preferences
│   │   │   ├── res/
│   │   │   │   ├── layout/ (35+ layout files)             # Giao diện XML
│   │   │   │   ├── values/                                # Colors, strings, styles
│   │   │   │   ├── drawable/                              # Icons và graphics
│   │   │   │   └── anim/                                  # Animation files
│   │   │   └── AndroidManifest.xml                        # App configuration
│   │   ├── test/                                          # Unit tests
│   │   └── androidTest/                                   # Integration tests
│   ├── build.gradle.kts                                   # Module build config
│   └── proguard-rules.pro                                 # ProGuard rules
├── gradle/                                                # Gradle wrapper
├── build.gradle.kts                                       # Project build config
├── settings.gradle.kts                                    # Gradle settings
└── README.md                                              # Documentation
```

## 📖 Hướng Dẫn Sử Dụng

### 👨‍🎓 Đối Với Sinh Viên

1. **📝 Đăng ký tài khoản**:
   - Mở ứng dụng và chọn "Đăng ký"
   - Nhập đầy đủ thông tin cá nhân (họ tên, email, mật khẩu)
   - Xác nhận đăng ký và đăng nhập

2. **🔐 Đăng nhập**:
   - Nhập username/email và mật khẩu
   - Hệ thống sẽ xác thực và chuyển đến dashboard

3. **📊 Theo dõi điểm số**:
   - Vào tab "Điểm số" để xem điểm theo môn học
   - Xem biểu đồ phân tích điểm số
   - Theo dõi xu hướng học tập

4. **👤 Quản lý hồ sơ**:
   - Cập nhật thông tin cá nhân trong tab "Hồ sơ"
   - Thay đổi mật khẩu và cài đặt bảo mật

5. **📅 Xem lịch học**:
   - Kiểm tra lịch học hàng ngày/tuần trong tab "Lịch học"
   - Xem chi tiết thời khóa biểu

### 👨‍💼 Đối Với Quản Trị Viên

1. **🔑 Đăng nhập quản trị**:
   - Chọn "Đăng nhập quản trị" từ màn hình chính
   - Sử dụng tài khoản admin để truy cập

2. **📊 Sử dụng Dashboard**:
   - Xem thống kê tổng quan số lượng sinh viên, điểm số
   - Theo dõi các chỉ số quan trọng qua biểu đồ

3. **👥 Quản lý sinh viên**:
   - Thêm sinh viên mới với đầy đủ thông tin
   - Chỉnh sửa, xóa thông tin sinh viên
   - Tìm kiếm và lọc danh sách sinh viên

4. **📈 Quản lý điểm số**:
   - Nhập điểm cho từng sinh viên theo môn học
   - Xem tổng quan điểm của tất cả sinh viên
   - Cập nhật và chỉnh sửa điểm khi cần thiết

5. **📢 Gửi thông báo**:
   - Soạn và gửi thông báo đến sinh viên
   - Quản lý lịch sử thông báo đã gửi

## 🧪 Testing & Build Commands

```bash
# Build project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK  
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumentation tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## 🎨 Design Guidelines

- **Material Design 3**: Tuân thủ nguyên tắc thiết kế Material Design mới nhất
- **Responsive UI**: Giao diện thích ứng với nhiều kích thước màn hình
- **Dark Mode**: Hỗ trợ theme tối/sáng tự động
- **Accessibility**: Thiết kế thân thiện với người khuyết tật
- **Performance**: Tối ưu hiệu suất và trải nghiệm người dùng

## 🔧 Phát Triển & Đóng Góp

### 📝 Code Style
- **Java 11** với Android SDK API 36
- **Naming Convention**: 
  - Packages: `lowercase`
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
- **Architecture**: MVC pattern với SQLite local storage
- **Error Handling**: Comprehensive try-catch với user feedback

### 🤝 Đóng Góp
1. Fork repository
2. Tạo feature branch: `git checkout -b feature/AmazingFeature`
3. Commit changes: `git commit -m 'Add some AmazingFeature'`
4. Push to branch: `git push origin feature/AmazingFeature`
5. Tạo Pull Request

## 📊 Thống Kê Dự Án

- **📁 Tổng số file Java**: 29 files
- **🎨 Layout XML files**: 35+ files
- **📱 Activities**: 13 activities
- **🧩 Fragments**: 7 fragments  
- **🔧 Adapters**: 3 adapters
- **📊 Biểu đồ**: MPAndroidChart integration
- **🗄️ Database**: SQLite với comprehensive schema

## 📄 Giấy Phép

Dự án này được phân phối dưới giấy phép **MIT License**. Xem file [LICENSE](LICENSE) để biết thêm chi tiết.

## 👥 Tác Giả

- **Developer**: [noqokhxnh](https://github.com/noqokhxnh)
- **Repository**: [QuanLyDiem](https://github.com/noqokhxnh/QuanLyDiem)

## 📞 Liên Hệ & Hỗ Trợ

Nếu bạn có bất kỳ câu hỏi hoặc cần hỗ trợ, vui lòng:
- Tạo [Issue](https://github.com/noqokhxnh/QuanLyDiem/issues) trên GitHub
- Fork repository và gửi Pull Request cho cải tiến

---

⭐ **Nếu project này hữu ích, hãy cho chúng tôi một star trên GitHub!** ⭐