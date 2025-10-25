# QL_DIEM - Android Student Management System

## Project Overview

This is a comprehensive Android application for managing students, classes, subjects, grades, and academic events. The application, named "QLDIEM" (Student Management), is built in Java with SQLite local database and follows the MVC architecture pattern.

### Key Features
- **Authentication & Authorization**: Login/Register with 3-level role-based access (Admin, Lecturer, Student)
- **Student Management**: CRUD operations for students with image upload capability
- **Class Management**: Manage classes and academic majors
- **Subject Management**: Manage courses taught
- **Grade Management**: Advanced grade system with attendance (10%), mid-term (30%), and final exam (60%) scores, with automatic conversion to 4-point scale and letter grades
- **Event Calendar**: Academic event scheduling and management
- **Profile Management**: User profile viewing and password change functionality

### Architecture
- **Pattern**: MVC (Model-View-Controller)
- **Language**: Java
- **Database**: SQLite local database
- **UI**: Material Design with CardView, RecyclerView, and FAB
- **Security**: SHA-256 password hashing and session-based authentication

### Project Structure
```
QLDIEM/
├── app/src/main/java/com/example/qldiem/
│   ├── activities/           # Activity controllers (12 files)
│   ├── adapters/             # RecyclerView adapters (7 files)
│   ├── models/               # Data models (7 files)
│   ├── database/             # SQLite DatabaseHelper
│   └── utils/                # Utility classes (5 files)
├── app/src/main/res/
│   ├── layout/              # XML layouts (27 files)
│   ├── menu/                # Menu XML files
│   ├── drawable/            # Icon resources
│   └── values/              # String/color resources
```

## Building and Running

### Prerequisites
- Android Studio (latest version)
- JDK 11+
- Android SDK API 24+ (Android 7.0 minimum)
- Gradle 8+

### Build Commands
```bash
# Build the project
cd /home/khxnh/Documents/AndroidStudio/QLDIEM
./gradlew assembleDebug

# Build APK location
app/build/outputs/apk/debug/app-debug.apk
```

### Default Credentials
- **Admin User**:
  - Username: `admin`
  - Password: `admin123`
  - Role: Admin (full access)

## Key Implementation Details

### Database Schema
The application uses a SQLite database with 7 tables:
1. **LOP** - Class management (with lecturer-in-charge field)
2. **CHUYENNGANH** - Academic major management
3. **MONHOC** - Course management
4. **SINHVIEN** - Student management (with image support)
5. **taiKhoan** - User account management (with role-based access)
6. **DIEM** - Grade management (advanced system with 3 types of grades)
7. **EventCalendar** - Event scheduling

### Advanced Grade System
The grade system includes:
- Attendance score (10% of total)
- Mid-term score (30% of total)
- Final exam score (60% of total)
- Automatic calculation of overall grade
- Conversion to 4-point scale and letter grades (A, B+, B, etc.)
- Filtering by semester and academic year

### Security Features
- Passwords are hashed using SHA-256
- Input validation to prevent SQL injection
- Session management with automatic timeout
- Role-based access control with 3 levels (Admin, Lecturer, Student)

### Role-Based Access Control
- **Admin**: Full access to all features
- **Lecturer**: Manage students, courses, and grades
- **Student**: View personal grades and events only

## Development Conventions

### Coding Standards
- Clean Java code with Vietnamese comments
- Consistent naming conventions
- MVC architectural pattern
- Proper error handling and validation
- Material Design UI components

### Database Migration
The DatabaseHelper includes proper migration support with version 2, including:
- Addition of grade types (attendance, mid-term, final)
- Addition of semester and academic year fields
- Addition of lecturer-in-charge field to class table

### Resource Management
- String resources in Vietnamese
- Material design color scheme
- Responsive layouts with ConstraintLayout and RecyclerView
- Drawable resources for icons

## Key Files

### Critical Files
- `DatabaseHelper.java` - Core database implementation (777 lines)
- `Diem.java` - Advanced grade model with automatic calculations
- `DiemActivity.java` - Grade management interface
- `MainActivity.java` - Main dashboard
- `ProfileActivity.java` - User profile with password change
- `SessionManager.java` - Session management utility
- `ValidationUtils.java` - Input validation utilities

### Layout Files
- 27 XML layout files covering all activities, dialogs, and list items
- Material design components with CardView and RecyclerView
- Responsive design for different screen sizes

## Project Status
The project is marked as 100% complete with all features implemented and tested. It includes comprehensive documentation and is ready for deployment.

### Features Completed
- ✅ Authentication system with SHA-256 encryption
- ✅ Student management with image upload
- ✅ Class and major management
- ✅ Course management
- ✅ Advanced grade management system
- ✅ Event calendar
- ✅ Profile management with password change
- ✅ Role-based access control
- ✅ Database with proper migrations
- ✅ Material Design UI
- ✅ Security implementation

## Troubleshooting

### Common Issues
1. **Cannot resolve symbol 'R'**
   - Clean Project: Build > Clean Project
   - Rebuild Project: Build > Rebuild Project
   - Invalidate Caches: File > Invalidate Caches / Restart

2. **Layout file not found**
   - Check file paths and names
   - Verify XML syntax for errors

3. **Database errors**
   - Uninstall app and reinstall to reset database
   - Check DatabaseHelper for migration issues

4. **Permission issues for image selection**
   - Ensure READ_EXTERNAL_STORAGE permissions are granted
   - Handle runtime permissions for Android 6.0+