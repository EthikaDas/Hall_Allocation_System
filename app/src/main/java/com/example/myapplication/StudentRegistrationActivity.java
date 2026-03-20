package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cancel Registration");
        }

        TextView tvCancel = findViewById(R.id.tvCancel);


        tvCancel.setOnClickListener(v -> {
            finish();
        });


        EditText etRoll = findViewById(R.id.etRoll);
        EditText etName = findViewById(R.id.etName);
        EditText etSeries = findViewById(R.id.etSeries);
        EditText etDept = findViewById(R.id.etDept);
        EditText etGender = findViewById(R.id.etGender);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPhone = findViewById(R.id.etPhone);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnSubmit = findViewById(R.id.btnSubmitStudent);


        btnSubmit.setOnClickListener(v -> {
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("roll", Integer.parseInt(etRoll.getText().toString().trim()));
                jsonBody.put("name", etName.getText().toString().trim());
                jsonBody.put("series", Integer.parseInt(etSeries.getText().toString().trim()));
                jsonBody.put("dept", etDept.getText().toString().trim());
                jsonBody.put("gender", etGender.getText().toString().trim());
                jsonBody.put("email", etEmail.getText().toString().trim());
                jsonBody.put("phone", etPhone.getText().toString().trim());
                jsonBody.put("passwordHash", etPassword.getText().toString().trim());

                registerStudent(jsonBody);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter numbers for Roll and Series", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void registerStudent(JSONObject jsonBody) {
        String url = "http://10.0.2.2:8080/student/register";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {

                    Toast.makeText(this, "Registration Success!", Toast.LENGTH_SHORT).show();
                    try {
                        Intent intent = new Intent(StudentRegistrationActivity.this, StudentDashboardActivity.class);
                        intent.putExtra("STUDENT_ROLL", jsonBody.getInt("roll"));
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        finish();
                    }
                },
                error -> {

                    Toast.makeText(this, "Connection Error - Check Backend", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}