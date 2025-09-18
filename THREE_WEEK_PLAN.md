# Student Manager App - 3-Week Development Plan

## Project Overview
This is a mobile application for managing student grades, with separate roles for teachers and students. Teachers can manage students and enter grades, while students can view their own grades and averages.

## Week 1: Foundation and Core Components

### Day 1-2: Project Setup and Environment Configuration
- Verify Android Studio installation
- Check project structure matches requirements
- Update build.gradle files if needed
- Set up emulator or connect physical device for testing
- Create project documentation and checklist

### Day 3-4: Database Implementation
- Create DatabaseHelper.java with all 4 required tables:
  - users table (id, username, password, role, full_name, created_date)
  - students table (id, user_id, student_code, class_name, birth_date)
  - subjects table (id, subject_name, subject_code)
  - scores table (id, student_id, subject_id, score_type, score, date_created, teacher_id)
- Implement DatabaseManager.java with CRUD operations for all entities
- Add proper error handling and cursor management

### Day 5-7: Authentication System
- Create models: User.java, Student.java, Subject.java, Score.java
- Implement SessionManager.java for SharedPreferences handling
- Create LoginActivity.java with proper UI
- Implement login validation and role-based redirection
- Create Constants.java for application constants
- Add password encryption (not plain text as prohibited)

## Week 2: Teacher Functionality Implementation

### Day 8-9: Teacher Main Interface
- Create TeacherMainActivity.java
- Design dashboard with navigation to student management and score management
- Implement navigation between activities

### Day 10-12: Student Management (CRUD)
- Create ManageStudentsActivity.java
- Implement student listing with RecyclerView
- Create StudentAdapter.java for displaying students
- Add functionality to add new students
- Implement student editing capabilities
- Add student deletion with confirmation dialog

### Day 13-14: Score Management
- Create ManageScoresActivity.java
- Implement score entry interface with proper form validation
- Create score listing functionality
- Add score editing and deletion capabilities
- Implement all score types (mieng, 15phut, 1tiet, hocky)

## Week 3: Student Features and Polish

### Day 15-16: Student Interface
- Create StudentMainActivity.java
- Design student dashboard for viewing personal scores
- Create ViewScoresActivity.java for detailed score viewing
- Implement score calculation and averaging functionality

### Day 17-18: Data Population and Testing
- Add sample data for testing (teacher and student accounts)
- Populate subjects table with sample data
- Test all CRUD operations
- Verify role-based access control
- Test edge cases and error handling

### Day 19-21: UI Polish and Finalization
- Improve UI/UX design with Material Design components
- Add proper Toast messages for user feedback
- Implement loading indicators for database operations
- Add confirmation dialogs for deletion operations
- Perform final testing on all features
- Prepare project documentation and user guide

## Daily Development Process
1. Morning (3 hours): Implementation of core features
2. Afternoon (2 hours): Testing and bug fixing
3. Evening (1 hour): Documentation and code cleanup

## Key Milestones

### End of Week 1:
- ✅ Functional database with all tables
- ✅ Working authentication system with role-based login
- ✅ Basic project structure in place

### End of Week 2:
- ✅ Complete teacher functionality (student and score management)
- ✅ All CRUD operations implemented and tested
- ✅ Role-based access control verified

### End of Week 3:
- ✅ Complete student functionality (view scores, calculate averages)
- ✅ Polished UI with proper user feedback
- ✅ Fully tested application ready for submission

## Risk Mitigation
- Daily Git commits to prevent code loss
- Regular testing on both emulator and physical device
- Early identification of blockers and seeking help
- Maintaining checklist to track progress
- Backup plan for complex features (simplify if needed)

## Success Criteria
- All mandatory features implemented as per QWEN.md
- Clean, well-commented code following naming conventions
- Proper error handling and user feedback
- No security vulnerabilities (especially password handling)
- Smooth user experience with intuitive navigation