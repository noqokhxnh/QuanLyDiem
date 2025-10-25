package com.example.qld.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qld.R;
import com.example.qld.models.Student;

import java.util.List;

/**
 * Adapter cho RecyclerView để hiển thị danh sách học sinh
 * Mỗi mục trong danh sách sẽ hiển thị tên, mã học sinh, lớp và điểm trung bình
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private Context context;
    private List<Student> students;

    /**
     * Constructor cho StudentAdapter
     * @param context Context của ứng dụng
     * @param students Danh sách học sinh để hiển thị
     */
    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    /**
     * Tạo view holder mới cho danh sách
     * @param parent ViewGroup chứa các item
     * @param viewType Loại view của item
     * @return ViewHolder mới cho item
     */
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    /**
     * Gán dữ liệu cho view holder tại vị trí cụ thể
     * @param holder ViewHolder chứa các thành phần giao diện
     * @param position Vị trí của item trong danh sách
     */
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        
        holder.tvStudentName.setText("Học sinh: " + (student.getFullName() != null ? student.getFullName() : "Không xác định"));
        holder.tvStudentCode.setText("Mã HS: " + student.getStudentCode());
        holder.tvClassName.setText("Lớp: " + student.getClassName());
        
        holder.tvBirthDate.setText("Điểm TB: " + String.format("%.2f", student.getAverage()));
    }

    /**
     * Trả về số lượng item trong danh sách
     * @return Số lượng học sinh trong danh sách
     */
    @Override
    public int getItemCount() {
        return students.size();
    }

    /**
     * Lớp view holder chứa các thành phần giao diện cho mỗi item
     */
    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentCode, tvClassName, tvBirthDate;

        /**
         * Constructor cho StudentViewHolder
         * @param itemView View chứa các thành phần giao diện
         */
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvStudentCode = itemView.findViewById(R.id.tv_student_code);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            tvBirthDate = itemView.findViewById(R.id.tv_birth_date);
        }
    }
}