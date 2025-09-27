package com.example.studentscoremanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminStudentListActivity extends AppCompatActivity {

    private TextView tvCount;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private AdminStudentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_list);

        dbHelper = new DatabaseHelper(this);
        tvCount = findViewById(R.id.tvCount);
        recyclerView = findViewById(R.id.recyclerStudents);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        int count = dbHelper.getStudentsCount();
        tvCount.setText("Số sinh viên đã đăng ký: " + count);

        Cursor cursor = dbHelper.getAllStudents();
        if (adapter == null) {
            adapter = new AdminStudentAdapter(cursor);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapCursor(cursor);
        }
    }
}


