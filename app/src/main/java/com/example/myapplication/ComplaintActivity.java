package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ComplaintActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText etComplaintDetails;
    private int studentRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);


        studentRoll = getIntent().getIntExtra("STUDENT_ROLL", 0);


        spinnerCategory = findViewById(R.id.spinnerCategory);
        etComplaintDetails = findViewById(R.id.etComplaintDetails);
        Button btnSubmit = findViewById(R.id.btnSubmitComplaint);


        btnSubmit.setOnClickListener(v -> validateAndSubmit());

        findViewById(R.id.btnBackCard).setOnClickListener(v -> finish());
    }

    private void validateAndSubmit() {
        String details = etComplaintDetails.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (details.isEmpty()) {
            Toast.makeText(this, "Please provide complaint details", Toast.LENGTH_SHORT).show();
            return;
        }

        submitToBackend(category, details);
    }

    private void submitToBackend(String category, String details) {

        String url = "http://10.0.2.2:8080/student/complaint";


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("roll", studentRoll);
            requestBody.put("category", category);
            requestBody.put("details", details);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {

                    Toast.makeText(ComplaintActivity.this, "Complaint Submitted!", Toast.LENGTH_LONG).show();
                    finish();
                },
                error -> {

                    if (error.networkResponse != null &&
                            (error.networkResponse.statusCode == 200 || error.networkResponse.statusCode == 201)) {

                        Toast.makeText(ComplaintActivity.this, "Complaint Submitted Successfully!", Toast.LENGTH_LONG).show();
                        finish();

                    } else {

                        String message = "Submission failed";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {

                                message = new String(error.networkResponse.data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(ComplaintActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}