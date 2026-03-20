package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentDashboardActivity extends AppCompatActivity {
    private TextView tvName, tvRoll, tvSeries, tvDept, tvGender, tvPhone, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        tvName = findViewById(R.id.tvStudentName);
        tvRoll = findViewById(R.id.tvRoll);
        tvSeries = findViewById(R.id.tvSeries);
        tvDept = findViewById(R.id.tvDept);
        tvGender = findViewById(R.id.tvGender);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);

        LinearLayout btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> finish());

        int studentRoll = getIntent().getIntExtra("STUDENT_ROLL", -1);
        if (studentRoll != -1) {
            fetchStudentProfile(studentRoll);
            setupMenuButtons(studentRoll);

            checkApplicationStatusAndDisableButton(studentRoll);
        } else {
            Toast.makeText(this, "Session Error: Roll not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMenuButtons(int currentRoll) {

        findViewById(R.id.btnNotifications).setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, NotificationsActivity.class);
            intent.putExtra("STUDENT_ROLL", currentRoll);
            startActivity(intent);
        });


        findViewById(R.id.btnFindPerson).setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, FindSeniorActivity.class);
            intent.putExtra("STUDENT_ROLL", currentRoll);
            startActivity(intent);
        });


        findViewById(R.id.btnComplaint).setOnClickListener(v -> {
            Intent intent = new Intent(this, ComplaintActivity.class);
            intent.putExtra("STUDENT_ROLL", currentRoll);
            startActivity(intent);
        });
    }


    private void checkApplicationStatusAndDisableButton(int roll) {
        String url = "http://10.0.2.2:8080/students/application-status";
        LinearLayout btnHallApp = findViewById(R.id.btnHallApp);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("roll", roll);
        } catch (JSONException e) { e.printStackTrace(); }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {

                    btnHallApp.setAlpha(0.5f);
                    btnHallApp.setOnClickListener(v ->
                            Toast.makeText(this, "You have already submitted an application!", Toast.LENGTH_SHORT).show()
                    );
                },
                error -> {

                    btnHallApp.setAlpha(1.0f);
                    btnHallApp.setOnClickListener(v -> {
                        Intent intent = new Intent(StudentDashboardActivity.this, HallApplicationActivity.class);
                        intent.putExtra("STUDENT_ROLL", roll);
                        startActivity(intent);
                    });
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchStudentProfile(int roll) {
        String url = "http://10.0.2.2:8080/student/profile/" + roll;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        tvName.setText(response.getString("name").toUpperCase());
                        tvRoll.setText(String.valueOf(response.getInt("roll")));
                        tvSeries.setText(String.valueOf(response.getInt("series")));
                        tvDept.setText(response.getString("dept"));
                        tvGender.setText(response.getString("gender"));
                        tvPhone.setText(response.getString("phone"));
                        tvEmail.setText(response.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Data Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch profile", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }
}