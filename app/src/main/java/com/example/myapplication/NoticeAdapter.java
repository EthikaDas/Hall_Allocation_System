package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<String> notices;

    public NoticeAdapter(List<String> notices) {
        this.notices = notices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String notice = notices.get(position);
        holder.tvNotice.setText(notice);


        if (notice.contains("MY STATUS") || notice.contains("ASSIGNED")) {
            holder.tvNotice.setTextColor(0xFFFFEB3B);
        } else {
            holder.tvNotice.setTextColor(0xFFFFFFFF);
        }
    }

    @Override
    public int getItemCount() { return notices.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotice = itemView.findViewById(R.id.tvNoticeText);
        }
    }

}