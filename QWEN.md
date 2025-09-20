# Claude.md - Quy tắc Dự án Student Manager

## 📋 Thông tin Dự án
- **Tên dự án**: App Quản lý Điểm Học Sinh
- **Mục tiêu**: Ứng dụng Android quản lý điểm số cho giáo viên và học sinh
- **Loại dự án**: Đề tài môn Lập trình Mobile
- **Ưu tiên**: Đơn giản, dễ hiểu, tính năng đầy đủ

## 🛠️ Stack Công nghệ CHÍNH THỨC

### Bắt buộc sử dụng
- **Ngôn ngữ**: Java (không dùng Kotlin)
- **IDE**: Android Studio
- **Database**: SQLite với SQLiteOpenHelper
- **UI**: XML Layout (không dùng Jetpack Compose)
- **Kiến trúc**: MVC đơn giản
- **Lưu trữ session**: SharedPreferences

### Không được sử dụng
- ❌ Kotlin
- ❌ Jetpack Compose  
- ❌ Room Database
- ❌ Retrofit/API calls
- ❌ Firebase
- ❌ MVP/MVVM pattern phức tạp
- ❌ Dependency Injection frameworks

## 📊 Cấu trúc Database CHUẨN

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

## 🏗️ Cấu trúc Project BẮT BUỘC

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

## 👥 Phân quyền CHÍNH THỨC

### Role = 1: Giáo viên
**Được phép:**
- ✅ Xem danh sách tất cả học sinh
- ✅ Thêm/sửa/xóa học sinh
- ✅ Nhập điểm cho học sinh (tất cả loại điểm)
- ✅ Sửa/xóa điểm đã nhập
- ✅ Xem thống kê điểm theo lớp/môn
- ✅ Xuất báo cáo điểm
- ✅ Đổi mật khẩu

### Role = 0: Học sinh  
**Được phép:**
- ✅ Xem điểm của chính mình
- ✅ Xem điểm trung bình cá nhân
- ✅ Tra cứu lịch sử điểm
- ✅ Đổi mật khẩu

**Không được phép:**
- ❌ Xem điểm học sinh khác
- ❌ Thêm/sửa/xóa bất kỳ điểm nào
- ❌ Xem danh sách học sinh khác

## 📱 Tính năng BẮT BUỘC phải có

### Cốt lõi (Core Features)
1. **Đăng nhập/Đăng xuất**
2. **Phân quyền theo role**
3. **CRUD học sinh** (chỉ giáo viên)
4. **CRUD điểm số** (giáo viên nhập, học sinh xem)
5. **Tính điểm trung bình**

### Bổ sung (Nice to have)
- Thống kê biểu đồ
- Tìm kiếm/lọc dữ liệu  
- Xuất file báo cáo
- Thông báo điểm mới

## 🎯 Dữ liệu mẫu BẮT BUỘC

### Tài khoản test
```sql
-- Giáo viên
INSERT INTO users VALUES (1, 'teacher1', '123456', 1, 'Nguyễn Văn Giáo', datetime('now'));

-- Học sinh
INSERT INTO users VALUES (2, 'student1', '123456', 0, 'Trần Thị Học', datetime('now'));
INSERT INTO users VALUES (3, 'student2', '123456', 0, 'Lê Văn Sinh', datetime('now'));
```

### Môn học mẫu
```sql
INSERT INTO subjects VALUES 
(1, 'Toán', 'TOAN'),
(2, 'Lý', 'LY'), 
(3, 'Hóa', 'HOA'),
(4, 'Văn', 'VAN');
```

### Loại điểm chuẩn
- `mieng`: Điểm miệng
- `15phut`: Kiểm tra 15 phút  
- `1tiet`: Kiểm tra 1 tiết
- `hocky`: Điểm thi học kỳ

## 🔧 Quy tắc Code & Naming Convention

### Naming Convention BẮT BUỘC

#### Classes (PascalCase)
```java
// ✅ ĐÚNG
public class StudentAdapter extends RecyclerView.Adapter
public class DatabaseManager
public class LoginActivity extends AppCompatActivity

// ❌ SAI  
public class studentAdapter
public class database_manager
public class login_activity
```

#### Methods (camelCase)
```java
// ✅ ĐÚNG
public List<Student> getAllStudents()
public void authenticateUser(String username, String password)
public double calculateAverageScore()
public void showSuccessMessage()

// ❌ SAI
public List<Student> GetAllStudents()
public void authenticate_user()
public double calculate_average_score()
```

#### Variables (camelCase)
```java
// ✅ ĐÚNG
private EditText etUsername;
private RecyclerView rvStudentList;
private DatabaseManager dbManager;
private SessionManager sessionManager;
private List<Student> studentList;
private double averageScore;

// ❌ SAI
private EditText ET_USERNAME;
private RecyclerView rv_student_list;
private DatabaseManager db_manager;
```

