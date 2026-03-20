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

public class AdminDashboardActivity extends AppCompatActivity {


    private TextView tvAdminName;
    private String currentAdminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        currentAdminEmail = getIntent().getStringExtra("ADMIN_EMAIL");
        setupAdminMenuButtons();

        currentAdminEmail = getIntent().getStringExtra("ADMIN_EMAIL");

        if (currentAdminEmail == null) {
            currentAdminEmail = "default@gmail.com";
        }

        fetchAdminProfile(currentAdminEmail);

    }

    private void setupAdminMenuButtons() {
        findViewById(R.id.btnAdminExit).setOnClickListener(v -> {

            finishAffinity();

            System.exit(0);
        });

        findViewById(R.id.btnViewProfile).setOnClickListener(v -> {

            Intent intent = new Intent(AdminDashboardActivity.this, AdminViewProfileActivity.class);

            intent.putExtra("ADMIN_EMAIL", currentAdminEmail);


            startActivity(intent);
        });


        findViewById(R.id.btnUpdateProfiles).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminUpdateProfileActivity.class);

            intent.putExtra("ADMIN_EMAIL", currentAdminEmail);

            startActivity(intent);
        });

        findViewById(R.id.btnSeeComplaints).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminComplaintsActivity.class);

            startActivity(intent);
        });

        findViewById(R.id.btnSeeApplications).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminApplicationsActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.btnRoomAssignment).setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, RoomAssignmentActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnSendNotice).setOnClickListener(v -> {

            Intent intent = new Intent(AdminDashboardActivity.this, AdminNoticeActivity.class);
            startActivity(intent);
        });

    }

    private void fetchAdminProfile(String email) {
        String url = "http://10.0.2.2:8080/admin/profile/" + email;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        String name = response.getString("name");


                        String role = response.getString("role");

                        if (!role.contains("ADMIN")) {
                            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to load admin data", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}