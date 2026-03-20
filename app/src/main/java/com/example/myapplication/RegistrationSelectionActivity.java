package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_selection);


        Button btnStudentRole = findViewById(R.id.btnStudentRole);
        Button btnAdminRole = findViewById(R.id.btnAdminRole);
        TextView tvBackToHome = findViewById(R.id.tvBackToHome);

        btnStudentRole.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationSelectionActivity.this, StudentRegistrationActivity.class);
            startActivity(intent);
        });

        btnAdminRole.setOnClickListener(v -> {
              Intent intent = new Intent(RegistrationSelectionActivity.this, AdminRegistrationActivity.class);
                      startActivity(intent);
        });


        tvBackToHome.setOnClickListener(v -> finish());
    }
}