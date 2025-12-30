package com.example.studentmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SchoolSystem.db";
    // Version 10 triggers the onUpgrade to create the new ROLES table and re-seed data
    private static final int DATABASE_VERSION = 10;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_LOGS = "system_logs";
    private static final String TABLE_CLASSES = "classes";
    private static final String TABLE_TIMETABLE = "timetable";
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String TABLE_GRADES = "grades";
    private static final String TABLE_FEES = "fees";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String TABLE_ROLES = "roles";

    private static final String TABLE_BOOKS = "library_books";

    private static final String TABLE_LIBRARY_ISSUES = "library_issues";

    private static final String TABLE_E_RESOURCES = "e_resources";

    private static final String TABLE_ASSIGNMENTS = "assignments";





    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {



        // 1. Users Table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT UNIQUE, " +
                "full_name TEXT, " +
                "password_hash TEXT, " +
                "role TEXT, " +
                "class_id INTEGER, " +
                "status TEXT)");

        // 2. Logs Table
        db.execSQL("CREATE TABLE " + TABLE_LOGS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "action TEXT, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // 3. Classes Table
        db.execSQL("CREATE TABLE " + TABLE_CLASSES + " (" +
                "class_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "grade_level TEXT, " +
                "section_name TEXT, " +
                "room_number TEXT, " +
                "teacher_id TEXT)");

        // 4. Timetable Table
        db.execSQL("CREATE TABLE " + TABLE_TIMETABLE + " (" +
                "schedule_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "class_id INTEGER, " +
                "day_of_week TEXT, " +
                "start_time TEXT, " +
                "end_time TEXT, " +
                "subject TEXT, " +
                "room TEXT, " +
                "teacher_name TEXT)");

        // 5. Attendance Table
        db.execSQL("CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "date TEXT, " +
                "status TEXT)");

        // 6. Grades Table
        db.execSQL("CREATE TABLE " + TABLE_GRADES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "subject TEXT, " +
                "grade TEXT, " +
                "semester TEXT)");

        // 7. Fees Table
        db.execSQL("CREATE TABLE " + TABLE_FEES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "description TEXT, " +
                "amount REAL, " +
                "type TEXT, " +
                "date TEXT)");

        // 8. Expenses Table
        db.execSQL("CREATE TABLE " + TABLE_EXPENSES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "requested_by TEXT, " +
                "category TEXT, " +
                "amount REAL, " +
                "description TEXT, " +
                "date TEXT, " +
                "status TEXT DEFAULT 'Pending')");

        // 9. Roles Table (New)
        db.execSQL("CREATE TABLE " + TABLE_ROLES + " (" +
                "role_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "role_name TEXT UNIQUE, " +
                "description TEXT)");

        // 10. Library Books Table
        db.execSQL("CREATE TABLE " + TABLE_BOOKS + " (" +
                "book_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "author TEXT, " +
                "isbn TEXT UNIQUE, " +
                "category TEXT, " +
                "quantity INTEGER, " +
                "location TEXT, " +
                "date_added TEXT)");

        // 11. Library Issues (Circulation) Table
        db.execSQL("CREATE TABLE " + TABLE_LIBRARY_ISSUES + " (" +
                "issue_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "book_id INTEGER, " +
                "student_id TEXT, " +
                "issue_date TEXT, " +
                "due_date TEXT, " +
                "return_date TEXT, " +
                "status TEXT DEFAULT 'Issued', " + // Issued, Returned, Overdue
                "fine_amount REAL DEFAULT 0)");

        // 12. E-Resources Table
        db.execSQL("CREATE TABLE " + TABLE_E_RESOURCES + " (" +
                "resource_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "category TEXT, " +
                "type TEXT, " + // PDF, Video, Link
                "url_or_path TEXT, " +
                "date_added TEXT)");

        // 13. Assignments Table
        db.execSQL("CREATE TABLE " + TABLE_ASSIGNMENTS + " (" +
                "assignment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "class_name TEXT, " + // e.g., "Grade 10-A"
                "subject TEXT, " + // e.g., "Chemistry"
                "max_score INTEGER)");

        // 14. Grades Table
        db.execSQL("CREATE TABLE " + TABLE_GRADES + " (" +
                "grade_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "assignment_id INTEGER, " +
                "student_id TEXT, " + // Links to User ID or Student ID
                "score INTEGER, " +
                "feedback TEXT)");


        seedData(db);
    }



    private void seedData(SQLiteDatabase db) {
        String testPassHash = SecurityUtil.hashPassword("123456");
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // --- SEED USERS ---
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach01', 'Mr. Robert Langdon', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach02', 'Ms. Sarah Connor', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach03', 'Mr. Walter White', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, class_id, status) VALUES ('stud01', 'Jason Statham', '" + testPassHash + "', 'Student', 1, 'Active')");

        // --- SEED CLASSES ---
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 10', 'Emerald', 'Rm 101', 'teach01')");
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 11', 'Ruby', 'Rm 104', 'teach02')");
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 12', 'Diamond', 'Rm 202', 'teach03')");

        // --- SEED TIMETABLE (Sample for Grade 10 / Class ID 1) ---
        // Monday
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (1, 'Monday', '08:00', '09:00', 'Mathematics', 'Rm 101', 'Mr. Langdon')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (1, 'Monday', '09:00', '10:00', 'History', 'Rm 102', 'Ms. Connor')");
        // Tuesday
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (1, 'Tuesday', '08:00', '09:00', 'Science', 'Lab 1', 'Mr. White')");
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, day_of_week, start_time, end_time, subject, room, teacher_name) VALUES (1, 'Tuesday', '10:00', '11:00', 'English', 'Rm 101', 'Mr. Keating')");

        // --- SEED ROLES ---
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_ROLES + " (role_name, description) VALUES ('Admin', 'Full System Access & Configuration')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_ROLES + " (role_name, description) VALUES ('Teacher', 'Class Management, Grading, Attendance')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_ROLES + " (role_name, description) VALUES ('Accountant', 'Fee Collection & Expense Management')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_ROLES + " (role_name, description) VALUES ('Student', 'View Schedule, Grades, and Fees')");

        // --- SEED EXPENSES ---
        db.execSQL("INSERT INTO " + TABLE_EXPENSES + " (title, requested_by, category, amount, description, date, status) VALUES ('Lab Equipment', 'teach03', 'Science Dept', 1200.00, 'New beakers', '" + todayDate + "', 'Pending')");
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY_ISSUES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_E_RESOURCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENTS);
        onCreate(db);
    }

    // ==========================================
    //            ROLES METHODS (NEW)
    // ==========================================

    public Cursor getAllRoles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ROLES, null);
    }

    public boolean addRole(String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("role_name", name);
        values.put("description", description);
        long result = db.insert(TABLE_ROLES, null, values);
        return result != -1;
    }

    public boolean updateRole(String originalName, String newName, String newDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("role_name", newName);
        values.put("description", newDesc);
        int result = db.update(TABLE_ROLES, values, "role_name = ?", new String[]{originalName});
        return result > 0;
    }

    public boolean deleteRole(String roleName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ROLES, "role_name = ?", new String[]{roleName}) > 0;
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

    public Cursor getStudentsByClass(String classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE role = 'Student' AND class_id = ?", new String[]{classId});
    }

    public Cursor getScheduleForDay(String dayOfWeek) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT t.*, c.grade_level, c.section_name " +
                "FROM " + TABLE_TIMETABLE + " t " +
                "LEFT JOIN " + TABLE_CLASSES + " c ON t.class_id = c.class_id " +
                "WHERE t.day_of_week = ? " +
                "ORDER BY t.start_time ASC";
        return db.rawQuery(query, new String[]{dayOfWeek});
    }

    public Cursor getClassSchedule(String classId, String dayOfWeek) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TIMETABLE +
                " WHERE class_id = ? AND day_of_week = ? " +
                " ORDER BY start_time ASC";
        return db.rawQuery(query, new String[]{classId, dayOfWeek});
    }

    // ==========================================
    //       STUDENT PROFILE & FINANCE METHODS
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
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(new Date());
        values.put("date", date);
        long result = db.insert("fees", null, values);
        return result != -1;
    }

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

    public boolean addBook(String title, String author, String isbn, String category, int quantity, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("author", author);
        values.put("isbn", isbn);
        values.put("category", category);
        values.put("quantity", quantity);
        values.put("location", location);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        values.put("date_added", today);

        long result = db.insert(TABLE_BOOKS, null, values);
        return result != -1;
    }

    // 2. Get Dashboard Stats (Active Issues & Overdue)
    public Map<String, Integer> getLibraryStats() {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, Integer> stats = new HashMap<>();

        // Count Active Issues (Status = 'Issued')
        Cursor activeCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_LIBRARY_ISSUES + " WHERE status = 'Issued'", null);
        if (activeCursor.moveToFirst()) {
            stats.put("active_issues", activeCursor.getInt(0));
        }
        activeCursor.close();

        // Count Overdue Items (Status = 'Issued' AND due_date < today)
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor overdueCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_LIBRARY_ISSUES + " WHERE status = 'Issued' AND due_date < ?", new String[]{today});
        if (overdueCursor.moveToFirst()) {
            stats.put("overdue_items", overdueCursor.getInt(0));
        }
        overdueCursor.close();

        return stats;
    }

    // 3. Get All Books (For Catalog)
    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " ORDER BY title ASC", null);
    }

    // ==========================================
    //       LIBRARY CIRCULATION METHODS
    // ==========================================

    public boolean issueBook(String isbn, String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 1. Get Book ID and check quantity
        Cursor cursor = db.rawQuery("SELECT book_id, quantity FROM " + TABLE_BOOKS + " WHERE isbn = ?", new String[]{isbn});
        if (cursor.moveToFirst()) {
            int bookId = cursor.getInt(0);
            int quantity = cursor.getInt(1);
            cursor.close();

            if (quantity > 0) {
                // 2. Create Issue Record
                ContentValues issueValues = new ContentValues();
                issueValues.put("book_id", bookId);
                issueValues.put("student_id", studentId);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String today = sdf.format(new Date());
                // Calculate due date (e.g., +14 days)
                String dueDate = sdf.format(new Date(System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000)));

                issueValues.put("issue_date", today);
                issueValues.put("due_date", dueDate);
                issueValues.put("status", "Issued");

                long result = db.insert(TABLE_LIBRARY_ISSUES, null, issueValues);

                if (result != -1) {
                    // 3. Decrease Book Quantity
                    db.execSQL("UPDATE " + TABLE_BOOKS + " SET quantity = quantity - 1 WHERE book_id = ?", new Object[]{bookId});
                    return true;
                }
            }
        } else {
            cursor.close();
        }
        return false; // Book not found or out of stock
    }

    public boolean returnBook(String isbn, String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 1. Find Book ID
        Cursor bCursor = db.rawQuery("SELECT book_id FROM " + TABLE_BOOKS + " WHERE isbn = ?", new String[]{isbn});
        if (bCursor.moveToFirst()) {
            int bookId = bCursor.getInt(0);
            bCursor.close();

            // 2. Find Active Issue
            Cursor iCursor = db.rawQuery("SELECT issue_id FROM " + TABLE_LIBRARY_ISSUES + " WHERE book_id = ? AND student_id = ? AND status = 'Issued'",
                    new String[]{String.valueOf(bookId), studentId});

            if (iCursor.moveToFirst()) {
                int issueId = iCursor.getInt(0);
                iCursor.close();

                // 3. Update Issue Record
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                ContentValues values = new ContentValues();
                values.put("return_date", today);
                values.put("status", "Returned");

                db.update(TABLE_LIBRARY_ISSUES, values, "issue_id = ?", new String[]{String.valueOf(issueId)});

                // 4. Increase Book Quantity
                db.execSQL("UPDATE " + TABLE_BOOKS + " SET quantity = quantity + 1 WHERE book_id = ?", new Object[]{bookId});
                return true;
            } else {
                iCursor.close();
            }
        } else {
            bCursor.close();
        }
        return false; // No active issue found
    }

    // ==========================================
    //         E-RESOURCES METHODS
    // ==========================================

    public boolean addEResource(String title, String category, String type, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("category", category);
        values.put("type", type);
        values.put("url_or_path", url);
        values.put("date_added", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        long result = db.insert(TABLE_E_RESOURCES, null, values);
        return result != -1;
    }

    public Cursor getAllEResources() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_E_RESOURCES + " ORDER BY resource_id DESC", null);
    }

    // ==========================================
    //         FINE CALCULATION METHODS
    // ==========================================

    // Returns list of students with overdue books and calculated fines
    public Cursor getOverdueStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Query joins Issues, Books, and Users to get details
        String query = "SELECT i.issue_id, b.title, u.full_name, i.due_date, " +
                "(julianday('" + today + "') - julianday(i.due_date)) as days_overdue " +
                "FROM " + TABLE_LIBRARY_ISSUES + " i " +
                "JOIN " + TABLE_BOOKS + " b ON i.book_id = b.book_id " +
                "JOIN " + TABLE_USERS + " u ON i.student_id = u.user_id " +
                "WHERE i.status = 'Issued' AND i.due_date < '" + today + "'";

        return db.rawQuery(query, null);
    }

    // ==========================================
    //      ACTIVE ISSUES (CIRCULATION)
    // ==========================================

    public Cursor getActiveLibraryIssues() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Joins Books table to get the Title.
        // We use the student_id string stored directly in the issues table.
        String query = "SELECT i.issue_id, b.title, i.student_id, i.due_date, i.issue_date " +
                "FROM " + TABLE_LIBRARY_ISSUES + " i " +
                "JOIN " + TABLE_BOOKS + " b ON i.book_id = b.book_id " +
                "WHERE i.status = 'Issued' " +
                "ORDER BY i.due_date ASC";

        return db.rawQuery(query, null);
    }

    private void seedTeacherData(SQLiteDatabase db) {
        // Add a sample assignment
        ContentValues cv = new ContentValues();
        cv.put("title", "Midterm Exam: Chemistry");
        cv.put("class_name", "Grade 10-A");
        cv.put("subject", "Chemistry");
        cv.put("max_score", 100);
        db.insert(TABLE_ASSIGNMENTS, null, cv);
    }

    // ==========================================
    //          TEACHER GRADEBOOK METHODS
    // ==========================================

    public Cursor getTeacherAssignments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ASSIGNMENTS, null);
    }

    // Get all students (simplified: fetching all users with role 'Student')
    // In a real app, you'd filter by Class ID
    public Cursor getStudentsForGradebook() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE role = 'Student'", null);
    }

    public int getStudentGrade(int assignmentId, String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT score FROM " + TABLE_GRADES + " WHERE assignment_id = ? AND student_id = ?",
                new String[]{String.valueOf(assignmentId), studentId});

        int score = -1; // Not graded yet
        if (cursor.moveToFirst()) {
            score = cursor.getInt(0);
        }
        cursor.close();
        return score;
    }

    public void saveStudentGrade(int assignmentId, String studentId, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("assignment_id", assignmentId);
        values.put("student_id", studentId);
        values.put("score", score);

        // Check if exists
        int current = getStudentGrade(assignmentId, studentId);
        if (current == -1) {
            db.insert(TABLE_GRADES, null, values);
        } else {
            db.update(TABLE_GRADES, values, "assignment_id = ? AND student_id = ?",
                    new String[]{String.valueOf(assignmentId), studentId});
        }
    }

}