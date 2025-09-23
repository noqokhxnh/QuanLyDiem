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

import java.util.ArrayList;
import java.util.List;

public class AdminTimetableActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private TimetableAdapter adapter;
    private List<TimetableItem> timetableList;
    private FloatingActionButton fabAddTimetable;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_timetable);

        dbHelper = new DatabaseHelper(this);
        timetableList = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewTimetables);
        fabAddTimetable = findViewById(R.id.fabAddTimetable);

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TimetableAdapter();
        recyclerView.setAdapter(adapter);

        loadTimetables();

        fabAddTimetable.setOnClickListener(v -> showAddTimetableDialog());
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

    private void showAddTimetableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_timetable, null);
        builder.setView(dialogView);

        EditText edtSubject = dialogView.findViewById(R.id.edtSubject);
        Spinner spinnerDay = dialogView.findViewById(R.id.spinnerDay);
        EditText edtStartTime = dialogView.findViewById(R.id.edtStartTime);
        EditText edtEndTime = dialogView.findViewById(R.id.edtEndTime);
        EditText edtRoom = dialogView.findViewById(R.id.edtRoom);
        EditText edtTeacher = dialogView.findViewById(R.id.edtTeacher);
        EditText edtClassName = dialogView.findViewById(R.id.edtClassName);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Setup day spinner
        String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String subject = edtSubject.getText().toString().trim();
            String day = spinnerDay.getSelectedItem().toString();
            String startTime = edtStartTime.getText().toString().trim();
            String endTime = edtEndTime.getText().toString().trim();
            String room = edtRoom.getText().toString().trim();
            String teacher = edtTeacher.getText().toString().trim();
            String className = edtClassName.getText().toString().trim();

            if (subject.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addTimetable(subject, day, startTime, endTime, room, teacher, className)) {
                Toast.makeText(this, "Thêm thời khóa biểu thành công", Toast.LENGTH_SHORT).show();
                loadTimetables();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Lỗi khi thêm thời khóa biểu", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showEditTimetableDialog(TimetableItem timetable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_timetable, null);
        builder.setView(dialogView);

        EditText edtSubject = dialogView.findViewById(R.id.edtSubject);
        Spinner spinnerDay = dialogView.findViewById(R.id.spinnerDay);
        EditText edtStartTime = dialogView.findViewById(R.id.edtStartTime);
        EditText edtEndTime = dialogView.findViewById(R.id.edtEndTime);
        EditText edtRoom = dialogView.findViewById(R.id.edtRoom);
        EditText edtTeacher = dialogView.findViewById(R.id.edtTeacher);
        EditText edtClassName = dialogView.findViewById(R.id.edtClassName);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Fill current data
        edtSubject.setText(timetable.getSubject());
        edtStartTime.setText(timetable.getStartTime());
        edtEndTime.setText(timetable.getEndTime());
        edtRoom.setText(timetable.getRoom());
        edtTeacher.setText(timetable.getTeacher());
        edtClassName.setText(timetable.getClassName());

        // Setup day spinner
        String[] days = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);
        
        // Set selected day
        for (int i = 0; i < days.length; i++) {
            if (days[i].equals(timetable.getDay())) {
                spinnerDay.setSelection(i);
                break;
            }
        }

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String subject = edtSubject.getText().toString().trim();
            String day = spinnerDay.getSelectedItem().toString();
            String startTime = edtStartTime.getText().toString().trim();
            String endTime = edtEndTime.getText().toString().trim();
            String room = edtRoom.getText().toString().trim();
            String teacher = edtTeacher.getText().toString().trim();
            String className = edtClassName.getText().toString().trim();

            if (subject.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update timetable (you'll need to add updateTimetable method to DatabaseHelper)
            if (dbHelper.addTimetable(subject, day, startTime, endTime, room, teacher, className)) {
                Toast.makeText(this, "Cập nhật thời khóa biểu thành công", Toast.LENGTH_SHORT).show();
                loadTimetables();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật thời khóa biểu", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

        @Override
        public TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable, parent, false);
            return new TimetableViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TimetableViewHolder holder, int position) {
            TimetableItem timetable = timetableList.get(position);
            holder.bind(timetable);
        }

        @Override
        public int getItemCount() {
            return timetableList.size();
        }

        class TimetableViewHolder extends RecyclerView.ViewHolder {
            private TextView tvSubject, tvDay, tvTime, tvRoom, tvTeacher, tvClassName;
            private Button btnEdit, btnDelete;

            TimetableViewHolder(View itemView) {
                super(itemView);
                tvSubject = itemView.findViewById(R.id.tvSubject);
                tvDay = itemView.findViewById(R.id.tvDay);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvRoom = itemView.findViewById(R.id.tvRoom);
                tvTeacher = itemView.findViewById(R.id.tvTeacher);
                tvClassName = itemView.findViewById(R.id.tvClassName);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }

            void bind(TimetableItem timetable) {
                tvSubject.setText(timetable.getSubject());
                tvDay.setText(timetable.getDay());
                tvTime.setText(timetable.getStartTime() + " - " + timetable.getEndTime());
                tvRoom.setText(timetable.getRoom());
                tvTeacher.setText(timetable.getTeacher());
                tvClassName.setText(timetable.getClassName());

                btnEdit.setOnClickListener(v -> showEditTimetableDialog(timetable));
                btnDelete.setOnClickListener(v -> {
                    new AlertDialog.Builder(AdminTimetableActivity.this)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa thời khóa biểu này?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                if (dbHelper.deleteTimetable(timetable.getId())) {
                                    Toast.makeText(AdminTimetableActivity.this, "Xóa thời khóa biểu thành công", Toast.LENGTH_SHORT).show();
                                    loadTimetables();
                                } else {
                                    Toast.makeText(AdminTimetableActivity.this, "Lỗi khi xóa thời khóa biểu", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                });
            }
        }
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
}
