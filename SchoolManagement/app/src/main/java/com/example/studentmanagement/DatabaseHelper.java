package com.example.studentmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SchoolSystem.db";
    // Updated Version to trigger upgrade/recreation
    private static final int DATABASE_VERSION = 32;

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
    private static final String TABLE_MESSAGES = "messages";
    private static final String TABLE_LEAVE = "leave_applications";
    private static final String TABLE_PAYROLL = "payroll";
    private static final String TABLE_FEE_PAYMENTS = "fee_payments";
    private static final String TABLE_EXAM_CATEGORIES = "exam_categories";
    private static final String TABLE_EXAM_SCHEDULE = "exam_schedule";
    private static final String TABLE_EXAM_MARKS = "exam_marks";
    private static final String TABLE_QUESTION_BANK = "question_bank";
    private static final String TABLE_NOTICES = "notices";
    private static final String TABLE_SESSIONS = "academic_sessions";
    private static final String TABLE_SUBJECTS = "subjects";
    private static final String TABLE_STUDENT_SUBJECTS = "student_subjects";
    // REMOVED DUPLICATE CONSTANT: private static final String TABLE_ACADEMIC_SESSIONS = "academic_sessions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Users Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT UNIQUE, " +
                "full_name TEXT, " +
                "password_hash TEXT, " +
                "role TEXT, " +
                "class_id INTEGER, " +
                "status TEXT, " +
                "email TEXT, " +
                "phone_number TEXT, " +
                "is_2fa_enabled INTEGER DEFAULT 0, " +
                "secret_key TEXT, " +
                "previous_school TEXT, " +
                "transfer_cert_no TEXT, " +
                "emergency_contact_name TEXT, " +
                "emergency_contact_phone TEXT, " +
                "roll_no TEXT)");

        // 2. Logs Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LOGS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "action TEXT, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");

        // 3. Classes Table (REMOVED DUPLICATE)
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CLASSES + " (" +
                "class_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "grade_level TEXT, " +
                "section_name TEXT, " +
                "room_number TEXT, " +
                "teacher_id TEXT)");

        // 4. Timetable Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TIMETABLE + " (" +
                "schedule_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "class_id INTEGER, " +
                "teacher_id TEXT, " +
                "class_name TEXT, " +
                "day_of_week TEXT, " +
                "start_time TEXT, " +
                "end_time TEXT, " +
                "subject TEXT, " +
                "room_no TEXT, " +
                "teacher_name TEXT)");

        // 5. Attendance Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ATTENDANCE + " (" +
                "att_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "date TEXT, " +
                "status TEXT, " +
                "class_name TEXT, " +
                "remarks TEXT)");

        // 6. Grades Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GRADES + " (" +
                "grade_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "assignment_id INTEGER, " +
                "student_id TEXT, " +
                "subject TEXT, " +
                "score INTEGER, " +
                "grade TEXT, " +
                "semester TEXT, " +
                "feedback TEXT, " +
                "exam_name TEXT, " +
                "total_marks INTEGER)");

        // 7. Fees Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FEES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "description TEXT, " +
                "amount REAL, " +
                "type TEXT, " +
                "date TEXT)");

        // 8. Expenses Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXPENSES + " (" +
                "expense_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "requested_by TEXT, " +
                "category TEXT, " +
                "amount REAL, " +
                "description TEXT, " +
                "date TEXT, " +
                "status TEXT DEFAULT 'Pending')");

        // 9. Roles Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ROLES + " (" +
                "role_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "role_name TEXT UNIQUE, " +
                "description TEXT)");

        // 10. Library Books Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BOOKS + " (" +
                "book_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "author TEXT, " +
                "isbn TEXT UNIQUE, " +
                "category TEXT, " +
                "quantity INTEGER, " +
                "location TEXT, " +
                "date_added TEXT)");

        // 11. Library Issues Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LIBRARY_ISSUES + " (" +
                "issue_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "book_id INTEGER, " +
                "student_id TEXT, " +
                "issue_date TEXT, " +
                "due_date TEXT, " +
                "return_date TEXT, " +
                "status TEXT DEFAULT 'Issued', " +
                "fine_amount REAL DEFAULT 0)");

        // 12. E-Resources Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_E_RESOURCES + " (" +
                "resource_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "category TEXT, " +
                "type TEXT, " +
                "url_or_path TEXT, " +
                "date_added TEXT)");

        // 13. Assignments Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ASSIGNMENTS + " (" +
                "assignment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "class_name TEXT, " +
                "subject TEXT, " +
                "due_date TEXT, " +
                "description TEXT, " +
                "max_score INTEGER, " +
                "date_created TEXT)");

        // 14. Messages Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGES + " (" +
                "msg_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sender_id TEXT, " +
                "receiver_id TEXT, " +
                "sender_name TEXT, " +
                "subject TEXT, " +
                "message_body TEXT, " +
                "timestamp TEXT, " +
                "is_read INTEGER DEFAULT 0)");

        // 15. Leave Applications Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LEAVE + " (" +
                "leave_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "leave_type TEXT, " +
                "start_date TEXT, " +
                "end_date TEXT, " +
                "reason TEXT, " +
                "status TEXT DEFAULT 'Pending', " +
                "applied_on TEXT)");

        // 16. Payroll Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PAYROLL + " (" +
                "payroll_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id TEXT, " +
                "month TEXT, " +
                "basic_salary REAL, " +
                "allowances REAL, " +
                "deductions REAL, " +
                "net_salary REAL, " +
                "status TEXT DEFAULT 'Paid', " +
                "generated_on TEXT)");

        // 17. Fee Payments Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FEE_PAYMENTS + " (" +
                "payment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "collected_by TEXT, " +
                "amount REAL, " +
                "payment_method TEXT, " +
                "date TEXT)");

        // 18. Exam Categories Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXAM_CATEGORIES + " (" +
                "exam_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exam_name TEXT, " +
                "start_date TEXT, " +
                "end_date TEXT, " +
                "status TEXT DEFAULT 'Draft')");

        // 19. Exam Schedule Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXAM_SCHEDULE + " (" +
                "schedule_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exam_id INTEGER, " +
                "class_name TEXT, " +
                "subject TEXT, " +
                "date TEXT, " +
                "start_time TEXT, " +
                "room_no TEXT)");

        // 20. Exam Marks Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXAM_MARKS + " (" +
                "mark_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "exam_id INTEGER, " +
                "student_id TEXT, " +
                "subject TEXT, " +
                "score INTEGER, " +
                "total_marks INTEGER DEFAULT 100)");

        // 21. Question Bank Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION_BANK + " (" +
                "question_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject TEXT, " +
                "grade_level TEXT, " +
                "question_text TEXT, " +
                "type TEXT, " +
                "options TEXT, " +
                "correct_answer TEXT)");

        // 22. Notice Board Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTICES + " (" +
                "notice_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "audience TEXT, " +
                "date_posted TEXT)");

        // 23. Academic Sessions Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SESSIONS + " (" +
                "session_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "session_name TEXT, " +
                "start_date TEXT, " +
                "end_date TEXT, " +
                "is_active INTEGER DEFAULT 0)");

        // 24. Subjects Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS + " (" +
                "subject_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject_name TEXT, " +
                "grade_level TEXT, " +
                "cost REAL, " +
                "description TEXT)");

        // 25. Student-Subject Link Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_STUDENT_SUBJECTS + " (" +
                "link_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id TEXT, " +
                "subject_id INTEGER, " +
                "enrollment_date TEXT)");

        seedData(db);
    }


    private void seedData(SQLiteDatabase db) {
        String testPassHash = SecurityUtil.hashPassword("123456");
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // --- SEED USERS & TEACHERS ---
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach01', 'Mr. Robert Langdon', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach02', 'Ms. Sarah Connor', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach03', 'Mr. Walter White', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach04', 'Mr. John Keating', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('teach05', 'Ms. Frizzle', '" + testPassHash + "', 'Teacher', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, class_id, status) VALUES ('stud01', 'Jason Statham', '" + testPassHash + "', 'Student', 1, 'Active')");

        // --- SEED CLASSES (1=G10, 2=G11, 3=G12) ---
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 10', 'Emerald', 'Rm 101', 'teach01')");
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 11', 'Ruby', 'Rm 104', 'teach02')");
        db.execSQL("INSERT INTO " + TABLE_CLASSES + " (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 12', 'Diamond', 'Rm 202', 'teach03')");

        // --- SEED ROLES ---
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_ROLES + " (role_name, description) VALUES ('Admin', 'Full System Access & Configuration')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_ROLES + " (role_name, description) VALUES ('Teacher', 'Class Management, Grading, Attendance')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_ROLES + " (role_name, description) VALUES ('Accountant', 'Fee Collection & Expense Management')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_ROLES + " (role_name, description) VALUES ('Student', 'View Schedule, Grades, and Fees')");

        // --- SEED TIMETABLE (Full Week, All Grades) ---

        // MONDAY
        // Grade 10 (Class ID 1)
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(1, 'Grade 10-A', 'Monday', '08:00', '09:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(1, 'Grade 10-A', 'Monday', '09:00', '10:00', 'History', 'Rm 102', 'Ms. Sarah Connor')," +
                "(1, 'Grade 10-A', 'Monday', '10:30', '11:30', 'Physics', 'Lab 1', 'Mr. Walter White')," +
                "(1, 'Grade 10-A', 'Monday', '01:00', '02:00', 'English', 'Rm 103', 'Mr. John Keating')");
        // Grade 11 (Class ID 2)
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(2, 'Grade 11-A', 'Monday', '08:00', '09:00', 'Physics', 'Lab 1', 'Mr. Walter White')," +
                "(2, 'Grade 11-A', 'Monday', '09:00', '10:00', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(2, 'Grade 11-A', 'Monday', '10:30', '11:30', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(2, 'Grade 11-A', 'Monday', '01:00', '02:00', 'Biology', 'Lab 2', 'Ms. Frizzle')");
        // Grade 12 (Class ID 3)
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(3, 'Grade 12-A', 'Monday', '08:00', '09:00', 'History', 'Rm 102', 'Ms. Sarah Connor')," +
                "(3, 'Grade 12-A', 'Monday', '09:00', '10:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(3, 'Grade 12-A', 'Monday', '10:30', '11:30', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(3, 'Grade 12-A', 'Monday', '01:00', '02:00', 'Chemistry', 'Lab 3', 'Mr. Walter White')");

        // Seed Users & Classes (Keep your existing seeds)
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO classes (grade_level, section_name, room_number) VALUES ('Grade 10', 'Emerald', 'Rm 101')");
        db.execSQL("INSERT OR IGNORE INTO classes (grade_level, section_name, room_number) VALUES ('Grade 11', 'Ruby', 'Rm 104')");

        // --- SEED SUBJECTS WITH COSTS ---
        // Grade 10 Subjects
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('Mathematics 10', 'Grade 10', 300.00)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('Science 10', 'Grade 10', 350.00)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('History 10', 'Grade 10', 200.00)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('English 10', 'Grade 10', 200.00)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('Physical Ed 10', 'Grade 10', 150.00)");

        // Grade 11 Subjects
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('Adv Math 11', 'Grade 11', 400.00)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('Physics 11', 'Grade 11', 450.00)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('Chemistry 11', 'Grade 11', 450.00)");
        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " (subject_name, grade_level, cost) VALUES ('Biology 11', 'Grade 11', 450.00)");


        // TUESDAY
        // Grade 10
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(1, 'Grade 10-A', 'Tuesday', '08:00', '09:00', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(1, 'Grade 10-A', 'Tuesday', '09:00', '10:00', 'Physics', 'Lab 1', 'Mr. Walter White')," +
                "(1, 'Grade 10-A', 'Tuesday', '10:30', '11:30', 'Biology', 'Lab 2', 'Ms. Frizzle')," +
                "(1, 'Grade 10-A', 'Tuesday', '01:00', '02:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')");
        // Grade 11
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(2, 'Grade 11-A', 'Tuesday', '08:00', '09:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(2, 'Grade 11-A', 'Tuesday', '09:00', '10:00', 'History', 'Rm 102', 'Ms. Sarah Connor')," +
                "(2, 'Grade 11-A', 'Tuesday', '10:30', '11:30', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(2, 'Grade 11-A', 'Tuesday', '01:00', '02:00', 'Physics', 'Lab 1', 'Mr. Walter White')");
        // Grade 12
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(3, 'Grade 12-A', 'Tuesday', '08:00', '09:00', 'Chemistry', 'Lab 3', 'Mr. Walter White')," +
                "(3, 'Grade 12-A', 'Tuesday', '09:00', '10:00', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(3, 'Grade 12-A', 'Tuesday', '10:30', '11:30', 'History', 'Rm 102', 'Ms. Sarah Connor')," +
                "(3, 'Grade 12-A', 'Tuesday', '01:00', '02:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')");

        // WEDNESDAY
        // Grade 10
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(1, 'Grade 10-A', 'Wednesday', '08:00', '09:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(1, 'Grade 10-A', 'Wednesday', '09:00', '10:00', 'History', 'Rm 102', 'Ms. Sarah Connor')," +
                "(1, 'Grade 10-A', 'Wednesday', '10:30', '11:30', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(1, 'Grade 10-A', 'Wednesday', '01:00', '02:00', 'Chemistry', 'Lab 1', 'Mr. Walter White')");
        // Grade 11
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(2, 'Grade 11-A', 'Wednesday', '08:00', '09:00', 'Biology', 'Lab 2', 'Ms. Frizzle')," +
                "(2, 'Grade 11-A', 'Wednesday', '09:00', '10:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(2, 'Grade 11-A', 'Wednesday', '10:30', '11:30', 'History', 'Rm 102', 'Ms. Sarah Connor')," +
                "(2, 'Grade 11-A', 'Wednesday', '01:00', '02:00', 'English', 'Rm 103', 'Mr. John Keating')");
        // Grade 12
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(3, 'Grade 12-A', 'Wednesday', '08:00', '09:00', 'Physics', 'Lab 3', 'Mr. Walter White')," +
                "(3, 'Grade 12-A', 'Wednesday', '09:00', '10:00', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(3, 'Grade 12-A', 'Wednesday', '10:30', '11:30', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(3, 'Grade 12-A', 'Wednesday', '01:00', '02:00', 'History', 'Rm 102', 'Ms. Sarah Connor')");

        // THURSDAY
        // Grade 10
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(1, 'Grade 10-A', 'Thursday', '08:00', '09:00', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(1, 'Grade 10-A', 'Thursday', '09:00', '10:00', 'Biology', 'Lab 2', 'Ms. Frizzle')," +
                "(1, 'Grade 10-A', 'Thursday', '10:30', '11:30', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(1, 'Grade 10-A', 'Thursday', '01:00', '02:00', 'History', 'Rm 102', 'Ms. Sarah Connor')");
        // Grade 11
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(2, 'Grade 11-A', 'Thursday', '08:00', '09:00', 'Physics', 'Lab 1', 'Mr. Walter White')," +
                "(2, 'Grade 11-A', 'Thursday', '09:00', '10:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(2, 'Grade 11-A', 'Thursday', '10:30', '11:30', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(2, 'Grade 11-A', 'Thursday', '01:00', '02:00', 'History', 'Rm 102', 'Ms. Sarah Connor')");
        // Grade 12
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(3, 'Grade 12-A', 'Thursday', '08:00', '09:00', 'History', 'Rm 102', 'Ms. Sarah Connor')," +
                "(3, 'Grade 12-A', 'Thursday', '09:00', '10:00', 'Biology', 'Lab 3', 'Ms. Frizzle')," +
                "(3, 'Grade 12-A', 'Thursday', '10:30', '11:30', 'Physics', 'Lab 1', 'Mr. Walter White')," +
                "(3, 'Grade 12-A', 'Thursday', '01:00', '02:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')");

        // FRIDAY
        // Grade 10
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(1, 'Grade 10-A', 'Friday', '08:00', '09:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(1, 'Grade 10-A', 'Friday', '09:00', '10:00', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(1, 'Grade 10-A', 'Friday', '10:30', '11:30', 'Physics', 'Lab 1', 'Mr. Walter White')," +
                "(1, 'Grade 10-A', 'Friday', '01:00', '02:00', 'History', 'Rm 102', 'Ms. Sarah Connor')");
        // Grade 11
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(2, 'Grade 11-A', 'Friday', '08:00', '09:00', 'Biology', 'Lab 2', 'Ms. Frizzle')," +
                "(2, 'Grade 11-A', 'Friday', '09:00', '10:00', 'History', 'Rm 102', 'Ms. Sarah Connor')," +
                "(2, 'Grade 11-A', 'Friday', '10:30', '11:30', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(2, 'Grade 11-A', 'Friday', '01:00', '02:00', 'English', 'Rm 103', 'Mr. John Keating')");
        // Grade 12
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (class_id, class_name, day_of_week, start_time, end_time, subject, room_no, teacher_name) VALUES " +
                "(3, 'Grade 12-A', 'Friday', '08:00', '09:00', 'Chemistry', 'Lab 3', 'Mr. Walter White')," +
                "(3, 'Grade 12-A', 'Friday', '09:00', '10:00', 'Mathematics', 'Rm 101', 'Mr. Robert Langdon')," +
                "(3, 'Grade 12-A', 'Friday', '10:30', '11:30', 'English', 'Rm 103', 'Mr. John Keating')," +
                "(3, 'Grade 12-A', 'Friday', '01:00', '02:00', 'History', 'Rm 102', 'Ms. Sarah Connor')");

        // --- SEED EXPENSES ---
        db.execSQL("INSERT INTO " + TABLE_EXPENSES + " (title, requested_by, category, amount, description, date, status) VALUES ('Lab Equipment', 'teach03', 'Science Dept', 1200.00, 'New beakers', '" + todayDate + "', 'Pending')");
        db.execSQL("INSERT INTO " + TABLE_EXPENSES + " (title, category, amount, date, description) VALUES ('Electric Bill', 'Utilities', 450.00, '2025-10-01', 'September Electricity')");

        // --- SEED MESSAGES ---
        db.execSQL("INSERT INTO " + TABLE_MESSAGES + " (sender_id, receiver_id, sender_name, subject, message_body, timestamp) VALUES " +
                "('admin01', 'teach01', 'Principal Skinner', 'Staff Meeting', 'Please attend the meeting at 2 PM tomorrow.', '2025-10-24 09:00')," +
                "('parent05', 'teach01', 'Mrs. Smith', 'Regarding Jason', 'Can we schedule a call regarding his grades?', '2025-10-23 18:30')");

        // --- SEED PAYROLL ---
        db.execSQL("INSERT INTO " + TABLE_PAYROLL + " (user_id, month, basic_salary, allowances, deductions, net_salary, generated_on) VALUES " +
                "('stf001', 'September 2025', 5000.00, 1200.00, 300.00, 5900.00, '2025-09-30')");

        // --- SEED LEAVE ---
        db.execSQL("INSERT INTO " + TABLE_LEAVE + " (user_id, leave_type, start_date, end_date, reason, status, applied_on) VALUES " +
                "('stf001', 'Sick Leave', '2025-10-10', '2025-10-12', 'Fever', 'Approved', '2025-10-09')");

        // --- SEED EXAMS & QUESTION BANK ---
        db.execSQL("INSERT INTO " + TABLE_EXAM_CATEGORIES + " (exam_name, start_date, end_date, status) VALUES ('Midterm Finals 2025', '2025-10-20', '2025-10-25', 'Published')");
        db.execSQL("INSERT INTO " + TABLE_QUESTION_BANK + " (subject, grade_level, question_text, type) VALUES ('Mathematics', 'Grade 10', 'Solve for x: 2x+5=15', 'MCQ')");

        db.execSQL("INSERT INTO " + TABLE_EXAM_CATEGORIES + " (exam_name, start_date) VALUES ('First Grading', '2025-01-15')");

        // 2. Seed Exams (Using 'start_date' instead of 'date')
        db.execSQL("INSERT INTO " + TABLE_EXAM_CATEGORIES + " (exam_name, start_date, end_date) VALUES " +
                "('Midterm', '2025-03-10', '2025-03-15')," +
                "('Finals', '2025-06-20', '2025-06-25')");

        // --- SEED FEE PAYMENTS (Sample for Student 'stud01') ---
        db.execSQL("INSERT INTO " + TABLE_FEE_PAYMENTS + " (student_id, collected_by, amount, payment_method, date) VALUES " +
                "('stud01', 'staff01', 5000.00, 'Cash', '2025-01-10'), " +
                "('stud01', 'staff01', 1250.00, 'Bank Transfer', '2025-01-12')");

        // --- SEED ATTENDANCE ---
        db.execSQL("INSERT INTO " + TABLE_ATTENDANCE + " (student_id, date, status, remarks) VALUES " +
                "('stud01', '2025-01-06', 'Present', 'On time')," +
                "('stud01', '2025-01-07', 'Late', 'Traffic')," +
                "('stud01', '2025-01-08', 'Absent', 'Sick')," +
                "('stud01', '2025-01-09', 'Present', 'On time')," +
                "('stud01', '2025-01-10', 'Present', 'On time')");

        // --- SEED EXAM MARKS (Sample for Jason 'stud01') ---
        db.execSQL("INSERT INTO " + TABLE_EXAM_MARKS + " (exam_id, student_id, subject, score, total_marks) VALUES " +
                "(1, 'stud01', 'Mathematics', 92, 100)," +
                "(1, 'stud01', 'Science', 88, 100)," +
                "(1, 'stud01', 'English', 95, 100)," +
                "(1, 'stud01', 'Filipino', 90, 100)," +
                "(1, 'stud01', 'History', 85, 100)");

        db.execSQL("INSERT INTO " + TABLE_GRADES + " (student_id, subject, score, grade, semester, feedback) VALUES " +
                "('stud01', 'Mathematics', 85, 'A', 'Midterm', 'Good job')," +
                "('stud01', 'Science', 78, 'B', 'Midterm', 'Keep it up')," +
                "('stud01', 'History', 90, 'A', 'Midterm', 'Excellent')," +
                "('stud01', 'English', 88, 'A', 'Midterm', 'Well done')");

        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, class_id, status) VALUES ('stud01', 'Jason Statham', '" + testPassHash + "', 'Student', 1, 'Active')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, class_id, status) VALUES ('stud02', 'Sarah Jane', '" + testPassHash + "', 'Student', 2, 'Active')");
        // stud05 = Hermione Granger (Grade 11)
        db.execSQL("INSERT OR IGNORE INTO users (user_id, full_name, password_hash, role, class_id, status, roll_no) VALUES ('stud05', 'Hermione Granger', '" + testPassHash + "', 'Student', 2, 'Active', '1101')");
        // 1. Seed Staff
        db.execSQL("INSERT OR IGNORE INTO users (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");
        db.execSQL("INSERT OR IGNORE INTO users (user_id, full_name, password_hash, role, status) VALUES ('teach01', 'Mr. Robert Langdon', '" + testPassHash + "', 'Teacher', 'Active')");

        // 2. Seed Classes
        db.execSQL("INSERT INTO classes (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 10', 'Emerald', 'Rm 101', 'teach01')");
        db.execSQL("INSERT INTO classes (grade_level, section_name, room_number, teacher_id) VALUES ('Grade 11', 'Ruby', 'Rm 104', 'teach01')");

        // 3. Seed Multiple Students
        db.execSQL("INSERT OR IGNORE INTO users (user_id, full_name, password_hash, role, class_id, status, roll_no) VALUES ('stud01', 'Jason Statham', '" + testPassHash + "', 'Student', 1, 'Active', '1001')");
        db.execSQL("INSERT OR IGNORE INTO users (user_id, full_name, password_hash, role, class_id, status, roll_no) VALUES ('stud02', 'Alice Wonderland', '" + testPassHash + "', 'Student', 1, 'Active', '1002')");
        db.execSQL("INSERT OR IGNORE INTO users (user_id, full_name, password_hash, role, class_id, status, roll_no) VALUES ('stud03', 'Peter Parker', '" + testPassHash + "', 'Student', 1, 'Active', '1003')");
        db.execSQL("INSERT OR IGNORE INTO users (user_id, full_name, password_hash, role, class_id, status, roll_no) VALUES ('stud05', 'Hermione Granger', '" + testPassHash + "', 'Student', 2, 'Active', '1101')");

        // 4. Seed Attendance History
        String[] dates = {"2025-01-01", "2025-01-02", "2025-01-03"};
        for (String date : dates) {
            db.execSQL("INSERT INTO attendance (student_id, date, status, class_name) VALUES ('stud01', '" + date + "', 'Present', 'Grade 10-Emerald')");
            db.execSQL("INSERT INTO attendance (student_id, date, status, class_name) VALUES ('stud02', '" + date + "', 'Absent', 'Grade 10-Emerald')");


        }
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USERS + " (user_id, full_name, password_hash, role, status) VALUES ('admin01', 'Principal Skinner', '" + testPassHash + "', 'Admin', 'Active')");



    }

    private void seedFeeData(SQLiteDatabase db) {
        // --- FIX: Define 'today' here so the code below can use it ---
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // 1. INVOICES (What they owe)
        // Student 1 (Jason)
        db.execSQL("INSERT INTO " + TABLE_FEES + " (student_id, description, amount, type, date) VALUES " +
                "('stud01', 'Tuition Fee - Term 1', 15000.00, 'Tuition', '" + today + "')," +
                "('stud01', 'Laboratory Fee', 2500.00, 'Lab', '" + today + "')," +
                "('stud01', 'Uniform Set', 1200.00, 'Misc', '" + today + "')");

        // Student 2 (Sarah)
        db.execSQL("INSERT INTO " + TABLE_FEES + " (student_id, description, amount, type, date) VALUES " +
                "('stud02', 'Tuition Fee - Term 1', 15000.00, 'Tuition', '" + today + "')," +
                "('stud02', 'Library Fine', 50.00, 'Fine', '" + today + "')");

        // 2. PAYMENTS (What they paid)
        db.execSQL("INSERT INTO " + TABLE_FEE_PAYMENTS + " (student_id, collected_by, amount, payment_method, date) VALUES " +
                "('stud01', 'admin01', 5000.00, 'Cash', '" + today + "')," +
                "('stud01', 'admin01', 2000.00, 'Bank Transfer', '" + today + "')");

        db.execSQL("INSERT INTO " + TABLE_FEE_PAYMENTS + " (student_id, collected_by, amount, payment_method, date) VALUES " +
                "('stud02', 'admin01', 15050.00, 'Cash', '" + today + "')");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYROLL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAM_MARKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_BANK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEE_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_SUBJECTS);
        // REMOVED DUPLICATE DROP: db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACADEMIC_SESSIONS);


        // Create new tables
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
        Cursor cursor = db.rawQuery("SELECT full_name FROM users WHERE user_id = ?", new String[]{userId});
        if(cursor.moveToFirst()){
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        cursor.close();
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



    // ==========================================
    //       STUDENT PROFILE & FINANCE METHODS
    // ==========================================



    public Cursor getStudentGrades(String studentId, String semester) {
        SQLiteDatabase db = this.getReadableDatabase();
        if(semester.equals("All")) {
            return db.rawQuery("SELECT * FROM " + TABLE_GRADES + " WHERE student_id = ?", new String[]{studentId});
        }
        return db.rawQuery("SELECT * FROM " + TABLE_GRADES + " WHERE student_id = ? AND semester = ?", new String[]{studentId, semester});
    }

    public Cursor getStudentFees(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // CHANGE: Query TABLE_FEE_PAYMENTS to include the payment_method column
        return db.rawQuery("SELECT * FROM " + TABLE_FEE_PAYMENTS + " WHERE student_id = ? ORDER BY date DESC", new String[]{studentId});
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

    // ==========================================
    //          HOMEWORK METHODS
    // ==========================================

    public boolean addAssignment(String title, String className, String subject, String dueDate, int maxScore, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("class_name", className);
        values.put("subject", subject);
        values.put("due_date", dueDate);
        values.put("max_score", maxScore);
        values.put("description", desc);
        values.put("date_created", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        long result = db.insert(TABLE_ASSIGNMENTS, null, values);
        return result != -1;
    }

    public Cursor getAllAssignments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ASSIGNMENTS + " ORDER BY assignment_id DESC", null);
    }

    // ==========================================
    //          ATTENDANCE METHODS
    // ==========================================

    // Get all students for the list, with their attendance status for a specific date if it exists
    public Cursor getStudentsWithAttendance(String date, String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Left Join Users with Attendance to get everyone even if not marked yet
        String query = "SELECT u.user_id, u.full_name, a.status " +
                "FROM " + TABLE_USERS + " u " +
                "LEFT JOIN " + TABLE_ATTENDANCE + " a ON u.user_id = a.student_id AND a.date = ? " +
                "WHERE u.role = 'Student'"; // Add 'AND u.class = className' in real app

        return db.rawQuery(query, new String[]{date});
    }

    public void saveAttendance(String studentId, String date, String status, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("date", date);
        values.put("status", status);
        values.put("class_name", className);

        // Check if record exists
        Cursor cursor = db.rawQuery("SELECT att_id FROM " + TABLE_ATTENDANCE + " WHERE student_id = ? AND date = ?", new String[]{studentId, date});
        if (cursor.moveToFirst()) {
            // Update
            db.update(TABLE_ATTENDANCE, values, "student_id = ? AND date = ?", new String[]{studentId, date});
        } else {
            // Insert
            db.insert(TABLE_ATTENDANCE, null, values);
        }
        cursor.close();
    }
    private void seedScheduleAndMessages(SQLiteDatabase db) {
        // Sample Schedule
        db.execSQL("INSERT INTO " + TABLE_TIMETABLE + " (teacher_id, class_name, subject, day_of_week, start_time, end_time, room_no) VALUES " +
                "('TCH-001', 'Grade 10-A', 'Chemistry', 'Monday', '08:00 AM', '09:30 AM', 'Lab 3')," +
                "('TCH-001', 'Grade 11-B', 'Physics', 'Monday', '10:00 AM', '11:30 AM', 'Room 102')," +
                "('TCH-001', 'Grade 9-C', 'Science', 'Tuesday', '09:00 AM', '10:30 AM', 'Room 105')");

        // Sample Messages
        db.execSQL("INSERT INTO " + TABLE_MESSAGES + " (sender_id, receiver_id, sender_name, subject, message_body, timestamp) VALUES " +
                "('ADM-001', 'TCH-001', 'Admin', 'Staff Meeting', 'Please attend the meeting at 2 PM.', '2025-10-24 09:00')," +
                "('PAR-005', 'TCH-001', 'Mrs. Smith', 'Regarding Jason', 'Can we schedule a call regarding his grades?', '2025-10-23 18:30')");
    }

    // ==========================================
    //          TIMETABLE & MESSAGE METHODS
    // ==========================================

    public Cursor getTeacherSchedule(String teacherId, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TIMETABLE + " WHERE teacher_id = ? AND day_of_week = ? ORDER BY start_time ASC",
                new String[]{teacherId, day});
    }

    public Cursor getTeacherMessages(String teacherId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " WHERE receiver_id = ? ORDER BY timestamp DESC",
                new String[]{teacherId});
    }

    // ==========================================
    //          MESSAGING METHODS
    // ==========================================

    public Cursor getMessagesForUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " WHERE receiver_id = ? ORDER BY timestamp DESC", new String[]{userId});
    }

    public boolean sendMessage(String senderId, String receiverId, String senderName, String subject, String body) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sender_id", senderId);
        values.put("receiver_id", receiverId);
        values.put("sender_name", senderName);
        values.put("subject", subject);
        values.put("body", body);
        values.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));

        long result = db.insert(TABLE_MESSAGES, null, values);
        return result != -1;
    }

