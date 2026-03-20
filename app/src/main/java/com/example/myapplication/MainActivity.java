package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnYes = findViewById(R.id.btnYes);
        Button btnNo = findViewById(R.id.btnNo);
        TextView tvExit = findViewById(R.id.tvExit);
        btnYes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginSelectionActivity.class);
            startActivity(intent);

        });
        btnNo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationSelectionActivity.class);
            startActivity(intent);

        });
        tvExit.setOnClickListener(v -> finishAffinity());
    }
}