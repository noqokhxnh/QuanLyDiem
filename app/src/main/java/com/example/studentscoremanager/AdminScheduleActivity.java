package com.example.studentscoremanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminScheduleActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<ScheduleItem> scheduleList;
    private FloatingActionButton fabAddSchedule;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_schedule);

        dbHelper = new DatabaseHelper(this);
        scheduleList = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewSchedules);
        fabAddSchedule = findViewById(R.id.fabAddSchedule);

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScheduleAdapter();
        recyclerView.setAdapter(adapter);

        loadSchedules();

        fabAddSchedule.setOnClickListener(v -> showAddScheduleDialog());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    private void showAddScheduleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_schedule, null);
        builder.setView(dialogView);

        EditText edtTitle = dialogView.findViewById(R.id.edtTitle);
        EditText edtDescription = dialogView.findViewById(R.id.edtDescription);
        EditText edtDate = dialogView.findViewById(R.id.edtDate);
        EditText edtTime = dialogView.findViewById(R.id.edtTime);
        EditText edtLocation = dialogView.findViewById(R.id.edtLocation);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Setup spinner
        String[] types = {"exam", "assignment", "event"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // Set current date as default
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        edtDate.setText(dateFormat.format(new Date()));

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String date = edtDate.getText().toString().trim();
            String time = edtTime.getText().toString().trim();
            String location = edtLocation.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();

            if (title.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề và ngày", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addSchedule(title, description, date, time, location, type)) {
                Toast.makeText(this, "Thêm lịch học thành công", Toast.LENGTH_SHORT).show();
                loadSchedules();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Lỗi khi thêm lịch học", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showEditScheduleDialog(ScheduleItem schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_schedule, null);
        builder.setView(dialogView);

        EditText edtTitle = dialogView.findViewById(R.id.edtTitle);
        EditText edtDescription = dialogView.findViewById(R.id.edtDescription);
        EditText edtDate = dialogView.findViewById(R.id.edtDate);
        EditText edtTime = dialogView.findViewById(R.id.edtTime);
        EditText edtLocation = dialogView.findViewById(R.id.edtLocation);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Fill current data
        edtTitle.setText(schedule.getTitle());
        edtDescription.setText(schedule.getDescription());
        edtDate.setText(schedule.getDate());
        edtTime.setText(schedule.getTime());
        edtLocation.setText(schedule.getLocation());

        // Setup spinner
        String[] types = {"exam", "assignment", "event"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        
        // Set selected type
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(schedule.getType())) {
                spinnerType.setSelection(i);
                break;
            }
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String date = edtDate.getText().toString().trim();
            String time = edtTime.getText().toString().trim();
            String location = edtLocation.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();

            if (title.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề và ngày", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update schedule (you'll need to add updateSchedule method to DatabaseHelper)
            if (dbHelper.addSchedule(title, description, date, time, location, type)) {
                Toast.makeText(this, "Cập nhật lịch học thành công", Toast.LENGTH_SHORT).show();
                loadSchedules();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật lịch học", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

        @Override
        public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
            return new ScheduleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ScheduleViewHolder holder, int position) {
            ScheduleItem schedule = scheduleList.get(position);
            holder.bind(schedule);
        }

        @Override
        public int getItemCount() {
            return scheduleList.size();
        }

        class ScheduleViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation, tvType;
            private Button btnEdit, btnDelete;

            ScheduleViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvDescription = itemView.findViewById(R.id.tvDescription);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvLocation = itemView.findViewById(R.id.tvLocation);
                tvType = itemView.findViewById(R.id.tvType);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }

            void bind(ScheduleItem schedule) {
                tvTitle.setText(schedule.getTitle());
                tvDescription.setText(schedule.getDescription());
                tvDate.setText(schedule.getDate());
                tvTime.setText(schedule.getTime());
                tvLocation.setText(schedule.getLocation());
                tvType.setText(schedule.getType());

                btnEdit.setOnClickListener(v -> showEditScheduleDialog(schedule));
                btnDelete.setOnClickListener(v -> {
                    new AlertDialog.Builder(AdminScheduleActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa lịch học này?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                if (dbHelper.deleteSchedule(schedule.getId())) {
                                    Toast.makeText(AdminScheduleActivity.this, "Xóa lịch học thành công", Toast.LENGTH_SHORT).show();
                                    loadSchedules();
                                } else {
                                    Toast.makeText(AdminScheduleActivity.this, "Lỗi khi xóa lịch học", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                });
            }
        }
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
}
