package com.example.studentscoremanager;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminAllScoresAdapter extends RecyclerView.Adapter<AdminAllScoresAdapter.VH> {

    private Cursor cursor;

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (cursor == null || cursor.isClosed()) return;
        if (!cursor.moveToPosition(position)) return;

        String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME));
        String studentId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STUDENT_ID));
        String subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBJECT));
        double mid = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MIDTERM_SCORE));
        double fin = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FINAL_SCORE));
        double avg = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AVERAGE_SCORE));
        String semester = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SEMESTER));
        String year = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_YEAR));

        holder.tvSubjectName.setText(subject + " • " + semester + " - " + year);
        holder.tvMidtermScore.setText(String.format("%.1f", mid));
        holder.tvFinalScore.setText(String.format("%.1f", fin));
        holder.tvAverageScore.setText(String.format("%.1f", avg));
        holder.tvStudentInfo.setText(fullName + " - " + studentId);
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public void updateCursor(Cursor newCursor) {
        if (this.cursor != null) this.cursor.close();
        this.cursor = newCursor;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvMidtermScore, tvFinalScore, tvAverageScore, tvStudentInfo;
        VH(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvMidtermScore = itemView.findViewById(R.id.tvMidtermScore);
            tvFinalScore = itemView.findViewById(R.id.tvFinalScore);
            tvAverageScore = itemView.findViewById(R.id.tvAverageScore);
            tvStudentInfo = itemView.findViewById(R.id.tvSemester); // reuse field for extra info line
        }
    }
}


