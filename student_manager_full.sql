-- SQL Script to create the Student Manager Database
-- This script creates the database, all tables, and inserts sample data

-- Create and use the database
DROP DATABASE IF EXISTS student_manager;
CREATE DATABASE student_manager;
USE student_manager;

-- Drop existing tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS scores;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create students table
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    student_code VARCHAR(50) UNIQUE NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    birth_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create subjects table
CREATE TABLE subjects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) NOT NULL,
    subject_code VARCHAR(50) UNIQUE NOT NULL
);

-- Create scores table
CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    subject_id INT,
    score_type VARCHAR(20) NOT NULL,
    score DECIMAL(3,1) NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    teacher_id INT,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert sample users (teachers and students)
INSERT INTO users (id, username, password, role, full_name, created_date) VALUES 
(1, 'tc1', '123456', 1, 'Nguyễn Văn Giáo', NOW()),
(2, 'st1', '123456', 0, 'Trần Thị Học', NOW()),
(3, 'st2', '123456', 0, 'Lê Văn Sinh', NOW());

-- Insert sample students
INSERT INTO students (id, user_id, student_code, class_name, birth_date) VALUES 
(1, 2, 'STU001', 'Class 10A1', '2005-05-15'),
(2, 3, 'STU002', 'Class 10A1', '2005-08-22');

-- Insert sample subjects
INSERT INTO subjects (id, subject_name, subject_code) VALUES 
(1, 'Toán', 'TOAN'),
(2, 'Lý', 'LY'),
(3, 'Hóa', 'HOA'),
(4, 'Văn', 'VAN');

-- Insert sample scores
INSERT INTO scores (id, student_id, subject_id, score_type, score, date_created, teacher_id) VALUES 
(1, 1, 1, 'mieng', 8.5, NOW(), 1),
(2, 1, 2, '15phut', 7.0, NOW(), 1),
(3, 2, 1, 'mieng', 9.0, NOW(), 1),
(4, 2, 3, '1tiet', 8.0, NOW(), 1);

-- Reset auto increment values to continue from the last inserted ID
ALTER TABLE users AUTO_INCREMENT = 4;
ALTER TABLE students AUTO_INCREMENT = 3;
ALTER TABLE subjects AUTO_INCREMENT = 5;
ALTER TABLE scores AUTO_INCREMENT = 5;

-- Display success message
SELECT 'Database created successfully with sample data!' AS Message;