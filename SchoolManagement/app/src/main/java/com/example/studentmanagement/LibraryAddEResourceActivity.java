package com.example.studentmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LibraryAddEResourceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etTitle, etCategory, etUrl;
    private RadioGroup rgType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_add_e_resources);

        db = new DatabaseHelper(this);

        etTitle = findViewById(R.id.et_resource_title);
        etCategory = findViewById(R.id.et_category);
        etUrl = findViewById(R.id.et_url);
        rgType = findViewById(R.id.rg_type);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        CardView btnPublish = findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(v -> saveResource());
    }

    private void saveResource() {
        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String url = etUrl.getText().toString().trim();

        String type = "PDF";
        int selectedId = rgType.getCheckedRadioButtonId();
        if (selectedId == R.id.rb_video) type = "Video";
        else if (selectedId == R.id.rb_link) type = "Link";

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) {
            Toast.makeText(this, "Title and URL/Link are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.addEResource(title, category, type, url)) {
            Toast.makeText(this, "Resource Published!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving resource", Toast.LENGTH_SHORT).show();
        }
    }
}