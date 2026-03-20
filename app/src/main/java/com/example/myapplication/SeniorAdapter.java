package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeniorAdapter extends RecyclerView.Adapter<SeniorAdapter.SeniorViewHolder> {

    private List<FindSeniorResponse> seniorList;

    public SeniorAdapter(List<FindSeniorResponse> seniorList) {
        this.seniorList = seniorList;
    }

    @NonNull
    @Override
    public SeniorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new SeniorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeniorViewHolder holder, int position) {
        FindSeniorResponse senior = seniorList.get(position);
        String room = senior.getRoomNumber();
        if (room == null || room.isEmpty()) {
            room = "Assignment Pending";
        }

        String displayText = "🏠 Room: " + senior.getRoomNumber() + "\n" +
                "👤 " + senior.getName() + " (" + senior.getSeries() + ")\n" +
                "🎓 Dept: " + senior.getDept() + "\n" +
                "📞 " + senior.getPhone();

        holder.tvSeniorDetails.setText(displayText);
    }

    @Override
    public int getItemCount() {
        return seniorList.size();
    }

    public static class SeniorViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeniorDetails;

        public SeniorViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSeniorDetails = itemView.findViewById(R.id.tvNoticeText);
        }
    }
}