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

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private Context context;
    private List<Student> students;

    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        
        holder.tvStudentName.setText("Học sinh: " + student.getId());  // In a real app, you'd have the full name
        holder.tvStudentCode.setText("Mã HS: " + student.getStudentCode());
        holder.tvClassName.setText("Lớp: " + student.getClassName());
        
        if (student.getBirthDate() != null) {
            holder.tvBirthDate.setText("Ngày sinh: " + student.getBirthDate());
        } else {
            holder.tvBirthDate.setText("Ngày sinh: Không có");
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentCode, tvClassName, tvBirthDate;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvStudentCode = itemView.findViewById(R.id.tv_student_code);
            tvClassName = itemView.findViewById(R.id.tv_class_name);
            tvBirthDate = itemView.findViewById(R.id.tv_birth_date);
        }
    }
}