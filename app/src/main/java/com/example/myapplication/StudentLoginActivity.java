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

public class StudentLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);


        EditText etRoll = findViewById(R.id.etLoginRoll);
        EditText etPassword = findViewById(R.id.etLoginPassword);
        Button btnLogin = findViewById(R.id.btnLoginStudent);
        TextView tvRegister = findViewById(R.id.tvGoToRegister);


        tvRegister.setOnClickListener(v -> finish());


        btnLogin.setOnClickListener(v -> {
            String rollStr = etRoll.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (rollStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both roll and password", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("roll", Integer.parseInt(rollStr));
                jsonBody.put("password", password);

                performLogin(jsonBody);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Roll must be a number", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void performLogin(JSONObject jsonBody) {
        String url = "http://10.0.2.2:8080/student/login";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");

                        if (success) {
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(StudentLoginActivity.this, StudentDashboardActivity.class);


                            intent.putExtra("STUDENT_ROLL", jsonBody.getInt("roll"));


                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Response Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Login Failed: Check Backend Connection", Toast.LENGTH_LONG).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}