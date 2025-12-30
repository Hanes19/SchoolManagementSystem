package com.example.studentmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LibraryEResourcesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout llResourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_e_resources);

        db = new DatabaseHelper(this);
        // Important: Ensure you added this ID to the LinearLayout inside ScrollView in XML
        llResourceList = findViewById(R.id.ll_resource_list);
        // Note: You might need to manually add android:id="@+id/ll_resource_list"
        // to the LinearLayout inside the ScrollView in library_e_resources.xml if not present.

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        CardView fabAdd = findViewById(R.id.fab_add_resource);
        fabAdd.setOnClickListener(v -> startActivity(new Intent(this, LibraryAddEResourceActivity.class)));

        loadResources();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadResources();
    }

    private void loadResources() {
        if(llResourceList == null) return;

        llResourceList.removeAllViews();
        Cursor cursor = db.getAllEResources();

        if (cursor.moveToFirst()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));

                // Create a simple card view programmatically or inflate a layout
                // Here we reuse item_library_fine.xml structure but change icons/colors roughly
                // Or create item_library_resource.xml for better UI

                View itemView = inflater.inflate(R.layout.item_library_fine, llResourceList, false);
                TextView tvTitle = itemView.findViewById(R.id.tv_student_name);
                TextView tvCat = itemView.findViewById(R.id.tv_fine_details);
                TextView tvType = itemView.findViewById(R.id.tv_fine_amount); // Reusing as Type label

                tvTitle.setText(title);
                tvCat.setText(category);
                tvType.setText("OPEN");
                tvType.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

                llResourceList.addView(itemView);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}