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

public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerViewSchedules;
    private ScheduleAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<ScheduleItem> scheduleList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        
        dbHelper = new DatabaseHelper(getContext());
        scheduleList = new ArrayList<>();
        
        recyclerViewSchedules = view.findViewById(R.id.recyclerViewSchedules);
        
        recyclerViewSchedules.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScheduleAdapter(scheduleList);
        recyclerViewSchedules.setAdapter(adapter);
        
        loadSchedules();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tự động tải lại lịch học khi màn hình hiển thị lại
        loadSchedules();
    }

    private void loadSchedules() {
        scheduleList.clear();
        Cursor cursor = dbHelper.getAllSchedules();
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SCHEDULE_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SCHEDULE_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SCHEDULE_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SCHEDULE_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SCHEDULE_TIME));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SCHEDULE_LOCATION));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SCHEDULE_TYPE));
                
                scheduleList.add(new ScheduleItem(id, title, description, date, time, location, type));
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        adapter.notifyDataSetChanged();
    }

    public static class ScheduleItem {
        private int id;
        private String title, description, date, time, location, type;

        public ScheduleItem(int id, String title, String description, String date, String time, String location, String type) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.date = date;
            this.time = time;
            this.location = location;
            this.type = type;
        }

        // Getters
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getLocation() { return location; }
        public String getType() { return type; }
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

        private List<ScheduleItem> schedules;

        public ScheduleAdapter(List<ScheduleItem> schedules) {
            this.schedules = schedules;
        }

        @Override
        public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_schedule, parent, false);
            return new ScheduleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ScheduleViewHolder holder, int position) {
            ScheduleItem schedule = schedules.get(position);
            holder.bind(schedule);
        }

        @Override
        public int getItemCount() {
            return schedules.size();
        }

        class ScheduleViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation, tvType;

            ScheduleViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvDescription = itemView.findViewById(R.id.tvDescription);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvLocation = itemView.findViewById(R.id.tvLocation);
                tvType = itemView.findViewById(R.id.tvType);
            }

            void bind(ScheduleItem schedule) {
                tvTitle.setText(schedule.getTitle());
                tvDescription.setText(schedule.getDescription());
                tvDate.setText(schedule.getDate());
                tvTime.setText(schedule.getTime());
                tvLocation.setText(schedule.getLocation());
                tvType.setText(schedule.getType());
            }
        }
    }
}
