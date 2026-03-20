package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HallApplicationActivity extends AppCompatActivity {

    private EditText etHallName, etRoommate1, etRoommate2, etRoommate3;
    private TextView tvFileName;
    private Uri selectedFileUri;
    private int studentRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_application);

        studentRoll = getIntent().getIntExtra("STUDENT_ROLL", 0);
        etHallName = findViewById(R.id.etHallName);
        etRoommate1 = findViewById(R.id.etRoommate1);
        etRoommate2 = findViewById(R.id.etRoommate2);
        etRoommate3 = findViewById(R.id.etRoommate3);
        tvFileName = findViewById(R.id.tvFileName);
        tvFileName.setText(R.string.status_using_default_gradesheet);
        Button btnSubmit = findViewById(R.id.btnSubmitApp);
        Button btnCancel = findViewById(R.id.btnCancel);


        ActivityResultLauncher<Intent> filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedFileUri = result.getData().getData();
                        tvFileName.setText("Selected: " + selectedFileUri.getLastPathSegment());
                    }
                });

        findViewById(R.id.btnSelectFile).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            filePicker.launch(intent);
        });


        btnCancel.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> submitApplication());
    }

    private void submitApplication() {
        if (etHallName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a Hall Name", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://10.0.2.2:8080/student/apply-hall";

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Successfully Submitted!", Toast.LENGTH_LONG).show();
                    getSharedPreferences("UserPrefs", MODE_PRIVATE)
                            .edit().putBoolean("applied_" + studentRoll, true).apply();
                    finish();
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("studentRoll", String.valueOf(studentRoll));
                params.put("hallName", etHallName.getText().toString().trim());
                params.put("roommate1", etRoommate1.getText().toString().trim());
                params.put("roommate2", etRoommate2.getText().toString().trim());
                params.put("roommate3", etRoommate3.getText().toString().trim());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                byte[] fileData;
                String fileName;

                if (selectedFileUri != null) {
                    fileData = getFileBytes(selectedFileUri);
                    if (fileData == null) {
                        fileData = getString(R.string.Default_gradesheet).getBytes();
                        fileName = "default_placeholder.txt";
                    } else {
                        fileName = getString(R.string.User_gradesheet_pdf);
                    }
                } else {
                    fileData = getString(R.string.default_gradesheet).getBytes();
                    fileName = "default_placeholder.txt";
                }

                params.put(getString(R.string.gradesheet), new DataPart(fileName, fileData));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
    private byte[] getFileBytes(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) byteBuffer.write(buffer, 0, len);
            return byteBuffer.toByteArray();
        } catch (Exception e) { return null; }
    }
}