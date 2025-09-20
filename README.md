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

## Prerequisites
Before you begin, ensure you have the following installed:
- Android Studio (latest version recommended)
- Git
- Android SDK (API level 21 or higher)

## How to Clone the Project

### Method 1: Using Android Studio (Recommended)
1. Open Android Studio
2. Select "Get from VCS" or "Get from Version Control"
3. Enter the repository URL: `https://github.com/your-username/QLD.git` (replace with actual repository URL)
4. Choose the directory where you want to clone the project
5. Click "Clone"

### Method 2: Using Command Line
1. Open your terminal/command prompt
2. Navigate to the directory where you want to clone the project:
   ```bash
   cd /path/to/your/desired/directory
   ```
3. Clone the repository:
   ```bash
   git clone https://github.com/your-username/QLD.git
   ```
4. Open Android Studio
5. Select "Open an existing Android Studio project"
6. Navigate to the cloned project directory and select it

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

## How to Build and Run

### Using Android Studio (Recommended)
1. Open the project in Android Studio
2. Wait for Gradle to sync (this may take a few minutes)
3. Connect an Android device or start an emulator
4. Click the "Run" button (green triangle) or press `Shift + F10`

### Using Command Line
1. Open your terminal/command prompt
2. Navigate to the project directory:
   ```bash
   cd /path/to/QLD
   ```
3. Build the project:
   ```bash
   ./gradlew assembleDebug
   ```
4. Install the APK on an Android device or emulator

## Making Changes to the Project

### 1. Create a New Branch (Recommended)
Before making any changes, create a new branch:
```bash
git checkout -b feature/your-feature-name
```

### 2. Modify the Code
- Open the project in Android Studio
- Make your desired changes to the code
- Test your changes thoroughly

### 3. Commit Your Changes
After making changes:

1. Stage your changes:
   ```bash
   git add .
   ```
   Or to add specific files:
   ```bash
   git add path/to/your/file.java
   ```

2. Commit your changes with a descriptive message:
   ```bash
   git commit -m "Add feature: brief description of what you did"
   ```

### 4. Push Changes to GitHub
Push your changes to GitHub:
```bash
git push origin feature/your-feature-name
```

### 5. Create a Pull Request (if working with others)
1. Go to your repository on GitHub
2. Click "Compare & pull request"
3. Add a title and description for your changes
4. Click "Create pull request"

## Common Development Tasks

### Adding a New Feature
1. Create a new branch for your feature
2. Implement your feature
3. Test thoroughly
4. Commit and push your changes
5. Create a pull request if needed

### Fixing a Bug
1. Create a new branch for your bug fix
2. Implement the fix
3. Test to ensure the bug is resolved
4. Commit and push your changes
5. Create a pull request if needed

### Updating Dependencies
1. Open the `build.gradle` file (Module: app)
2. Modify the dependencies section as needed
3. Sync the project with Gradle files

## Troubleshooting

### Gradle Sync Issues
If you encounter Gradle sync issues:
1. Try "File" → "Sync Project with Gradle Files"
2. If that doesn't work, try "File" → "Invalidate Caches and Restart"

### Database Issues
If you encounter database issues:
1. Uninstall the app from your device/emulator
2. Clean the project: "Build" → "Clean Project"
3. Rebuild the project: "Build" → "Rebuild Project"

### Emulator Issues
If the emulator is not working:
1. Try creating a new AVD (Android Virtual Device)
2. Ensure you have enough system resources allocated
3. Try using a physical device instead

## Contributing
1. Fork the repository
2. Create a new branch for your feature or bug fix
3. Make your changes
4. Test thoroughly
5. Commit your changes with a descriptive message
6. Push to your fork
7. Create a pull request

## Future Improvements
- Add data validation and error handling
- Implement password encryption
- Add search and filtering capabilities
- Implement statistics and reporting features
- Add offline capabilities

## License
This project is for educational purposes and does not have a specific license. Please check with your institution for usage guidelines.