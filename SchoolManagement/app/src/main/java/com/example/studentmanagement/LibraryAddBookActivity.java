package com.example.studentmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LibraryAddBookActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etTitle, etAuthor, etIsbn, etCopies, etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_add_new_book);

        db = new DatabaseHelper(this);

        // Bind Views (Ensure these IDs exist in your XML)
        etTitle = findViewById(R.id.et_book_title);
        etAuthor = findViewById(R.id.et_book_author);
        etIsbn = findViewById(R.id.et_isbn);
        etCopies = findViewById(R.id.et_copies);
        etLocation = findViewById(R.id.et_location);

        // Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Save Button
        CardView btnSave = findViewById(R.id.btn_save_container);
        btnSave.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String isbn = etIsbn.getText().toString().trim();
        String copiesStr = etCopies.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = "General"; // You can get this from a Spinner if implemented

        // Validation
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(isbn) || TextUtils.isEmpty(copiesStr)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int copies = Integer.parseInt(copiesStr);

        // Database Insert
        boolean isInserted = db.addBook(title, author, isbn, category, copies, location);

        if (isInserted) {
            Toast.makeText(this, "Book Added Successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Go back to dashboard
        } else {
            Toast.makeText(this, "Error: ISBN might already exist", Toast.LENGTH_LONG).show();
        }
    }
}