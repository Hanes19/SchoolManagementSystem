package com.example.studentmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SchoolSystem.db";
    private static final int DATABASE_VERSION = 7; // Incremented version to apply date updates

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_LOGS = "system_logs";
    private static final String TABLE_CLASSES = "classes";
    private static final String TABLE_TIMETABLE = "timetable";
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String TABLE_GRADES = "grades";
    private static final String TABLE_FEES = "fees";
    private static final String TABLE_EXPENSES = "expenses";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Users Table
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT UNIQUE, " +
                "full_name TEXT, " +
                "password_hash TEXT, " +
                "role TEXT, " +
                "status TEXT)";
        db.execSQL(createUsers);

        // 2. Logs Table
        String createLogs = "CREATE TABLE " + TABLE_LOGS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "action TEXT, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createLogs);

        // 3. Classes Table
        String createClasses = "CREATE TABLE " + TABLE_CLASSES + " (" +
                "class_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "grade_level TEXT, " +
                "section_name TEXT, " +
                "room_number TEXT, " +
                "teacher_id TEXT)";
        db.execSQL(createClasses);

        // 4. Timetable Table
        String createTimetable = "CREATE TABLE " + TABLE_TIMETABLE + " (" +
                "schedule_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "class_id INTEGER, " +
                "day_of_week TEXT, " +
                "start_time TEXT, " +
                "end_time TEXT, " +
                "subject TEXT, " +
                "room TEXT, " +
                "teacher_name TEXT)";
        db.execSQL(createTimetable);

        // 5. Attendance Table
        String createAttendance = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "date TEXT, " +
                "status TEXT)";
        db.execSQL(createAttendance);

        // 6. Grades Table
        String createGrades = "CREATE TABLE " + TABLE_GRADES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "subject TEXT, " +
                "grade TEXT, " +
                "semester TEXT)";
        db.execSQL(createGrades);

        // 7. Fees Table
        String createFees = "CREATE TABLE " + TABLE_FEES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "description TEXT, " +
                "amount REAL, " +
                "type TEXT, " +
                "date TEXT)";
        db.execSQL(createFees);

        // 8. Expenses Table
        String createExpenses = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "requested_by TEXT, " +
                "category TEXT, " +
                "amount REAL, " +
                "description TEXT, " +
                "date TEXT, " +
                "status TEXT DEFAULT 'Pending')";
        db.execSQL(createExpenses);

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        String testPassHash = SecurityUtil.hashPassword("123456");

        // Helpers for Realistic Dates
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String yesterdayDate = "2025-12-26";
        String recentDate = "2025-12-20";

        // --- 1. SEED USERS (Teachers & Admin) ---
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach01', 'Mr. Robert Langdon', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach02', 'Ms. Sarah Connor', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach03', 'Mr. Walter White', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach04', 'Mrs. Minerva McGonagall', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach05', 'Mr. John Keating', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach06', 'Mr. Elliot Alderson', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach07', 'Coach Ted Lasso', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('stud01', 'Jason Statham', '" + testPassHash + "', 'Student', 'Active')");

        // --- 2. SEED CLASSES (Grades) ---
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 10', 'Emerald', 'Rm 101', 'teach01')");
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 11', 'Ruby', 'Rm 104', 'teach02')");
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 12', 'Diamond', 'Rm 202', 'teach04')");

        // --- 3. SEED MASTER TIMETABLE ---
        // == MONDAY ==
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (1, 'Monday', '07:30', '08:30', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (2, 'Monday', '07:30', '08:30', 'Physics', 'Rm 104', 'Ms. Sarah Connor')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (3, 'Monday', '07:30', '08:30', 'World History', 'Rm 202', 'Mrs. Minerva McGonagall')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (1, 'Monday', '08:30', '09:30', 'English Lit', 'Rm 101', 'Mr. John Keating')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (2, 'Monday', '08:30', '09:30', 'Chemistry', 'Lab A', 'Mr. Walter White')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (3, 'Monday', '08:30', '09:30', 'Economics', 'Rm 205', 'Ms. Sarah Connor')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (1, 'Monday', '10:00', '11:00', 'Physical Ed.', 'Gym', 'Coach Ted Lasso')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (3, 'Monday', '10:00', '11:00', 'Computer Sci', 'Comp Lab', 'Mr. Elliot Alderson')");

        // == TUESDAY ==
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (1, 'Tuesday', '07:30', '08:30', 'Science', 'Lab 1', 'Mr. Walter White')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (3, 'Tuesday', '09:00', '10:00', 'Adv Math', 'Rm 202', 'Mr. Robert Langdon')");

        // --- 4. SEED OTHER DATA (Realistic Dates) ---

        // Attendance: Uses Today and Yesterday (Dec 2025)
        db.execSQL("INSERT INTO " + TABLE_ATTENDANCE + " (student_id, date, status) VALUES ('stud01', '" + yesterdayDate + "', 'Present')");
        db.execSQL("INSERT INTO " + TABLE_ATTENDANCE + " (student_id, date, status) VALUES ('stud01', '" + todayDate + "', 'Present')");

        // Grades
        db.execSQL("INSERT INTO " + TABLE_GRADES + " (student_id, subject, grade, semester) VALUES ('stud01', 'Mathematics', '95', '1st Sem')");
        db.execSQL("INSERT INTO " + TABLE_GRADES + " (student_id, subject, grade, semester) VALUES ('stud01', 'Science', '88', '1st Sem')");

        // Fees: Uses a recent date in Dec 2025
        db.execSQL("INSERT INTO " + TABLE_FEES + " (student_id, description, amount, type, date) VALUES ('stud01', 'Tuition Fee (Final)', 5000.00, 'Bill', '" + recentDate + "')");

        // Expenses: Uses Today's date for a pending request
        db.execSQL("INSERT INTO " + TABLE_EXPENSES + " (title, requested_by, category, amount, description, date, status) VALUES ('Lab Equipment', 'teach03', 'Science Dept', 1200.00, 'New beakers for Chemistry', '" + todayDate + "', 'Pending')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop all tables on upgrade to ensure clean state
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // ==========================================
    //              USER METHODS
    // ==========================================

    public boolean checkUser(String userId, String rawPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedInput = SecurityUtil.hashPassword(rawPassword);
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_USERS + " WHERE user_id = ? AND password_hash = ? AND status = 'Active'", new String[]{userId, hashedInput});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public String getUserRole(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM " + TABLE_USERS + " WHERE user_id = ?", new String[]{userId});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        return null;
    }

    public String getUserName(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"full_name"}, "user_id = ?", new String[]{userId}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        return userId;
    }

    public boolean updateUserStatus(String userId, boolean isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", isActive ? "Active" : "Inactive");
        int rows = db.update(TABLE_USERS, values, "user_id = ?", new String[]{userId});
        return rows > 0;
    }

    public boolean resetPassword(String userId, String newPlainPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String passwordHash = SecurityUtil.hashPassword(newPlainPassword);
        values.put("password_hash", passwordHash);
        int rows = db.update(TABLE_USERS, values, "user_id = ?", new String[]{userId});
        return rows > 0;
    }

    public boolean isUserActive(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"status"}, "user_id = ?", new String[]{userId}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String status = cursor.getString(0);
            cursor.close();
            return "Active".equalsIgnoreCase(status);
        }
        return false;
    }

    // ==========================================
    //            SYSTEM LOG METHODS
    // ==========================================

    public void logAction(String userId, String action) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("action", action);
        db.insert(TABLE_LOGS, null, values);
    }

    public Cursor getAllLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_LOGS + " ORDER BY id DESC", null);
    }

    // ==========================================
    //          CLASS & TIMETABLE METHODS
    // ==========================================

    public Cursor getTeachers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT user_id, full_name FROM " + TABLE_USERS + " WHERE role = 'Teacher' AND status = 'Active'", null);
    }

    public boolean addClass(String grade, String section, String room, String teacherId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("grade_level", grade);
        values.put("section_name", section);
        values.put("room_number", room);
        values.put("teacher_id", teacherId);
        long result = db.insert(TABLE_CLASSES, null, values);
        return result != -1;
    }

    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.class_id, c.grade_level, c.section_name, c.room_number, u.full_name " +
                "FROM " + TABLE_CLASSES + " c " +
                "LEFT JOIN " + TABLE_USERS + " u ON c.teacher_id = u.user_id";
        return db.rawQuery(query, null);
    }

    // ==========================================
    //       NEW STUDENT PROFILE METHODS
    // ==========================================

    public Cursor getScheduleForDay(String dayOfWeek) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t.*, c.grade_level, c.section_name " +
                "FROM " + TABLE_TIMETABLE + " t " +
                "LEFT JOIN " + TABLE_CLASSES + " c ON t.class_id = c.class_id " +
                "WHERE t.day_of_week = ? " +
                "ORDER BY t.start_time ASC";
        return db.rawQuery(query, new String[]{dayOfWeek});
    }

    public Cursor getStudentAttendance(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE + " WHERE student_id = ?", new String[]{studentId});
    }

    public Cursor getStudentGrades(String studentId, String semester) {
        SQLiteDatabase db = this.getReadableDatabase();
        if(semester.equals("All")) {
            return db.rawQuery("SELECT * FROM " + TABLE_GRADES + " WHERE student_id = ?", new String[]{studentId});
        }
        return db.rawQuery("SELECT * FROM " + TABLE_GRADES + " WHERE student_id = ? AND semester = ?", new String[]{studentId, semester});
    }

    public Cursor getStudentFees(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FEES + " WHERE student_id = ? ORDER BY date DESC", new String[]{studentId});
    }

    public boolean addFee(String studentId, String description, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("description", description);
        values.put("amount", amount);
        values.put("type", type);

        // Get current date
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());
        values.put("date", date);

        long result = db.insert("fees", null, values);
        return result != -1;
    }

    // ==========================================
    //           EXPENSE METHODS
    // ==========================================

    public boolean addExpense(String title, String requestedBy, String category, double amount, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("requested_by", requestedBy);
        values.put("category", category);
        values.put("amount", amount);
        values.put("description", description);
        values.put("date", date);
        values.put("status", "Pending");

        long result = db.insert(TABLE_EXPENSES, null, values);
        return result != -1;
    }

    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.id, e.title, e.amount, e.date, e.category, e.status, e.requested_by, u.full_name " +
                "FROM " + TABLE_EXPENSES + " e " +
                "LEFT JOIN " + TABLE_USERS + " u ON e.requested_by = u.user_id " +
                "ORDER BY e.date DESC";
        return db.rawQuery(query, null);
    }

    public Cursor getExpenseById(int expenseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE id = ?", new String[]{String.valueOf(expenseId)});
    }

    public boolean updateExpenseStatus(int expenseId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        int rows = db.update(TABLE_EXPENSES, values, "id = ?", new String[]{String.valueOf(expenseId)});
        return rows > 0;
    }

    public double getTotalClaimedAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE status = 'Approved'", null);
        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            cursor.close();
            return total;
        }
        return 0.0;
    }

    public double getPendingAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE status = 'Pending'", null);
        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            cursor.close();
            return total;
        }
        return 0.0;
    }
}