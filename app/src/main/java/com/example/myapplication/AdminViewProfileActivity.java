package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;

public class AdminViewProfileActivity extends AppCompatActivity {

    private TextView tvName,  tvEmail, tvRole;
    private String adminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_profile);

        adminEmail = getIntent().getStringExtra("ADMIN_EMAIL");


        tvName = findViewById(R.id.tvAdminName);
        tvEmail = findViewById(R.id.tvAdminEmail);
        tvRole = findViewById(R.id.tvAdminRole);

        findViewById(R.id.btnBackCard).setOnClickListener(v -> finish());

        if (adminEmail != null) {
            fetchAdminProfile();
        } else {
            Toast.makeText(this, "Admin email not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAdminProfile() {

        String url = "http://10.0.2.2:8080/admin/profile/" + adminEmail;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        tvName.setText(response.getString("name"));
                        tvEmail.setText(response.getString("email"));
                        tvRole.setText(response.getString("role"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing profile data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}