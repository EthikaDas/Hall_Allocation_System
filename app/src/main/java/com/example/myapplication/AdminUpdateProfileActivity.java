package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminUpdateProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private String adminEmailFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_profile);

        adminEmailFromIntent = getIntent().getStringExtra("ADMIN_EMAIL");

        etName = findViewById(R.id.etUpdateAdminName);
        etEmail = findViewById(R.id.etUpdateAdminEmail);
        etPassword = findViewById(R.id.etUpdateAdminPassword);
        Button btnSave = findViewById(R.id.btnSaveProfile);

        findViewById(R.id.btnBackCard).setOnClickListener(v -> finish());


        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        String url = "http://10.0.2.2:8080/admin/update-profile";
        JSONObject updateData = new JSONObject();

        try {

            updateData.put("oldEmail", getIntent().getStringExtra("ADMIN_EMAIL"));


            updateData.put("name", etName.getText().toString().trim());
            updateData.put("email", etEmail.getText().toString().trim()); // NEW email
            updateData.put("password", etPassword.getText().toString().trim());
        } catch (JSONException e) { e.printStackTrace(); }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, updateData,
                response -> {
                    Toast.makeText(this, "Profile and Email updated! Please login again.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, AdminLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                },
                error -> Toast.makeText(this, "Update Error", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }
}