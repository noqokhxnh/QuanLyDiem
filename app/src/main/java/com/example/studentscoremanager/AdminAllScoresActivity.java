package com.example.studentscoremanager;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminAllScoresActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private TextView tvTotal;
    private Spinner spSemester, spYear;
    private com.google.android.material.textfield.TextInputEditText edtSubject;
    private DatabaseHelper dbHelper;
    private AdminAllScoresAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_scores);

        dbHelper = new DatabaseHelper(this);
        recycler = findViewById(R.id.recyclerAllScores);
        tvTotal = findViewById(R.id.tvTotalScores);
        spSemester = findViewById(R.id.spSemester);
        spYear = findViewById(R.id.spYear);
        edtSubject = findViewById(R.id.edtSubjectFilter);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminAllScoresAdapter();
        recycler.setAdapter(adapter);

        setupFilters();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void setupFilters() {
        String[] semesters = new String[]{"Tất cả", "HK1", "HK2", "HK Hè"};
        String[] years = new String[]{"Tất cả", "2023-2024", "2024-2025"};
        spSemester.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesters));
        spYear.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years));

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                loadData();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        spSemester.setOnItemSelectedListener(listener);
        spYear.setOnItemSelectedListener(listener);

        if (edtSubject != null) {
            edtSubject.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) { loadData(); }
                @Override public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void loadData() {
        String semester = spSemester.getSelectedItem() != null ? String.valueOf(spSemester.getSelectedItem()) : null;
        String year = spYear.getSelectedItem() != null ? String.valueOf(spYear.getSelectedItem()) : null;
        String subject = edtSubject != null && edtSubject.getText() != null ? edtSubject.getText().toString() : null;

        Cursor c = dbHelper.getAllScoresWithStudent(semester, year, subject);
        adapter.updateCursor(c);
        tvTotal.setText("Tổng bản ghi: " + adapter.getItemCount());
    }
}


