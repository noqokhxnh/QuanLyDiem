package com.example.qld.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.qld.models.Student;
import com.example.qld.models.User;

import java.util.List;

/**
 * Adapter để hiển thị danh sách học sinh trong RecyclerView
 * Hỗ trợ click listener để chỉnh sửa thông tin học sinh
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private Context context;
    private List<Student> studentList;
    private DatabaseManager dbManager;
    
    /**
     * Constructor cho StudentAdapter
     * @param context context của activity
     * @param studentList danh sách học sinh cần hiển thị
     * @param dbManager database manager để truy xuất thông tin
     */
    public StudentAdapter(Context context, List<Student> studentList, DatabaseManager dbManager) {
        this.context = context;
        this.studentList = studentList;
        this.dbManager = dbManager;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view từ layout item_student
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy thông tin học sinh tại vị trí position
        Student student = studentList.get(position);
        
        // Hiển thị thông tin học sinh
        try {
            dbManager.open();
            User user = dbManager.getUserById(student.getUserId());
            
            if (user != null) {
                holder.tvStudentName.setText(user.getFullName());
                holder.tvStudentCode.setText("Mã số: " + student.getStudentCode());
                holder.tvClassName.setText("Lớp: " + student.getClassName());
            }
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi tải thông tin học sinh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
        
        // Set click listener cho nút chỉnh sửa
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement edit functionality
                Toast.makeText(context, "Chức năng chỉnh sửa sẽ được triển khai", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Set click listener cho nút xóa
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(student);
            }
        });
    }
    
    /**
     * Hiển thị dialog xác nhận xóa học sinh
     * @param student học sinh cần xóa
     */
    private void showDeleteConfirmationDialog(Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa học sinh này? Hành động này không thể hoàn tác.");
        
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteStudent(student);
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
     * Xóa học sinh khỏi database
     * @param student học sinh cần xóa
     */
    private void deleteStudent(Student student) {
        try {
            dbManager.open();
            
            // Delete student record
            int result = dbManager.deleteStudent(student.getId());
            
            if (result > 0) {
                // Also delete the user account
                dbManager.deleteUser(student.getUserId());
                
                // Remove from list and notify adapter
                studentList.remove(student);
                notifyDataSetChanged();
                
                Toast.makeText(context, "Xóa học sinh thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Lỗi khi xóa học sinh", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi xóa học sinh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            dbManager.close();
        }
    }
    
    @Override
    public int getItemCount() {
        return studentList.size();
    }
    
    /**
     * ViewHolder để giữ các view trong item_student
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        TextView tvStudentCode;
        TextView tvClassName;
        Button btnEdit;
        Button btnDelete;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvStudentCode = itemView.findViewById(R.id.tv_student_code);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}