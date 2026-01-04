package com.example.studentmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LibraryBookCatalogActivity extends AppCompatActivity {

    private LinearLayout bookListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_book_catalog);

        setupHeader();
        findContainer();
        loadSampleBooks();
    }

    private void setupHeader() {
        ViewGroup root = findViewById(android.R.id.content);
        LinearLayout header = findHeaderLayout(root);
        if (header != null) {
            // Back button logic (usually first child)
            if (header.getChildCount() > 0) {
                header.getChildAt(0).setOnClickListener(v -> finish());
            }
            // Title logic
            for (int i = 0; i < header.getChildCount(); i++) {
                if (header.getChildAt(i) instanceof TextView) {
                    ((TextView) header.getChildAt(i)).setText("Book Catalog");
                    break;
                }
            }
        }
    }

    private void findContainer() {
        ViewGroup root = findViewById(android.R.id.content);
        ScrollView scrollView = findScrollView(root);
        if (scrollView != null && scrollView.getChildCount() > 0) {
            View child = scrollView.getChildAt(0);
            if (child instanceof LinearLayout) {
                bookListContainer = (LinearLayout) child;
            }
        }
    }

    private void loadSampleBooks() {
        if (bookListContainer == null) return;

        // Populate with mock data
        addBookCard("Harry Potter", "J.K. Rowling", "Available", "#4CAF50");
        addBookCard("Clean Code", "Robert C. Martin", "Borrowed", "#FF9800");
        addBookCard("The Hobbit", "J.R.R. Tolkien", "Available", "#4CAF50");
        addBookCard("Physics Vol 1", "H.C. Verma", "Reserved", "#2196F3");
        addBookCard("Chemistry 101", "Walter White", "Available", "#4CAF50");
    }

    private void addBookCard(String title, String author, String status, String colorHex) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setRadius(20);
        card.setCardElevation(4);
        card.setCardBackgroundColor(Color.WHITE);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(32, 32, 32, 32);
        inner.setGravity(Gravity.CENTER_VERTICAL);

        // Book Icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(android.R.drawable.ic_menu_agenda); // Generic book icon
        icon.setColorFilter(Color.parseColor("#1B254B"));
        icon.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        inner.addView(icon);

        // Text Info
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(32, 0, 0, 0);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setTextColor(Color.parseColor("#1B254B"));

        TextView tvAuthor = new TextView(this);
        tvAuthor.setText(author);
        tvAuthor.setTextSize(14);
        tvAuthor.setTextColor(Color.GRAY);

        textLayout.addView(tvTitle);
        textLayout.addView(tvAuthor);
        inner.addView(textLayout);

        // Status Badge
        TextView tvStatus = new TextView(this);
        tvStatus.setText(status);
        tvStatus.setTextSize(12);
        tvStatus.setTextColor(Color.WHITE);
        tvStatus.setBackgroundColor(Color.parseColor(colorHex));
        tvStatus.setPadding(16, 8, 16, 8);

        inner.addView(tvStatus);
        card.addView(inner);
        bookListContainer.addView(card);
    }

    // --- Helpers ---
    private LinearLayout findHeaderLayout(View view) {
        if (view instanceof LinearLayout) return (LinearLayout) view;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                LinearLayout res = findHeaderLayout(group.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }

    private ScrollView findScrollView(View view) {
        if (view instanceof ScrollView) return (ScrollView) view;
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                ScrollView res = findScrollView(group.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }
}