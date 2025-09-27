package com.example.studentscoremanager;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminStudentAdapter extends RecyclerView.Adapter<AdminStudentAdapter.StudentVH> {

    private Cursor cursor;

    public AdminStudentAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void swapCursor(Cursor newCursor) {
        if (this.cursor != null) this.cursor.close();
        this.cursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_admin, parent, false);
        return new StudentVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentVH holder, int position) {
        if (cursor == null || cursor.isClosed()) return;
        if (!cursor.moveToPosition(position)) return;

        String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STUDENT_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME));
        String className = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CLASS));

        holder.tvStudentName.setText(name);
        holder.tvStudentId.setText("MSSV: " + id);
        holder.tvStudentClass.setText("Lớp: " + className);
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    static class StudentVH extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentId, tvStudentClass;

        StudentVH(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvStudentClass = itemView.findViewById(R.id.tvStudentClass);
        }
    }
}


