package com.example.studentmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SchoolSystem.db";
    private static final int DATABASE_VERSION = 5; // Version 5 includes Expenses table

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
                "category TEXT, " +
                "amount REAL, " +
                "date TEXT, " +
                "description TEXT, " +
                "status TEXT, " +
                "requested_by TEXT)";
        db.execSQL(createExpenses);

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        String testPassHash = SecurityUtil.hashPassword("123456");

        // Users
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach01', 'Edna Krabappel', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('stud01', 'Jason Statham', '" + testPassHash + "', 'Student', 'Active')");

        // Schedule
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (day_of_week, start_time, end_time, subject, room, teacher_name) VALUES ('Mon', '08:00', '09:00', 'Mathematics', '101', 'Mrs. Krabappel')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (day_of_week, start_time, end_time, subject, room, teacher_name) VALUES ('Mon', '10:00', '11:00', 'History', '102', 'Mr. Hoover')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (day_of_week, start_time, end_time, subject, room, teacher_name) VALUES ('Tue', '09:00', '10:30', 'Science', 'Lab 1', 'Prof. Frink')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (day_of_week, start_time, end_time, subject, room, teacher_name) VALUES ('Wed', '08:00', '09:00', 'English', '103', 'Ms. Hoover')");

        // Attendance
        db.execSQL("INSERT INTO " + TABLE_ATTENDANCE + " (student_id, date, status) VALUES ('stud01', '2025-10-01', 'Present')");
        db.execSQL("INSERT INTO " + TABLE_ATTENDANCE + " (student_id, date, status) VALUES ('stud01', '2025-10-02', 'Present')");
        db.execSQL("INSERT INTO " + TABLE_ATTENDANCE + " (student_id, date, status) VALUES ('stud01', '2025-10-03', 'Absent')");

        // Grades
        db.execSQL("INSERT INTO " + TABLE_GRADES + " (student_id, subject, grade, semester) VALUES ('stud01', 'Mathematics', '95', '1st Sem')");
        db.execSQL("INSERT INTO " + TABLE_GRADES + " (student_id, subject, grade, semester) VALUES ('stud01', 'Science', '88', '1st Sem')");

        // Fees
        db.execSQL("INSERT INTO " + TABLE_FEES + " (student_id, description, amount, type, date) VALUES ('stud01', 'Tuition Fee', 5000.00, 'Bill', '2025-08-01')");
        db.execSQL("INSERT INTO " + TABLE_FEES + " (student_id, description, amount, type, date) VALUES ('stud01', 'Payment - Cash', 2500.00, 'Payment', '2025-08-10')");

        // Expenses Sample Data
        db.execSQL("INSERT INTO " + TABLE_EXPENSES + " (title, category, amount, date, description, status, requested_by) " +
                "VALUES ('Science Lab Microscopes', 'Education', 12500.00, '2025-10-15', 'Purchase of 5 new microscopes for the Biology lab.', 'Pending', 'teach01')");
        db.execSQL("INSERT INTO " + TABLE_EXPENSES + " (title, category, amount, date, description, status, requested_by) " +
                "VALUES ('Sports Day Refreshments', 'Events', 3500.00, '2025-09-20', 'Water and snacks for the annual sports meet.', 'Approved', 'admin01')");
        db.execSQL("INSERT INTO " + TABLE_EXPENSES + " (title, category, amount, date, description, status, requested_by) " +
                "VALUES ('Office Party Pizza', 'Miscellaneous', 1500.00, '2025-10-01', 'Pizza for staff meeting.', 'Rejected', 'teach01')");
        db.execSQL("INSERT INTO " + TABLE_EXPENSES + " (title, category, amount, date, description, status, requested_by) " +
                "VALUES ('Printer Maintenance', 'Maintenance', 850.00, '2025-10-18', 'Fixing the library printer paper jam issue.', 'Pending', 'admin01')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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

    public Cursor getScheduleForDay(String dayOfWeek) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TIMETABLE + " WHERE day_of_week = ? ORDER BY start_time ASC", new String[]{dayOfWeek});
    }

    // ==========================================
    //       STUDENT DATA METHODS
    // ==========================================

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

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String date = sdf.format(new java.util.Date());
        values.put("date", date);

        long result = db.insert(TABLE_FEES, null, values);
        return result != -1;
    }

    // ==========================================
    //             EXPENSE METHODS
    // ==========================================

    // Original method (Full parameters)
    public boolean addExpense(String title, String category, double amount, String date, String description, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("category", category);
        values.put("amount", amount);
        values.put("date", date);
        values.put("description", description);
        values.put("status", "Pending");
        values.put("requested_by", userId);

        long result = db.insert(TABLE_EXPENSES, null, values);
        return result != -1;
    }

    // Overloaded method to match Activity call (5 parameters)
    // Map: title, amount, date, category, userId
    public boolean addExpense(String title, double amount, String date, String category, String userId) {
        return addExpense(title, category, amount, date, "No description provided", userId);
    }

    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY date DESC", null);
    }

    public Cursor getExpenseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE id = ?", new String[]{String.valueOf(id)});
    }

    public boolean updateExpenseStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        int rows = db.update(TABLE_EXPENSES, values, "id = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // ADDED: Method to get total claimed amount
    public double getTotalClaimedAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSES, null);
        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            cursor.close();
            return total;
        }
        return 0;
    }

    // ADDED: Method to get total pending amount
    public double getPendingAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE status = 'Pending'", null);
        if (cursor.moveToFirst()) {
            double pending = cursor.getDouble(0);
            cursor.close();
            return pending;
        }
        return 0;
    }
}