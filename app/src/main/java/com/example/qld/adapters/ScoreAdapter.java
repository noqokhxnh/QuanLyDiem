package com.example.qld.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qld.R;
import com.example.qld.models.Score;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private Context context;
    private List<Score> scores;

    public ScoreAdapter(Context context, List<Score> scores) {
        this.context = context;
        this.scores = scores;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scores.get(position);
        
        // Get score type in Vietnamese
        String scoreType = getScoreTypeInVietnamese(score.getScoreType());
        holder.tvScoreType.setText(scoreType);
        
        holder.tvScoreValue.setText(String.valueOf(score.getScore()));
        holder.tvDate.setText(score.getDateCreated() != null ? score.getDateCreated() : "Không có");
    }

    private String getScoreTypeInVietnamese(String scoreType) {
        switch (scoreType) {
            case "mieng":
                return "Điểm miệng";
            case "15phut":
                return "Kiểm tra 15 phút";
            case "1tiet":
                return "Kiểm tra 1 tiết";
            case "hocky":
                return "Thi học kỳ";
            default:
                return scoreType;
        }
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvScoreType, tvScoreValue, tvDate;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvScoreType = itemView.findViewById(R.id.tv_score_type);
            tvScoreValue = itemView.findViewById(R.id.tv_score_value);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}