package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);


        Button btnStudentRole = findViewById(R.id.btnStudentRole);
        Button btnAdminRole = findViewById(R.id.btnAdminRole);
        TextView tvBackToHome = findViewById(R.id.tvBackToHome);


        btnStudentRole.setOnClickListener(v -> {
           Intent intent = new Intent(LoginSelectionActivity.this, StudentLoginActivity.class);
            startActivity(intent);
        });


        btnAdminRole.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSelectionActivity.this, AdminLoginActivity.class);
             startActivity(intent);
        });


        tvBackToHome.setOnClickListener(v -> {
            finish();
        });
    }
}