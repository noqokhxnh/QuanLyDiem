# Student Manager App

## Overview
This is an Android application for managing student grades, with separate roles for teachers and students. The app allows teachers to manage students and enter grades, while students can view their own grades and averages.

## Features
- User authentication with role-based access (teacher/student)
- Teacher features:
  - Manage students (add, edit, delete)
  - Manage scores (add, edit, delete)
- Student features:
  - View personal scores
  - View average scores
- SQLite database for local data storage
- Clean, intuitive user interface

## Technologies Used
- Java
- SQLite database
- Android SDK
- Material Design Components

## Project Structure
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

## Database Schema
The app uses SQLite with the following tables:
1. `users` - Stores user information (teachers and students)
2. `students` - Stores student-specific information
3. `subjects` - Stores subject information
4. `scores` - Stores score records

## Sample Data
The app comes with sample data for testing:
- Teacher account: username `teacher1`, password `123456`
- Student accounts: 
  - username `student1`, password `123456`
  - username `student2`, password `123456`

## How to Build
1. Clone the repository
2. Open in Android Studio
3. Build the project using `./gradlew assembleDebug`

## How to Run
1. Build the project
2. Install the APK on an Android device or emulator
3. Use the sample accounts to log in and test the features

## Future Improvements
- Add data validation and error handling
- Implement password encryption
- Add search and filtering capabilities
- Implement statistics and reporting features
- Add offline capabilities
