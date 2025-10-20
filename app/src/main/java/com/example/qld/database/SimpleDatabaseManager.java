package com.example.qld.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qld.models.User;

public class SimpleDatabaseManager {
    private SimpleDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public SimpleDatabaseManager(Context context) {
        dbHelper = new SimpleDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Authenticates a user with the provided username and password
     * @param username The username
     * @param password The password
     * @return User object if authentication is successful, null otherwise
     */
    public User authenticateUser(String username, String password) {
        User user = null;
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + SimpleDatabaseHelper.TABLE_USERS + 
                          " WHERE " + SimpleDatabaseHelper.COLUMN_USERNAME + " = ? AND " +
                          SimpleDatabaseHelper.COLUMN_PASSWORD + " = ?";
            
            cursor = database.rawQuery(query, new String[]{username, password});
            
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SimpleDatabaseHelper.COLUMN_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(SimpleDatabaseHelper.COLUMN_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(SimpleDatabaseHelper.COLUMN_PASSWORD)));
                user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(SimpleDatabaseHelper.COLUMN_FULL_NAME)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(SimpleDatabaseHelper.COLUMN_ROLE)));
            }
        } catch (Exception e) {
            Log.e("SimpleDatabaseManager", "Error authenticating user: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
}