package com.example.studentmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StaffFeesCollectionActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etStudentId, etAmount;
    private TextView tvStudentName;
    private RadioGroup rgMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_fees_collection); // Ensure XML name matches

        db = new DatabaseHelper(this);

        etStudentId = findViewById(R.id.et_student_id);
        etAmount = findViewById(R.id.et_amount);
        tvStudentName = findViewById(R.id.tv_student_name);
        rgMethod = findViewById(R.id.rg_payment_method);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Verify Student Button
        findViewById(R.id.btn_verify_student).setOnClickListener(v -> {
            String id = etStudentId.getText().toString().trim();
            if (!id.isEmpty()) {
                String name = db.getStudentName(id);
                if (name != null) {
                    tvStudentName.setText(name);
                    tvStudentName.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Student Not Found", Toast.LENGTH_SHORT).show();
                    tvStudentName.setVisibility(View.GONE);
                }
            }
        });

        // Collect Button
        findViewById(R.id.btn_collect_fee).setOnClickListener(v -> {
            String id = etStudentId.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();

            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(amountStr)) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String method = "Cash";
            int selectedId = rgMethod.getCheckedRadioButtonId();
            // Assuming IDs for radio buttons exist like rb_card, rb_online
            // if (selectedId == R.id.rb_card) method = "Card";

            if (db.collectFee(id, "stf001", Double.parseDouble(amountStr), method)) {
                Toast.makeText(this, "Payment Recorded Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}