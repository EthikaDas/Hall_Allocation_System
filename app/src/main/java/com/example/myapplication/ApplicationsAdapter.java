package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.List;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {
    private List<ApplicationResponse> applications;
    private Context context;

    public ApplicationsAdapter(List<ApplicationResponse> applications, Context context) {
        this.applications = applications;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationResponse app = applications.get(position);

        holder.tvRoll.setText("Roll: " + app.getStudentRoll());
        holder.tvHall.setText("Hall: " + app.getHallName());

        String roommatesStr = app.getRoommates().toString().replace("[", "").replace("]", "");
        holder.tvRoommates.setText(roommatesStr.isEmpty() ? "No roommates requested" : roommatesStr);


        String path = app.getGradesheetPath();

        if (path == null || path.contains("default_gradesheet.pdf")) {
            holder.tvGradesheetStatus.setText("Gradesheet: Default (System)");
            holder.tvGradesheetStatus.setTextColor(Color.parseColor("#FFCC00"));
            holder.tvGradesheetStatus.setOnClickListener(null);
        } else {
            holder.tvGradesheetStatus.setText("Gradesheet: 📄 View PDF");
            holder.tvGradesheetStatus.setTextColor(Color.parseColor("#4CAF50"));

            holder.tvGradesheetStatus.setOnClickListener(v -> {
                String pdfUrl = "http://10.0.2.2:8080/admin/view-pdf/" + app.getId();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
                    context.startActivity(browserIntent);
                }
            });
        }

        holder.btnApprove.setOnClickListener(v -> updateStatus(app.getId(), "Approved", holder.getAdapterPosition()));
        holder.btnReject.setOnClickListener(v -> updateStatus(app.getId(), "Rejected", holder.getAdapterPosition()));
    }

    private void updateStatus(int appId, String status, int position) {
        String url = "http://10.0.2.2:8080/admin/update-application?appId=" + appId + "&status=" + status;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(context, "Application " + status, Toast.LENGTH_SHORT).show();
                    if (position < applications.size()) {
                        applications.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, applications.size());
                    }
                },
                error -> Toast.makeText(context, "Update Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public int getItemCount() { return applications.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoll, tvHall, tvRoommates, tvGradesheetStatus; // Added tvGradesheetStatus
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoll = itemView.findViewById(R.id.tvAppRoll);
            tvHall = itemView.findViewById(R.id.tvAppHall);
            tvRoommates = itemView.findViewById(R.id.tvAppRoommates);
            tvGradesheetStatus = itemView.findViewById(R.id.tvGradesheetStatus); // Link to XML
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}