#### Constants (UPPER_SNAKE_CASE)
```java
// ✅ ĐÚNG
public static final String DATABASE_NAME = "StudentManager.db";
public static final int DATABASE_VERSION = 1;
public static final String KEY_USER_ID = "userId";
public static final int ROLE_STUDENT = 0;
public static final int ROLE_TEACHER = 1;

// ❌ SAI
public static final String databaseName = "StudentManager.db";
public static final int DatabaseVersion = 1;
```

#### Database Fields (snake_case)
```java
// ✅ ĐÚNG - Tên cột database
"student_id", "full_name", "created_date", "score_type"

// ✅ ĐÚNG - Tên bảng  
"users", "students", "subjects", "scores"

// ❌ SAI
"studentId", "fullName", "createdDate"
"Users", "Students"
```

#### XML IDs (snake_case với prefix)
```xml
<!-- ✅ ĐÚNG -->
<EditText android:id="@+id/et_username" />
<Button android:id="@+id/btn_login" />  
<RecyclerView android:id="@+id/rv_student_list" />
<TextView android:id="@+id/tv_student_name" />

<!-- ❌ SAI -->
<EditText android:id="@+id/etUsername" />
<Button android:id="@+id/loginButton" />
<RecyclerView android:id="@+id/studentList" />
```

### Quy tắc đặt tên tiếng Anh/Việt

#### Ngôn ngữ trong Code (100% TIẾNG ANH)
```java
// ✅ ĐÚNG
public class Student {
    private String fullName;
    private String className;
    private String birthDate;
    
    public void calculateAverage() {}
    public boolean isPassingGrade() {}
}

// ❌ SAI - Không dùng tiếng Việt trong code
public class HocSinh {
    private String hoTen;
    private String lopHoc;
    
    public void tinhDiemTrungBinh() {}
}
```

#### String hiển thị (100% TIẾNG VIỆT)
```java
// ✅ ĐÚNG - strings.xml
<string name="app_name">Quản lý Điểm</string>
<string name="login_title">Đăng nhập</string>
<string name="username_hint">Tên đăng nhập</string>
<string name="password_hint">Mật khẩu</string>
<string name="login_success">Đăng nhập thành công</string>
<string name="login_failed">Tên đăng nhập hoặc mật khẩu không đúng</string>

// ✅ ĐÚNG - Toast messages
Toast.makeText(this, "Thêm học sinh thành công", Toast.LENGTH_SHORT).show();
Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();

// ❌ SAI - Không dùng tiếng Anh trong UI
Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
```

### Prefix quy chuẩn

#### View Components
```java
// EditText
EditText etUsername, etPassword, etFullName;

// TextView  
TextView tvStudentName, tvAverageScore, tvClassName;

// Button
Button btnLogin, btnSave, btnCancel, btnDelete;

// RecyclerView
RecyclerView rvStudentList, rvScoreList;

// Spinner
Spinner spSubjects, spScoreTypes;

// ImageView
ImageView ivProfile, ivBack, ivEdit;
```

#### Activity & Fragment Names
```java
// Activities (phải có Activity suffix)
LoginActivity.java
TeacherMainActivity.java  
StudentMainActivity.java
ManageStudentsActivity.java
AddScoreActivity.java

// Fragments (phải có Fragment suffix)  
StudentListFragment.java
ScoreDetailFragment.java
StatisticsFragment.java

// Adapters (phải có Adapter suffix)
StudentAdapter.java
ScoreAdapter.java
SubjectSpinnerAdapter.java
```

### Database Naming Rules

#### Table Names (snake_case, số nhiều)
```sql
-- ✅ ĐÚNG
CREATE TABLE users (...);
CREATE TABLE students (...);
CREATE TABLE subjects (...);
CREATE TABLE scores (...);

-- ❌ SAI
CREATE TABLE User (...);
CREATE TABLE student (...);
CREATE TABLE Subject (...);
```

#### Column Names (snake_case)
```sql
-- ✅ ĐÚNG  
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    full_name TEXT NOT NULL,
    created_date TEXT DEFAULT CURRENT_TIMESTAMP
);

-- ❌ SAI
CREATE TABLE users (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    userName TEXT UNIQUE NOT NULL,
    fullName TEXT NOT NULL,
    createdDate TEXT
);
```

### Comment Rules

#### Class Comments (Tiếng Việt)
```java
/**
 * Adapter để hiển thị danh sách học sinh trong RecyclerView
 * Hỗ trợ click listener để chỉnh sửa thông tin học sinh
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
}
```

#### Method Comments (Tiếng Việt)
```java
/**
 * Xác thực thông tin đăng nhập của người dùng
 * @param username tên đăng nhập
 * @param password mật khẩu  
 * @return User object nếu đúng, null nếu sai
 */
public User authenticateUser(String username, String password) {
}
```