// ==========================================
    //           STAFF PORTAL METHODS
    // ==========================================

    public boolean applyForLeave(String userId, String type, String start, String end, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("leave_type", type);
        values.put("start_date", start);
        values.put("end_date", end);
        values.put("reason", reason);
        values.put("applied_on", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        return db.insert(TABLE_LEAVE, null, values) != -1;
    }

    public Cursor getMyLeaveHistory(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_LEAVE + " WHERE user_id = ? ORDER BY applied_on DESC", new String[]{userId});
    }

    public Cursor getMyPayslips(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PAYROLL + " WHERE user_id = ? ORDER BY payroll_id DESC", new String[]{userId});
    }

    // Fee Collection
    public boolean collectFee(String studentId, String staffId, double amount, String method) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("collected_by", staffId);
        values.put("amount", amount);
        values.put("payment_method", method);
        values.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        return db.insert(TABLE_FEE_PAYMENTS, null, values) != -1;
    }

    // Get Student Name Helper
    public String getStudentName(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT full_name FROM " + TABLE_USERS + " WHERE user_id = ?", new String[]{studentId});
        if(cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        cursor.close();
        return null;
    }
    // ==========================================
    //           EXAMINATION METHODS
    // ==========================================

    public boolean addExamCategory(String name, String start, String end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("exam_name", name);
        values.put("start_date", start);
        values.put("end_date", end);
        return db.insert(TABLE_EXAM_CATEGORIES, null, values) != -1;
    }

    public Cursor getAllExams() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXAM_CATEGORIES + " ORDER BY start_date DESC", null);
    }

    public boolean scheduleExam(int examId, String className, String subject, String date, String time, String room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("exam_id", examId);
        values.put("class_name", className);
        values.put("subject", subject);
        values.put("date", date);
        values.put("start_time", time);
        values.put("room_no", room);
        return db.insert(TABLE_EXAM_SCHEDULE, null, values) != -1;
    }

    public Cursor getQuestions(String subject, String grade) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUESTION_BANK + " WHERE subject = ? AND grade_level = ?", new String[]{subject, grade});
    }

    public boolean addQuestion(String subject, String grade, String text, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject", subject);
        values.put("grade_level", grade);
        values.put("question_text", text);
        values.put("type", type);
        return db.insert(TABLE_QUESTION_BANK, null, values) != -1;
    }

    // Marks Entry
    public void saveExamMark(int examId, String studentId, String subject, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("exam_id", examId);
        values.put("student_id", studentId);
        values.put("subject", subject);
        values.put("score", score);

        // Update if exists, else insert
        int rows = db.update(TABLE_EXAM_MARKS, values, "exam_id=? AND student_id=? AND subject=?",
                new String[]{String.valueOf(examId), studentId, subject});
        if (rows == 0) {
            db.insert(TABLE_EXAM_MARKS, null, values);
        }
    }

    public String getLinkedChildId(String parentId) {
        // In a real app, query a 'parent_child_link' table.
        // For this demo, we return a hardcoded student ID.
        return "stud01";
    }

    // ==========================================
    //           ADMIN ANALYTICS & MISC
    // ==========================================

    public long getCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return android.database.DatabaseUtils.queryNumEntries(db, tableName);
    }

    // Specific counts for analytics
    public long getStudentCount() { return getCount(TABLE_USERS) - getCount(TABLE_USERS) + 150; /* Mock or filter by role */ }
    // In real app: return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM users WHERE role='Student'", null);

    public boolean addNotice(String title, String desc, String audience) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", desc);
        values.put("audience", audience);
        values.put("date_posted", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        return db.insert(TABLE_NOTICES, null, values) != -1;
    }

    public Cursor getAllNotices() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NOTICES + " ORDER BY notice_id DESC", null);
    }

    // ==========================================
    //           ADMIT CARD METHODS
    // ==========================================

    public Cursor getStudentsForAdmitCard(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        // In real app, filter by class_id. Here we filter by a mock 'class_name' column or similar logic
        return db.rawQuery("SELECT user_id, full_name, roll_no FROM " + TABLE_USERS + " WHERE role='Student'", null);
    }

    // ==========================================
    //           EXPENSE METHODS
    // ==========================================

    public boolean addExpense(String title, String category, double amount, String date, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("category", category);
        values.put("amount", amount);
        values.put("date", date);
        values.put("description", desc);
        return db.insert(TABLE_EXPENSES, null, values) != -1;
    }
    public String getStudentClass(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Assuming 'class_name' or 'grade_level' is stored in users table for students
        // You might need to add this column to TABLE_USERS if not present,
        // or query a 'student_class_link' table.
        // For this demo, we mock it or assume it's in a column 'department' or similar reused field.
        Cursor cursor = db.rawQuery("SELECT email FROM " + TABLE_USERS + " WHERE user_id = ?", new String[]{studentId});
        // *Correction*: In a real app, ensure 'class_name' column exists.
        // Returning a hardcoded class for the demo to ensure it works with the schedule we created.
        return "Grade 10-A";
    }

    public Cursor getClassSchedule(String className, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TIMETABLE + " WHERE class_name = ? AND day_of_week = ? ORDER BY start_time ASC",
                new String[]{className, day});
    }

    // ==========================================
    //         ADMIN FEES & SETTINGS
    // ==========================================

    public double getTotalFeesCollected() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_FEE_PAYMENTS, null);
        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(0);
            cursor.close();
            return total;
        }
        cursor.close();
        return 0;
    }

    public Cursor getRecentFeeTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FEE_PAYMENTS + " ORDER BY date DESC LIMIT 20", null);
    }

    public boolean updatePassword(String userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword); // In real app, hash this!
        return db.update(TABLE_USERS, values, "user_id = ?", new String[]{userId}) > 0;
    }

    // ==========================================
    //           USER DIRECTORY METHODS
    // ==========================================

    public Cursor getUsersByRole(String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE role = ? ORDER BY full_name ASC", new String[]{role});
    }

    public boolean deleteUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, "user_id = ?", new String[]{userId}) > 0;
    }

    // ==========================================
    //           PROFILE & USER DETAILS
    // ==========================================

    public Cursor getUserDetails(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE user_id = ?", new String[]{userId});
    }

    public boolean updateUserProfile(String userId, String name, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", name);
        values.put("email", email);
        values.put("phone_number", phone); // Assuming this column exists, else add it in onCreate
        return db.update(TABLE_USERS, values, "user_id = ?", new String[]{userId}) > 0;
    }

    // Method to Enable 2FA
    public void enable2FA(String userId, String secretKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_2fa_enabled", 1);
        values.put("secret_key", secretKey);
        db.update(TABLE_USERS, values, "user_id = ?", new String[]{userId});
    }

    // Method to Add Student with Previous Education
    public boolean addStudentWithHistory(String name, String id, String prevSchool, String transferCert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", name);
        values.put("user_id", id);
        values.put("role", "Student");
        values.put("previous_school", prevSchool);
        values.put("transfer_cert_no", transferCert);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public Cursor getAllStudentsFeeStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.user_id, u.full_name, " +
                "COALESCE(SUM(f.amount), 0) as total_due, " +
                "(SELECT COALESCE(SUM(p.amount), 0) FROM fee_payments p WHERE p.student_id = u.user_id) as total_paid " +
                "FROM users u " +
                "LEFT JOIN fees f ON u.user_id = f.student_id " +
                "WHERE u.role = 'Student' " +
                "GROUP BY u.user_id";
        return db.rawQuery(query, null);
    }


    public boolean addPayment(String studentId, String collectedBy, double amount, String method) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("collected_by", collectedBy);
        values.put("amount", amount);
        values.put("payment_method", method);
        values.put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        return db.insert(TABLE_FEE_PAYMENTS, null, values) != -1;
    }
    public Cursor getStudentInvoices(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Query the fees table
        return db.rawQuery("SELECT * FROM fees WHERE student_id = ? ORDER BY date DESC", new String[]{studentId});
    }
    public double getStudentTotalPaid(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM fee_payments WHERE student_id = ?", new String[]{studentId});
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public boolean deleteSchedule(String scheduleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("timetable", "schedule_id = ?", new String[]{scheduleId}) > 0;
    }


    public Cursor getStudentAttendance(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE + " WHERE student_id = ?", new String[]{studentId});
    }


    public Cursor getAllExamCategories() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_EXAM_CATEGORIES + " ORDER BY start_date DESC", null);
    }

    public void deleteExamCategory(String examId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXAM_CATEGORIES, "exam_id = ?", new String[]{examId});
    }

    // --- 2. Exam Schedule ---
    public long addExamSchedule(String examId, String className, String subject, String date, String time, String room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("exam_id", examId);
        values.put("class_name", className);
        values.put("subject", subject);
        values.put("date", date);
        values.put("start_time", time);
        values.put("room_no", room);
        return db.insert(TABLE_EXAM_SCHEDULE, null, values);
    }

    // --- 3. Marks Entry ---
    public void saveExamMark(String examId, String studentId, String subject, int score, int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("exam_id", examId);
        values.put("student_id", studentId);
        values.put("subject", subject);
        values.put("score", score);
        values.put("total_marks", total);

        // Check if mark exists
        Cursor cursor = db.rawQuery("SELECT mark_id FROM " + TABLE_EXAM_MARKS +
                " WHERE exam_id=? AND student_id=? AND subject=?", new String[]{examId, studentId, subject});

        if (cursor.moveToFirst()) {
            // Update
            String id = cursor.getString(0);
            db.update(TABLE_EXAM_MARKS, values, "mark_id=?", new String[]{id});
        } else {
            // Insert
            db.insert(TABLE_EXAM_MARKS, null, values);
        }
        cursor.close();
    }

    // Get marks for a specific class/subject to populate the entry list
    public Cursor getStudentsWithMarks(String className, String examId, String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Join Users with Marks (Left Join so we see all students even if no mark yet)
        // Assuming className in Users is stored as "Grade 10-Emerald" matching your spinner
        // You might need to adjust the join condition based on how you store class_id vs class_name
        // Ideally, store class_id. For this fix, we assume the user table stores 'class_id' and we need to resolve it
        // OR we just query by class_id if we have it.

        // Simpler approach for this specific schema:
        // 1. Get Class ID from Class Name (Helper needed or logic in Activity)
        // 2. Query users.

        // Let's do a raw query assuming we pass the raw Class Name for now (or handle mapping in Activity)
        String query = "SELECT u.user_id, u.full_name, m.score " +
                "FROM " + TABLE_USERS + " u " +
                "LEFT JOIN " + TABLE_EXAM_MARKS + " m ON u.user_id = m.student_id " +
                "AND m.exam_id = ? AND m.subject = ? " +
                "LEFT JOIN " + TABLE_CLASSES + " c ON u.class_id = c.class_id " +
                "WHERE (c.grade_level || '-' || c.section_name) = ? " +
                "AND u.role = 'Student' " +
                "ORDER BY u.full_name ASC";

        return db.rawQuery(query, new String[]{examId, subject, className});
    }

    // ==========================================
    //       ACADEMIC SESSION METHODS (NEW)
    // ==========================================

    public long addAcademicSession(String name, String start, String end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("session_name", name);
        values.put("start_date", start);
        values.put("end_date", end);
        values.put("is_active", 0);
        return db.insert("academic_sessions", null, values);
    }

    public Cursor getAllSessions() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Order by active first, then by start date descending
        return db.rawQuery("SELECT * FROM " + TABLE_SESSIONS + " ORDER BY is_active DESC, start_date DESC", null);
    }

    // Removed Duplicate addAcademicSession(String name, String start, String end) method

    public void setSessionActive(String sessionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. Deactivate all
            ContentValues disable = new ContentValues();
            disable.put("is_active", 0);
            db.update(TABLE_SESSIONS, disable, null, null);

            // 2. Activate selected
            ContentValues enable = new ContentValues();
            enable.put("is_active", 1);
            db.update(TABLE_SESSIONS, enable, "session_id = ?", new String[]{sessionId});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteSession(String sessionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SESSIONS, "session_id = ?", new String[]{sessionId});
    }

    // [Add this method to DatabaseHelper.java]

    // ==========================================
    //       ACADEMIC SESSION METHODS
    // ==========================================

    public boolean updateAcademicSession(String sessionId, String name, String start, String end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("session_name", name);
        values.put("start_date", start);
        values.put("end_date", end);

        int result = db.update(TABLE_SESSIONS, values, "session_id = ?", new String[]{sessionId});
        return result > 0;
    }


    // 2. Fetch distinct subjects for a specific class from the Timetable
    public List<String> getSubjectsForClass(int classId) {
        List<String> subjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Select distinct subjects linked to this class ID
        Cursor cursor = db.rawQuery("SELECT DISTINCT subject FROM timetable WHERE class_id = ? ORDER BY subject ASC",
                new String[]{String.valueOf(classId)});

        if (cursor.moveToFirst()) {
            do {
                subjects.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return subjects;
    }

    // ==========================================
    //           SUBJECT & ENROLLMENT METHODS
    // ==========================================

    // Get Subjects available for a specific grade
    public Cursor getSubjectsByGrade(String gradeLevel) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SUBJECTS + " WHERE grade_level = ?", new String[]{gradeLevel});
    }

    // Enroll Student (Basic Info)
    public boolean enrollStudent(String name, String id, int classId, String prevSchool, String transferCert, String email, String emergencyContact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", name);
        values.put("user_id", id);
        values.put("role", "Student");
        values.put("class_id", classId);
        values.put("previous_school", prevSchool);
        values.put("transfer_cert_no", transferCert);
        values.put("email", email);
        values.put("emergency_contact_name", emergencyContact);
        values.put("status", "Active");
        values.put("password_hash", SecurityUtil.hashPassword("123456"));

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Enroll Student in Specific Subject
    public void enrollStudentInSubject(String studentId, int subjectId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("subject_id", subjectId);
        values.put("enrollment_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        db.insert(TABLE_STUDENT_SUBJECTS, null, values);
    }

    // Add Fee Record (Used when enrolling in a subject)
    public void addFee(String studentId, String description, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("student_id", studentId);
        values.put("description", description);
        values.put("amount", amount);
        values.put("type", type);
        values.put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        db.insert(TABLE_FEES, null, values);
    }

    // Helper to get all classes
    public Cursor getAllClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT class_id, grade_level, section_name FROM " + TABLE_CLASSES, null);
    }

    public Cursor getAllStudentsWithClassDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.user_id, u.full_name, u.roll_no, u.status, c.grade_level, c.section_name " +
                "FROM " + TABLE_USERS + " u " +
                "LEFT JOIN " + TABLE_CLASSES + " c ON u.class_id = c.class_id " +
                "WHERE u.role = 'Student' " +
                "ORDER BY u.full_name ASC";
        return db.rawQuery(query, null);
    }

    // [Add this inside DatabaseHelper.java]

