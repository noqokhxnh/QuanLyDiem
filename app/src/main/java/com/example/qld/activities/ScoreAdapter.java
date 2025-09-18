package com.example.qld.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qld.R;
import com.example.qld.database.DatabaseManager;
import com.example.qld.models.Score;
import com.example.qld.models.Student;
import com.example.qld.models.Subject;
import com.example.qld.models.User;

import java.util.List;

/**
 * Adapter để hiển thị danh sách điểm trong RecyclerView
 * Hỗ trợ click listener để chỉnh sửa điểm
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {
    private Context context;
    private List<Score> scoreList;
    private DatabaseManager dbManager;
    
    /**
     * Constructor cho ScoreAdapter
     * @param context context của activity
     * @param scoreList danh sách điểm cần hiển thị
     * @param dbManager database manager để truy xuất thông tin
     */
    public ScoreAdapter(Context context, List<Score> scoreList, DatabaseManager dbManager) {
        this.context = context;
        this.scoreList = scoreList;
        this.dbManager = dbManager;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view từ layout item_score
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy thông tin điểm tại vị trí position
        Score score = scoreList.get(position);
        
        // Hiển thị thông tin điểm
        try {
            dbManager.open();
            Student student = dbManager.getStudentById(score.getStudentId());
            Subject subject = dbManager.getSubjectById(score.getSubjectId());
            User user = dbManager.getUserById(score.getTeacherId());
            
            if (student != null) {
                User studentUser = dbManager.getUserById(student.getUserId());
                if (studentUser != null) {
                    holder.tvStudentName.setText(studentUser.getFullName());
                }
            }
            
            if (subject != null) {
                holder.tvSubjectName.setText(subject.getSubjectName());
            }
            
            // Hiển thị loại điểm bằng tiếng Việt
            String scoreTypeDisplay = "";
            switch (score.getScoreType()) {
                case "mieng":
                    scoreTypeDisplay = "Điểm miệng";
                    break;
                case "15phut":
                    scoreTypeDisplay = "Điểm 15 phút";
                    break;
                case "1tiet":
                    scoreTypeDisplay = "Điểm 1 tiết";
                    break;
                case "hocky":
                    scoreTypeDisplay = "Điểm học kỳ";
                    break;
                default:
                    scoreTypeDisplay = score.getScoreType();
            }
            
            holder.tvScoreType.setText("Loại: " + scoreTypeDisplay);
            holder.tvScoreValue.setText("Điểm: " + String.valueOf(score.getScore()));
            
            if (user != null) {
                holder.tvTeacherName.setText("GV: " + user.getFullName());
            }
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi tải thông tin điểm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
        
        // Thiết lập sự kiện click cho nút chỉnh sửa
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Triển khai chức năng chỉnh sửa
                Toast.makeText(context, "Chức năng chỉnh sửa sẽ được triển khai", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Thiết lập sự kiện click cho nút xóa
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(score);
            }
        });
    }
    
    /**
     * Hiển thị dialog xác nhận xóa điểm
     * @param score điểm cần xóa
     */
    private void showDeleteConfirmationDialog(Score score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa điểm này? Hành động này không thể hoàn tác.");
        
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteScore(score);
            }
        });
        
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    /**
     * Xóa điểm khỏi database
     * @param score điểm cần xóa
     */
    private void deleteScore(Score score) {
        try {
            dbManager.open();
            
            // Xóa bản ghi điểm
            int result = dbManager.deleteScore(score.getId());
            
            if (result > 0) {
                // Xóa khỏi danh sách và thông báo cho adapter
                scoreList.remove(score);
                notifyDataSetChanged();
                
                Toast.makeText(context, "Xóa điểm thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Lỗi khi xóa điểm", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi xóa điểm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
    
    @Override
    public int getItemCount() {
        return scoreList.size();
    }
    
    /**
     * ViewHolder để giữ các view trong item_score
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        TextView tvSubjectName;
        TextView tvScoreType;
        TextView tvScoreValue;
        TextView tvTeacherName;
        Button btnEdit;
        Button btnDelete;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvSubjectName = itemView.findViewById(R.id.tv_subject_name);
            tvScoreType = itemView.findViewById(R.id.tv_score_type);
            tvScoreValue = itemView.findViewById(R.id.tv_score_value);
            tvTeacherName = itemView.findViewById(R.id.tv_teacher_name);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}