package com.example.qldiem.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qldiem.R;
import com.example.qldiem.adapters.EventAdapter;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.EventCalendar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventActivity extends AppCompatActivity implements EventAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private DatabaseHelper db;
    private List<EventCalendar> list;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sự kiện");
        }
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewEvent);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new EventAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        loadData();
        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void loadData() {
        list.clear();
        list.addAll(db.getAllEvents());
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_event, null);
        EditText edtDate = view.findViewById(R.id.edtEventDate);
        EditText edtEvent = view.findViewById(R.id.edtEventName);
        
        edtDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view1, year, month, day) -> {
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
                edtDate.setText(selectedDate);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
        
        new AlertDialog.Builder(this).setTitle("Thêm sự kiện").setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String event = edtEvent.getText().toString().trim();
                    if (!selectedDate.isEmpty() && !event.isEmpty()) {
                        long result = db.addEvent(new EventCalendar(selectedDate, event));
                        Toast.makeText(this, result > 0 ? "Thêm thành công" : "Thất bại", Toast.LENGTH_SHORT).show();
                        if (result > 0) loadData();
                    } else {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public void onEditClick(EventCalendar event) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_event, null);
        EditText edtDate = view.findViewById(R.id.edtEventDate);
        EditText edtEvent = view.findViewById(R.id.edtEventName);
        edtDate.setText(event.getDate());
        edtEvent.setText(event.getEvent());
        selectedDate = event.getDate();
        
        edtDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view1, year, month, day) -> {
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
                edtDate.setText(selectedDate);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
        
        new AlertDialog.Builder(this).setTitle("Sửa sự kiện").setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    event.setDate(selectedDate);
                    event.setEvent(edtEvent.getText().toString().trim());
                    Toast.makeText(this, db.updateEvent(event) > 0 ? "Cập nhật thành công" : "Thất bại", Toast.LENGTH_SHORT).show();
                    loadData();
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public void onDeleteClick(EventCalendar event) {
        new AlertDialog.Builder(this).setTitle("Xác nhận xóa")
                .setMessage("Xóa sự kiện này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(this, db.deleteEvent(event.getId()) > 0 ? "Đã xóa" : "Thất bại", Toast.LENGTH_SHORT).show();
                    loadData();
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }

    @Override
    protected void onDestroy() { super.onDestroy(); if (db != null) db.close(); }
}
