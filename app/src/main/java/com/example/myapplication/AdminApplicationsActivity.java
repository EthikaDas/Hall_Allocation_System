package com.example.myapplication;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AdminApplicationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApplicationsAdapter adapter;
    private List<ApplicationResponse> applicationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_applications);

        recyclerView = findViewById(R.id.rvApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ApplicationsAdapter(applicationList, this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnBackApps).setOnClickListener(v -> finish());

        loadApplications();
    }

    private void loadApplications() {
        String url = "http://10.0.2.2:8080/admin/pending-applications";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    applicationList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);


                            JSONArray roommatesArray = obj.optJSONArray("roommates");
                            List<Integer> roommates = new ArrayList<>();
                            if (roommatesArray != null) {
                                for(int j=0; j < roommatesArray.length(); j++) {
                                    roommates.add(roommatesArray.getInt(j));
                                }
                            }


                            applicationList.add(new ApplicationResponse(
                                    obj.getInt("id"),
                                    obj.getInt("studentRoll"),
                                    obj.getString("hallName"),
                                    roommates,
                                    obj.getString("status"),
                                    obj.optString("gradesheetPath", "default_gradesheet.pdf")
                            ));
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }
}