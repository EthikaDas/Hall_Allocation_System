package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class AdminRegistrationActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);

        etName = findViewById(R.id.etAdminName);
        etEmail = findViewById(R.id.etAdminEmail);
        etPassword = findViewById(R.id.etAdminPassword);
        btnRegister = findViewById(R.id.btnRegisterAdmin);
        tvCancel = findViewById(R.id.tvAdminCancel);

        tvCancel.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("name", name);
                jsonBody.put("email", email);
                jsonBody.put("password", password);

                registerAdmin(jsonBody);

            } catch (Exception e) {
                Toast.makeText(this,
                        "Error creating request",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerAdmin(JSONObject jsonBody) {

        String url = "http://10.0.2.2:8080/admin/register";

        JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonBody,

                        response -> {
                            Toast.makeText(this, "Admin Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminRegistrationActivity.this, AdminDashboardActivity.class);
                            intent.putExtra("ADMIN_EMAIL", etEmail.getText().toString().trim());
                            startActivity(intent);

                            finish();
                        },

                        error -> {
                            Toast.makeText(this,
                                    "Connection Error - Check Backend",
                                    Toast.LENGTH_SHORT).show();
                        }
                );

        Volley.newRequestQueue(this).add(request);
    }
}