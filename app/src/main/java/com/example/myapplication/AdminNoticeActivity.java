package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AdminNoticeActivity extends AppCompatActivity {

    private EditText etMessage;
    private TextView tvSelectedFile;
    private Uri selectedFileUri = null;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedFileUri = result.getData().getData();
                    tvSelectedFile.setText("File Attached Successfully");
                    tvSelectedFile.setVisibility(View.VISIBLE);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice);

        etMessage = findViewById(R.id.etNoticeMessage);
        tvSelectedFile = findViewById(R.id.tvSelectedFile);

        findViewById(R.id.btnBackNotice).setOnClickListener(v -> finish());

        findViewById(R.id.btnAttachPdf).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            filePickerLauncher.launch(intent);
        });

        findViewById(R.id.btnSendNotice).setOnClickListener(v -> uploadNotice());
    }

    private void uploadNotice() {
        String message = etMessage.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://" +
                ":8080/admin/send-notice";

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> Toast.makeText(AdminNoticeActivity.this, "Notice Sent!", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(AdminNoticeActivity.this, "Upload failed: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("message", message);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (selectedFileUri != null) {
                    params.put("file", new DataPart("notice.pdf", getBytes(selectedFileUri)));
                }
                return params;
            }
        };

        Volley.newRequestQueue(this).add(multipartRequest);
    }


    private byte[] getBytes(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}