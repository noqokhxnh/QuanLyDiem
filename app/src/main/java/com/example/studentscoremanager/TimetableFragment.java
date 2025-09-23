package com.example.studentscoremanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TimetableFragment extends Fragment {

    private RecyclerView recyclerViewTimetables;
    private TimetableAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<TimetableItem> timetableList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        
        dbHelper = new DatabaseHelper(getContext());
        timetableList = new ArrayList<>();
        
        recyclerViewTimetables = view.findViewById(R.id.recyclerViewTimetables);
        
        recyclerViewTimetables.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TimetableAdapter(timetableList);
        recyclerViewTimetables.setAdapter(adapter);
        
        loadTimetables();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tự động tải lại thời khóa biểu khi màn hình hiển thị lại
        loadTimetables();
    }

    private void loadTimetables() {
        timetableList.clear();
        Cursor cursor = dbHelper.getAllTimetables();
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMETABLE_ID));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMETABLE_SUBJECT));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMETABLE_DAY));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMETABLE_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMETABLE_END_TIME));
                String room = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMETABLE_ROOM));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMETABLE_TEACHER));
                String className = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIMETABLE_CLASS));
                
                timetableList.add(new TimetableItem(id, subject, day, startTime, endTime, room, teacher, className));
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        adapter.notifyDataSetChanged();
    }

    public static class TimetableItem {
        private int id;
        private String subject, day, startTime, endTime, room, teacher, className;

        public TimetableItem(int id, String subject, String day, String startTime, String endTime, String room, String teacher, String className) {
            this.id = id;
            this.subject = subject;
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
            this.room = room;
            this.teacher = teacher;
            this.className = className;
        }

        // Getters
        public int getId() { return id; }
        public String getSubject() { return subject; }
        public String getDay() { return day; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getRoom() { return room; }
        public String getTeacher() { return teacher; }
        public String getClassName() { return className; }
    }

    private class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

        private List<TimetableItem> timetables;

        public TimetableAdapter(List<TimetableItem> timetables) {
            this.timetables = timetables;
        }

        @Override
        public TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_timetable, parent, false);
            return new TimetableViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TimetableViewHolder holder, int position) {
            TimetableItem timetable = timetables.get(position);
            holder.bind(timetable);
        }

        @Override
        public int getItemCount() {
            return timetables.size();
        }

        class TimetableViewHolder extends RecyclerView.ViewHolder {
            private TextView tvSubject, tvDay, tvTime, tvRoom, tvTeacher, tvClassName;

            TimetableViewHolder(View itemView) {
                super(itemView);
                tvSubject = itemView.findViewById(R.id.tvSubject);
                tvDay = itemView.findViewById(R.id.tvDay);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvRoom = itemView.findViewById(R.id.tvRoom);
                tvTeacher = itemView.findViewById(R.id.tvTeacher);
                tvClassName = itemView.findViewById(R.id.tvClassName);
            }

            void bind(TimetableItem timetable) {
                tvSubject.setText(timetable.getSubject());
                tvDay.setText(timetable.getDay());
                tvTime.setText(timetable.getStartTime() + " - " + timetable.getEndTime());
                tvRoom.setText(timetable.getRoom());
                tvTeacher.setText(timetable.getTeacher());
                tvClassName.setText(timetable.getClassName());
            }
        }
    }
}
