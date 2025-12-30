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

        // Back Button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish()); // Ensure header ImageView has this ID

        // Add Book FAB
        CardView fabAdd = findViewById(R.id.fab_add_book);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, LibraryAddBookActivity.class));
        });

        // Search Listener
        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadBooks(s.toString());
            }
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks("");
    }

    private void loadBooks(String query) {
        llBookList.removeAllViews();
        Cursor cursor = db.getAllBooks(); // You might want to add a search specific query to DB Helper later

        LayoutInflater inflater = LayoutInflater.from(this);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow("isbn"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

                // Filter logic (simple java filter)
                if (!query.isEmpty() && !title.toLowerCase().contains(query.toLowerCase())
                        && !isbn.contains(query)) {
                    continue;
                }

                View itemView = inflater.inflate(R.layout.item_library_book, llBookList, false);

                TextView tvTitle = itemView.findViewById(R.id.tv_book_title);
                TextView tvDetails = itemView.findViewById(R.id.tv_book_author_isbn);
                TextView tvStatus = itemView.findViewById(R.id.tv_book_status);

                tvTitle.setText(title);
                tvDetails.setText(author + " • ISBN: " + isbn);

                if (quantity > 0) {
                    tvStatus.setText("AVAILABLE (" + quantity + ")");
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#05CD99")); // Green
                    tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#E6FFF5"));
                } else {
                    tvStatus.setText("OUT OF STOCK");
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#FF9800")); // Orange
                    tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#FFF3E0"));
                }

                llBookList.addView(itemView);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}