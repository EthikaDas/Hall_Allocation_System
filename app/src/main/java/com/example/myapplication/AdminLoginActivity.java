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

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        EditText etEmail = findViewById(R.id.etAdminLoginEmail);
        EditText etPassword = findViewById(R.id.etAdminLoginPassword);
        Button btnLogin = findViewById(R.id.btnAdminLogin);
        TextView tvCancel = findViewById(R.id.tvAdminCancelLogin);

        tvCancel.setOnClickListener(v -> finish());

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", email);
                jsonBody.put("password", password);


                performAdminLogin(jsonBody, email);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




        private void performAdminLogin(JSONObject jsonBody, String email) {
            String url = "http://10.0.2.2:8080/admin/login";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            if (success) {
                                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);

                                intent.putExtra("ADMIN_EMAIL", email);
                                startActivity(intent);

                                finish();
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Response parsing error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(this, "Login Failed: Check connection or backend", Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);
        }
    }