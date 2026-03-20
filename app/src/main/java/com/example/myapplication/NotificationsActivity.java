package com.example.myapplication;

import android.graphics.Color; // Added for styling status colors
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> noticeList = new ArrayList<>();
    private int studentRoll;
    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        studentRoll = getIntent().getIntExtra("STUDENT_ROLL", 0);

        recyclerView = findViewById(R.id.rvNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NoticeAdapter(noticeList);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnBackCard).setOnClickListener(v -> finish());


        if (studentRoll != 0) {
            fetchApplicationStatus();
        } else {
            noticeList.add("📋 STATUS: Log in again to view status");
            fetchGeneralNotices();
        }
    }

    private void fetchApplicationStatus() {
        String url = "http://10.0.2.2:8080/students/application-status";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("roll", studentRoll);
        } catch (JSONException e) { e.printStackTrace(); }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    try {
                        String status = response.optString("status", "Pending");
                        String room = response.optString("roomNumber", "");
                        String hall = response.optString("hallName", "");
                        String msg = response.optString("message", "");

                        String personalStatus;
                        if ("Completed".equalsIgnoreCase(status) && !room.isEmpty()) {
                            personalStatus = "🏠 ASSIGNED: Room " + room + " in " + hall;
                        } else if ("Approved".equalsIgnoreCase(status)) {
                            personalStatus = "✅ APPROVED: " + msg;
                        } else if ("Rejected".equalsIgnoreCase(status)) {
                            personalStatus = "❌ REJECTED: " + msg;
                        } else {
                            personalStatus = "⏳ STATUS: " + status + " - " + msg;
                        }

                        noticeList.clear();
                        noticeList.add(personalStatus);
                        fetchGeneralNotices();
                    } catch (Exception e) {
                        fetchGeneralNotices();
                    }
                },
                error -> {

                    noticeList.clear();
                    noticeList.add("📋 STATUS: No Application Found");
                    fetchGeneralNotices();
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchGeneralNotices() {
        String url = "http://10.0.2.2:8080/student/notices";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject notice = response.getJSONObject(i);
                            noticeList.add("📢 NOTICE: " + notice.getString("message"));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) { e.printStackTrace(); }
                },
                error -> {
                    Toast.makeText(this, "Server Error: Notices unavailable", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
        );
        Volley.newRequestQueue(this).add(request);
    }
}