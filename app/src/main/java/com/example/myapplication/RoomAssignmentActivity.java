package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;

public class RoomAssignmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_assignment);

        findViewById(R.id.btnBackAssign).setOnClickListener(v -> finish());

        Button btnRun = findViewById(R.id.btnRunAssignment);
        btnRun.setOnClickListener(v -> runAssignmentTask());
    }

    private void runAssignmentTask() {

        String url = "http://10.0.2.2:8080/admin/assign-rooms";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    try {

                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}