package com.example.qld.models;

public class User {
    private int id;
    private String username;
    private String password;
    private int role;
    private String fullName;
    private String createdDate;
    
    // Các constructor
    public User() {
    }
    
    public User(String username, String password, int role, String fullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }
    
    public User(int id, String username, String password, int role, String fullName, String createdDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.createdDate = createdDate;
    }
    
    // Các phương thức getter và setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getRole() {
        return role;
    }
    
    public void setRole(int role) {
        this.role = role;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}