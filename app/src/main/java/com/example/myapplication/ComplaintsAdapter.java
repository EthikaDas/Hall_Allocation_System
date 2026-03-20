package com.example.myapplication;

import android.content.Context;
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
import com.example.myapplication.ComplaintResponse;
import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder> {
    private List<ComplaintResponse> complaints;
    private Context context;

    public ComplaintsAdapter(List<ComplaintResponse> complaints, Context context) {
        this.complaints = complaints;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComplaintResponse complaint = complaints.get(position);


        holder.tvCategory.setText(complaint.getCategory());
        holder.tvStatus.setText(complaint.getStatus());
        holder.tvStudent.setText(complaint.getStudentName() + " (" + complaint.getStudentRoll() + ")");
        holder.tvDetails.setText(complaint.getDetails());


        if ("Resolved".equalsIgnoreCase(complaint.getStatus())) {
            holder.btnResolve.setVisibility(View.GONE);
        } else {
            holder.btnResolve.setVisibility(View.VISIBLE);
            holder.btnResolve.setOnClickListener(v -> resolveComplaint(complaint.getId(), position));
        }
    }

    private void resolveComplaint(int id, int position) {

        String url = "http://10.0.2.2:8080/admin/complaints/resolve?id=" + id;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(context, "Complaint Resolved", Toast.LENGTH_SHORT).show();

                    complaints.get(position).getClass();
                    notifyItemChanged(position);
                },
                error -> Toast.makeText(context, "Failed to resolve", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(context).add(request);
    }

    @Override
    public int getItemCount() { return complaints.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvStatus, tvStudent, tvDetails;
        Button btnResolve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvItemCategory);
            tvStatus = itemView.findViewById(R.id.tvItemStatus);
            tvStudent = itemView.findViewById(R.id.tvItemStudent);
            tvDetails = itemView.findViewById(R.id.tvItemDetails);
            btnResolve = itemView.findViewById(R.id.btnResolve);
        }
    }
}