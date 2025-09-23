package com.example.studentscoremanager;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private Cursor cursor;
    private Context context;

    public ScoreAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            String subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBJECT));
            double midtermScore = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MIDTERM_SCORE));
            double finalScore = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FINAL_SCORE));
            double averageScore = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AVERAGE_SCORE));
            String semester = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SEMESTER));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_YEAR));

            holder.tvSubjectName.setText(subject);
            holder.tvMidtermScore.setText(String.format("%.1f", midtermScore));
            holder.tvFinalScore.setText(String.format("%.1f", finalScore));
            holder.tvAverageScore.setText(String.format("%.1f", averageScore));
            holder.tvSemester.setText(semester + " - " + year);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void updateCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvMidtermScore, tvFinalScore, tvAverageScore, tvSemester;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvMidtermScore = itemView.findViewById(R.id.tvMidtermScore);
            tvFinalScore = itemView.findViewById(R.id.tvFinalScore);
            tvAverageScore = itemView.findViewById(R.id.tvAverageScore);
            tvSemester = itemView.findViewById(R.id.tvSemester);
        }
    }
}
