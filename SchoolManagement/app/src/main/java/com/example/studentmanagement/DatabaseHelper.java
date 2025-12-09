package com.example.studentmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SchoolSystem.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT UNIQUE, " +
                "full_name TEXT, " +
                "password_hash TEXT, " + // Note: storing HASH, not password
                "role TEXT, " +
                "status TEXT)";
        db.execSQL(createUsers);

        // Inside DatabaseHelper.java -> onCreate

// Password for all test users is: 123456
        String testPassHash = SecurityUtil.hashPassword("123456");

// Create Admin
        db.execSQL("INSERT INTO " + TABLE_USERS +
                " (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");

// Create Teacher
        db.execSQL("INSERT INTO " + TABLE_USERS +
                " (user_id, full_name, password_hash, role, status) VALUES ('teach01', 'Edna Krabappel', '" + testPassHash + "', 'Teacher', 'Active')");

// Create Student
        db.execSQL("INSERT INTO " + TABLE_USERS +
                " (user_id, full_name, password_hash, role, status) VALUES ('stud01', 'Bart Simpson', '" + testPassHash + "', 'Student', 'Active')");

// Create Parent
//        db.execSQL("INSERT INTO " + TABLE_USERS +
//                " (user_id, full_name, password_hash, role, status) VALUES ('parent01', 'Homer Simpson', '" + testPassHash + "', 'Parent', 'Active')");
//
//        // --- SEED DEFAULT ADMIN USER ---
        // ID: admin, Password: admin123
        // We MUST hash the default password before inserting it
        String defaultPassHash = SecurityUtil.hashPassword("admin123");

        db.execSQL("INSERT INTO " + TABLE_USERS +
                " (user_id, full_name, password_hash, role, status) VALUES " +
                " ('admin', 'System Admin', '" + defaultPassHash + "', 'Admin', 'Active')");

        // 1. Common Password Hash (e.g., "123456")
        String commonPass = SecurityUtil.hashPassword("123456");

        // 2. Insert Test Users
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('admin', 'Admin User', '" + commonPass + "', 'Admin', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teacher', 'Teacher User', '" + commonPass + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('student', 'Student User', '" + commonPass + "', 'Student', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('parent', 'Parent User', '" + commonPass + "', 'Parent', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('staff', 'Staff User', '" + commonPass + "', 'Staff', 'Active')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Secure Login Check
    public boolean checkUser(String userId, String rawPassword) {
        SQLiteDatabase db = this.getReadableDatabase();

        // 1. Hash the entered password to match what is in the DB
        String hashedInput = SecurityUtil.hashPassword(rawPassword);

        String[] columns = { "id" };
        String selection = "user_id = ? AND password_hash = ? AND status = 'Active'";
        String[] selectionArgs = { userId, hashedInput };

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public String getUserRole(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"role"}, "user_id = ?", new String[]{userId}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        return null;
    }
}