#### Inline Comments (Tiếng Việt)
```java
// Kiểm tra quyền truy cập của user
if (sessionManager.getUserRole() == Constants.ROLE_TEACHER) {
    // Chỉ giáo viên mới được thêm điểm
    showAddScoreDialog();
} else {
    // Học sinh chỉ được xem điểm
    Toast.makeText(this, "Bạn không có quyền thực hiện thao tác này", Toast.LENGTH_SHORT).show();
}
```

### Package Structure với Naming
```
com.yourname.studentmanager/
├── models/           // Tất cả Model classes
├── database/         // Database helper và manager
├── activities/       // Tất cả Activity classes  
├── fragments/        // Tất cả Fragment classes
├── adapters/         // Tất cả Adapter classes
├── utils/           // Utility classes
└── constants/       // Constants classes
```

### File Naming Convention

#### Java Files
```
LoginActivity.java          // Activity
StudentAdapter.java         // Adapter  
DatabaseHelper.java         // Helper class
SessionManager.java         // Manager class
Constants.java              // Constants
Student.java               // Model
```

#### XML Files  
```
activity_login.xml          // Activity layout
fragment_student_list.xml   // Fragment layout
item_student.xml           // RecyclerView item
dialog_add_score.xml       // Dialog layout
```

### Database Rules
- Luôn mở `dbManager.open()` trước khi query
- Luôn đóng `dbManager.close()` sau khi xong
- Kiểm tra `cursor != null` trước khi sử dụng
- Đóng cursor sau khi sử dụng: `cursor.close()`

### Activity Rules  
- Mỗi Activity có 1 mục đích rõ ràng
- Sử dụng `startActivityForResult()` khi cần trả kết quả
- Kiểm tra `sessionManager.isLoggedIn()` trong `onCreate()`
- Redirect về Login nếu chưa đăng nhập

### Error Handling
```java
// Bắt buộc có try-catch cho database operations
try {
    dbManager.open();
    // database operations
} catch (SQLException e) {
    Toast.makeText(this, "Lỗi cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
    Log.e("DatabaseError", e.getMessage());
} finally {
    dbManager.close();
}
```

## 🚫 Những điều KHÔNG ĐƯỢC làm

### Security
- ❌ Lưu password dạng plain text (phải mã hóa)
- ❌ Để học sinh truy cập được chức năng giáo viên
- ❌ SQL Injection (dùng parameterized queries)

### Performance  
- ❌ Query database trên Main Thread quá lâu
- ❌ Không đóng cursor sau khi dùng
- ❌ Tạo quá nhiều object trong Adapter

### UX/UI
- ❌ Không có loading indicator cho thao tác lâu
- ❌ Không validate input trước khi lưu
- ❌ Không có confirmation dialog khi xóa

## 📋 Checklist hoàn thành

### Database ✅
- [ ] Tạo DatabaseHelper với 4 bảng
- [ ] Implement DatabaseManager với CRUD methods
- [ ] Có dữ liệu mẫu để test
- [ ] Test tất cả query hoạt động đúng

### Authentication ✅  
- [ ] Login screen hoạt động
- [ ] SessionManager lưu thông tin user
- [ ] Phân quyền theo role chính xác
- [ ] Logout và clear session

### Teacher Features ✅
- [ ] Xem danh sách học sinh
- [ ] Thêm/sửa học sinh
- [ ] Nhập điểm cho học sinh  
- [ ] Xem thống kê cơ bản

### Student Features ✅
- [ ] Xem điểm cá nhân
- [ ] Tính điểm trung bình
- [ ] Không truy cập được chức năng giáo viên

### UI/UX ✅
- [ ] Giao diện trực quan, dễ sử dụng
- [ ] Toast message thông báo lỗi/thành công
- [ ] Loading indicator cho thao tác database
- [ ] Confirmation dialog khi xóa

## 🎯 Mục tiêu cuối kỳ

### Điểm tốt (7-8)
- Hoàn thành tất cả tính năng cốt lõi
- Code sạch, có comment
- Database hoạt động ổn định
- UI cơ bản đẹp

### Điểm khá/giỏi (8-10)  
- Thêm tính năng thống kê biểu đồ
- Tìm kiếm/lọc dữ liệu
- UI/UX đẹp, smooth
- Handle edge cases tốt
- Code architecture tốt

---

## 📞 Support & Debug

### Khi gặp lỗi:
1. Check Logcat để xem stack trace
2. Kiểm tra database có tạo đúng không
3. Verify user role và session
4. Test trên real device

### Resources:
- [Android Developer Guide](https://developer.android.com)
- [SQLite Tutorial](https://www.sqlitetutorial.net)
- Stack Overflow cho specific issues

---

**Lưu ý cuối**: Đây là dự án học tập, ưu tiên sự đơn giản và dễ hiểu hơn là công nghệ mới nhất!
