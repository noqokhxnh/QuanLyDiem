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

/**
 * Adapter cho RecyclerView để hiển thị danh sách điểm số
 * Mỗi mục trong danh sách sẽ hiển thị loại điểm, giá trị điểm và ngày tạo
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private Context context;
    private List<Score> scores;

    /**
     * Constructor cho ScoreAdapter
     * @param context Context của ứng dụng
     * @param scores Danh sách điểm số để hiển thị
     */
    public ScoreAdapter(Context context, List<Score> scores) {
        this.context = context;
        this.scores = scores;
    }

    /**
     * Tạo view holder mới cho danh sách
     * @param parent ViewGroup chứa các item
     * @param viewType Loại view của item
     * @return ViewHolder mới cho item
     */
    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    /**
     * Gán dữ liệu cho view holder tại vị trí cụ thể
     * @param holder ViewHolder chứa các thành phần giao diện
     * @param position Vị trí của item trong danh sách
     */
    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scores.get(position);
        
        // Get score type in Vietnamese
        String scoreType = getScoreTypeInVietnamese(score.getScoreType());
        holder.tvScoreType.setText(scoreType);
        
        holder.tvScoreValue.setText(String.valueOf(score.getScore()));
        holder.tvDate.setText(score.getDateCreated() != null ? score.getDateCreated() : "Không có");
    }

    /**
     * Chuyển đổi loại điểm sang tiếng Việt
     * @param scoreType Loại điểm gốc (mieng, 15phut, 1tiet, hocky)
     * @return Tên loại điểm bằng tiếng Việt
     */
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

    /**
     * Trả về số lượng item trong danh sách
     * @return Số lượng điểm số trong danh sách
     */
    @Override
    public int getItemCount() {
        return scores.size();
    }

    /**
     * Lớp view holder chứa các thành phần giao diện cho mỗi item
     */
    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvScoreType, tvScoreValue, tvDate;

        /**
         * Constructor cho ScoreViewHolder
         * @param itemView View chứa các thành phần giao diện
         */
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvScoreType = itemView.findViewById(R.id.tv_score_type);
            tvScoreValue = itemView.findViewById(R.id.tv_score_value);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}