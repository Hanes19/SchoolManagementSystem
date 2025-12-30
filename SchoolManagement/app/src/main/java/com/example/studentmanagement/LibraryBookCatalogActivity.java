package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LibraryBookCatalogActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llBookList;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_book_catalog);

        db = new DatabaseHelper(this);
        llBookList = findViewById(R.id.ll_book_list);
        etSearch = findViewById(R.id.et_search);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        CardView fabAdd = findViewById(R.id.fab_add_book);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, LibraryAddBookActivity.class));
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadBooks(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks(""); // Refresh list when returning from "Add Book"
    }

    private void loadBooks(String query) {
        llBookList.removeAllViews();
        // Uses the getAllBooks method you likely have or need to add to DatabaseHelper
        // If DatabaseHelper doesn't have a search method, use rawQuery:
        Cursor cursor = db.getReadableDatabase().rawQuery(
                "SELECT * FROM library_books WHERE title LIKE ? OR author LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"}
        );

        if (cursor.moveToFirst()) {
            do {
                // Inflate your item_library_book.xml
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_library_book, llBookList, false);

                TextView tvTitle = itemView.findViewById(R.id.tv_book_title); // Ensure IDs match XML
                TextView tvAuthor = itemView.findViewById(R.id.tv_book_author);
                TextView tvQty = itemView.findViewById(R.id.tv_book_qty);

                String title = cursor.getString(cursor.getColumnIndex("title"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int qty = cursor.getInt(cursor.getColumnIndex("quantity"));

                tvTitle.setText(title);
                tvAuthor.setText(author);
                tvQty.setText("Avail: " + qty);

                llBookList.addView(itemView);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}