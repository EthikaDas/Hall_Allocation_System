package com.example.myapplication;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class FindSeniorActivity extends AppCompatActivity {

    private EditText etSearchQuery;
    private RecyclerView rvSeniors;
    private int requesterRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_senior);

        requesterRoll = getIntent().getIntExtra("STUDENT_ROLL", 0);
        etSearchQuery = findViewById(R.id.etSearchQuery);
        rvSeniors = findViewById(R.id.rvSeniors);
        rvSeniors.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnSearch).setOnClickListener(v -> performSearch());
        findViewById(R.id.btnBackCard).setOnClickListener(v -> finish());
    }

    private void performSearch() {
        String query = etSearchQuery.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a name, dept, or roll", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:8080/students/find-senior";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("query", query);
            requestBody.put("requesterRoll", requesterRoll);
        } catch (JSONException e) { e.printStackTrace(); }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null,
                response -> {
                    List<FindSeniorResponse> seniorList = new ArrayList<>();

                    if (response.length() == 0) {
                        Toast.makeText(this, "No hall residents found", Toast.LENGTH_SHORT).show();
                        rvSeniors.setAdapter(new SeniorAdapter(seniorList));
                    } else {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                FindSeniorResponse senior = new FindSeniorResponse(
                                        obj.getInt("roll"),
                                        obj.getString("name"),
                                        obj.getString("dept"),
                                        obj.getInt("series"),
                                        obj.getString("phone"),
                                        obj.getString("email"),
                                        obj.optString("photo", ""),
                                        obj.getString("roomNumber")
                                );
                                seniorList.add(senior);
                            }

                            SeniorAdapter adapter = new SeniorAdapter(seniorList);
                            rvSeniors.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> Toast.makeText(this, "Server not responding", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public byte[] getBody() {
                return requestBody.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}