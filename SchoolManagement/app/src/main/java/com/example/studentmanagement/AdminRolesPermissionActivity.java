package com.example.studentmanagement;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminRolesPermissionActivity extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout rolesContainer;

    // Predefined Permission Sets for the Dropdown
    private final String[] PERMISSION_SETS = {
            "Full System Access & Configuration",
            "Class Management, Grading, Attendance",
            "Fee Collection & Expense Management",
            "View Schedule, Grades, and Fees (Read Only)",
            "Library Management Only",
            "Custom Restricted Access"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_roles_permission);

        db = new DatabaseHelper(this);
        rolesContainer = findViewById(R.id.roles_container);

        // Back Button
        findViewById(R.id.header).setOnClickListener(v -> finish());

        // Add Role FAB
        findViewById(R.id.btn_add_role).setOnClickListener(v -> showRoleDialog(false, "", ""));

        loadRoles();
    }

    private void loadRoles() {
        rolesContainer.removeAllViews();
        Cursor cursor = db.getAllRoles();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String roleName = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                addRoleCard(roleName, description);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void addRoleCard(String name, String desc) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_role_card, rolesContainer, false);

        TextView tvName = view.findViewById(R.id.tv_role_name);
        TextView tvDesc = view.findViewById(R.id.tv_role_desc);
        TextView btnEdit = view.findViewById(R.id.btn_edit_role);

        tvName.setText(name);
        tvDesc.setText(desc);

        // Edit Button Logic
        btnEdit.setOnClickListener(v -> showRoleDialog(true, name, desc));

        rolesContainer.addView(view);
    }

    private void showRoleDialog(boolean isEdit, String oldName, String oldDesc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_role, null);
        builder.setView(view);

        EditText etName = view.findViewById(R.id.et_role_name);
        Spinner spinnerPerms = view.findViewById(R.id.spinner_permissions);
        Button btnConfirm = view.findViewById(R.id.btn_save_role);
        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);

        // 1. Setup Spinner Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PERMISSION_SETS);
        spinnerPerms.setAdapter(adapter);

        if (isEdit) {
            tvTitle.setText("Edit Role");
            etName.setText(oldName);

            // Try to pre-select the existing permission if it matches one of our sets
            for (int i = 0; i < PERMISSION_SETS.length; i++) {
                if (PERMISSION_SETS[i].equals(oldDesc)) {
                    spinnerPerms.setSelection(i);
                    break;
                }
            }
        } else {
            tvTitle.setText("Add New Role");
        }

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btnConfirm.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String selectedPerms = spinnerPerms.getSelectedItem().toString();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Role Name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Confirmation Alert (The "Follow up function")
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Changes")
                    .setMessage("Are you sure you want to " + (isEdit ? "update" : "create") + " this role with these permissions?")
                    .setPositiveButton("Yes, Confirm", (confirmDialog, which) -> {
                        // Perform DB Operation
                        boolean success;
                        if (isEdit) {
                            success = db.updateRole(oldName, newName, selectedPerms);
                        } else {
                            success = db.addRole(newName, selectedPerms);
                        }

                        if (success) {
                            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                            loadRoles();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(this, "Operation Failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        dialog.show();
    }
}