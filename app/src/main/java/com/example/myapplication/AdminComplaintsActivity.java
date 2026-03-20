package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ComplaintResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AdminComplaintsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ComplaintsAdapter adapter;
    private List<ComplaintResponse> complaintList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_complaints);


        recyclerView = findViewById(R.id.rvComplaints);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackCard).setOnClickListener(v -> finish());

        loadComplaints();
    }

    private void loadComplaints() {

        String url = "http://10.0.2.2:8080/admin/complaints";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    complaintList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);


                            complaintList.add(new ComplaintResponse(
                                    obj.getInt("id"),
                                    obj.getInt("studentRoll"),
                                    obj.getString("studentName"),
                                    obj.getString("category"),
                                    obj.getString("details"),
                                    obj.getString("status")
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter = new ComplaintsAdapter(complaintList, this);
                    recyclerView.setAdapter(adapter);
                },
                error -> {
                    Toast.makeText(this, "Server error: Check if backend is running", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );
        Volley.newRequestQueue(this).add(request);
    }
}