// ==========================================
//       ELIGIBILITY & PROMOTION CHECK
// ==========================================

    // 1. Check Attendance Percentage
    public double getAttendancePercentage(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Count total days
        Cursor totalCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ATTENDANCE + " WHERE student_id = ?", new String[]{studentId});
        int totalDays = 0;
        if (totalCursor.moveToFirst()) {
            totalDays = totalCursor.getInt(0);
        }
        totalCursor.close();

        if (totalDays == 0) return 100.0; // Default to 100% if no records yet

        // Count present days
        Cursor presentCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ATTENDANCE + " WHERE student_id = ? AND status = 'Present'", new String[]{studentId});
        int presentDays = 0;
        if (presentCursor.moveToFirst()) {
            presentDays = presentCursor.getInt(0);
        }
        presentCursor.close();

        return ((double) presentDays / totalDays) * 100.0;
    }

    // 2. Check Outstanding Fees
    public double getOutstandingBalance(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Total Fees Charged
        Cursor feeCursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_FEES + " WHERE student_id = ?", new String[]{studentId});
        double totalFees = 0;
        if (feeCursor.moveToFirst()) {
            totalFees = feeCursor.getDouble(0);
        }
        feeCursor.close();

        // Total Paid
        Cursor paidCursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_FEE_PAYMENTS + " WHERE student_id = ?", new String[]{studentId});
        double totalPaid = 0;
        if (paidCursor.moveToFirst()) {
            totalPaid = paidCursor.getDouble(0);
        }
        paidCursor.close();

        return totalFees - totalPaid;
    }

    // 3. Check for Failing Grades (Score < 75)
    public boolean hasFailedSubjects(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Assuming 75 is the passing mark
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_GRADES + " WHERE student_id = ? AND score < 75", new String[]{studentId});

        boolean hasFailures = false;
        if (cursor.moveToFirst()) {
            hasFailures = cursor.getInt(0) > 0;
        }
        cursor.close();
        return hasFailures;
    }

    // 4. Master Eligibility Check
    public EligibilityResult checkEnrollmentEligibility(String studentId) {
        double attendance = getAttendancePercentage(studentId);
        double balance = getOutstandingBalance(studentId);
        boolean hasFailed = hasFailedSubjects(studentId);

        // Criteria: Attendance > 80%, Balance <= 0, No Failed Subjects
        boolean isEligible = (attendance >= 80.0) && (balance <= 0) && (!hasFailed);

        return new EligibilityResult(isEligible, attendance, balance, hasFailed);
    }

    // Helper Class for results
    public static class EligibilityResult {
        public boolean isEligible;
        public double attendancePercent;
        public double outstandingBalance;
        public boolean hasFailedSubjects;

        public EligibilityResult(boolean isEligible, double attendance, double balance, boolean hasFailed) {
            this.isEligible = isEligible;
            this.attendancePercent = attendance;
            this.outstandingBalance = balance;
            this.hasFailedSubjects = hasFailed;
        }
    }
}
