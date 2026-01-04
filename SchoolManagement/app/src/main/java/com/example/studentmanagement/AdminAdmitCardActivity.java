package com.example.studentmanagement;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminAdmitCardActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spExam, spClass;
    private LinearLayout llList;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_admit_cards);

        db = new DatabaseHelper(this);
        spExam = findViewById(R.id.sp_exam_select);
        spClass = findViewById(R.id.sp_class_select);
        llList = findViewById(R.id.ll_student_list);

        findViewById(R.id.btn_back_admit).setOnClickListener(v -> finish());
        findViewById(R.id.btn_generate).setOnClickListener(v -> generateList());

        setupSpinners();
        checkPermissions();
    }

    private void setupSpinners() {
        // Load Exam Categories
        List<String> exams = new ArrayList<>();
        Cursor cExams = db.getAllExamCategories();
        if (cExams != null && cExams.moveToFirst()) {
            do {
                exams.add(cExams.getString(1)); // exam_name
            } while (cExams.moveToNext());
            cExams.close();
        }
        if (exams.isEmpty()) { exams.add("Midterm 2025"); exams.add("Finals 2025"); }

        ArrayAdapter<String> examAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, exams);
        examAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExam.setAdapter(examAdapter);

        // Load Classes
        List<String> classes = new ArrayList<>();
        Cursor cClasses = db.getAllClasses();
        if (cClasses != null && cClasses.moveToFirst()) {
            do {
                classes.add(cClasses.getString(1) + "-" + cClasses.getString(2));
            } while (cClasses.moveToNext());
            cClasses.close();
        }
        if (classes.isEmpty()) { classes.add("Grade 10-A"); }

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, classes);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(classAdapter);
    }

    private void generateList() {
        llList.removeAllViews();

        if (spClass.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a class", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedClassFull = spClass.getSelectedItem().toString();
        String selectedExam = spExam.getSelectedItem().toString();

        Cursor cursor = db.getStudentsForAdmitCard(selectedClassFull);
        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Fetch student details
                String name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                String roll = cursor.getString(cursor.getColumnIndexOrThrow("roll_no"));
                // Fallback for null roll numbers
                if (roll == null) roll = "N/A";

                View view = inflater.inflate(R.layout.item_admit_card_row, llList, false);

                TextView tvName = view.findViewById(R.id.tv_student_name);
                TextView tvRoll = view.findViewById(R.id.tv_roll_no);
                View btnDownload = view.findViewById(R.id.btn_download_card);

                tvName.setText(name);
                tvRoll.setText("Roll No: " + roll);

                // --- PDF GENERATION LOGIC ---
                String finalRoll = roll;
                btnDownload.setOnClickListener(v -> {
                    generateAdmitCardPdf(name, finalRoll, selectedExam, selectedClassFull);
                });

                llList.addView(view);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No students found", Toast.LENGTH_SHORT).show();
        }
    }

    // ==========================================
    //            PDF GENERATION LOGIC
    // ==========================================
    private void generateAdmitCardPdf(String studentName, String rollNo, String examName, String className) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        // 1. Create a Page (A4 width approx 595, height 842)
        // We will make a smaller card size: 595 width x 400 height
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 400, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // 2. Draw Header (School Name)
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(24);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("SCHOOL MANAGEMENT SYSTEM", 297, 50, titlePaint);

        // 3. Draw Sub-header (Exam Name)
        titlePaint.setTextSize(18);
        titlePaint.setFakeBoldText(false);
        canvas.drawText("Admit Card: " + examName, 297, 80, titlePaint);

        // 4. Draw Divider Line
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);
        canvas.drawLine(50, 100, 545, 100, paint);

        // 5. Draw Student Details
        paint.setColor(Color.BLACK);
        paint.setTextSize(16);

        int leftMargin = 60;
        int startY = 150;
        int lineHeight = 30;

        // Label Column
        paint.setFakeBoldText(true);
        canvas.drawText("Student Name:", leftMargin, startY, paint);
        canvas.drawText("Roll Number:", leftMargin, startY + lineHeight, paint);
        canvas.drawText("Class:", leftMargin, startY + (lineHeight * 2), paint);
        canvas.drawText("Examination:", leftMargin, startY + (lineHeight * 3), paint);

        // Value Column
        paint.setFakeBoldText(false);
        int valueMargin = 200;
        canvas.drawText(studentName, valueMargin, startY, paint);
        canvas.drawText(rollNo, valueMargin, startY + lineHeight, paint);
        canvas.drawText(className, valueMargin, startY + (lineHeight * 2), paint);
        canvas.drawText(examName, valueMargin, startY + (lineHeight * 3), paint);

        // 6. Draw Box Border around card
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4);
        borderPaint.setColor(Color.BLACK);
        canvas.drawRect(20, 20, 575, 380, borderPaint);

        // 7. Footer Instructions
        paint.setTextSize(12);
        paint.setColor(Color.DKGRAY);
        canvas.drawText("Instructions: Please bring this card to the exam hall.", 297, 350, titlePaint); // Centered using titlePaint align

        pdfDocument.finishPage(page);

        // 8. Save File
        String fileName = "AdmitCard_" + studentName.replaceAll("\\s+", "") + ".pdf";

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (Scoped Storage)
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                if (uri != null) {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    pdfDocument.writeTo(outputStream);
                    if (outputStream != null) outputStream.close();
                    Toast.makeText(this, "Saved to Downloads: " + fileName, Toast.LENGTH_LONG).show();
                }
            } else {
                // Older Android Versions
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;
                java.io.File file = new java.io.File(filePath);
                pdfDocument.writeTo(new java.io.FileOutputStream(file));
                Toast.makeText(this, "Saved to: " + filePath, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }

    // Check permissions for older devices
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied. Cannot save PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}