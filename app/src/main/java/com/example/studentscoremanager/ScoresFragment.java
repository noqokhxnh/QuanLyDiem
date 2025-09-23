package com.example.studentscoremanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class ScoresFragment extends Fragment {

    private RecyclerView recyclerViewScores;
    private ScoreAdapter scoreAdapter;
    private DatabaseHelper dbHelper;
    private TextView tvStudentInfo, tvGPA, tvTotalSubjects;
    private Spinner spinnerSemester, spinnerYear;
    private String subjectQuery = "";
    private String currentStudentId = "20201234"; 
    private SharedPreferencesHelper prefsHelper;
    private BarChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, container, false);

        dbHelper = new DatabaseHelper(getContext());
        prefsHelper = new SharedPreferencesHelper(requireContext());

        // Ánh xạ views
        recyclerViewScores = view.findViewById(R.id.recyclerViewScores);
        tvStudentInfo = view.findViewById(R.id.tvStudentInfo);
        tvGPA = view.findViewById(R.id.tvGPA);
        tvTotalSubjects = view.findViewById(R.id.tvTotalSubjects);
        spinnerSemester = view.findViewById(R.id.spinnerSemester);
        spinnerYear = view.findViewById(R.id.spinnerYear);
        chart = view.findViewById(R.id.chartScores);
        com.google.android.material.textfield.TextInputEditText edtSearch = view.findViewById(R.id.edtSearchSubject);

        setupRecyclerView();
        setupSpinners();
        if (edtSearch != null) {
            edtSearch.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    subjectQuery = s.toString();
                    loadScores();
                }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });
        }
        loadStudentInfo();
        loadScores();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tự động tải lại thông tin và điểm khi màn hình hiển thị lại
        loadStudentInfo();
        loadScores();
    }

    private void setupRecyclerView() {
        scoreAdapter = new ScoreAdapter(getContext());
        recyclerViewScores.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewScores.setAdapter(scoreAdapter);
    }

    private void setupSpinners() {
        // Setup semester spinner
        List<String> semesters = new ArrayList<>();
        semesters.add("Tất cả");
        semesters.add("HK1");
        semesters.add("HK2");
        semesters.add("HK3");

        ArrayAdapter<String> semesterAdapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, semesters);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(semesterAdapter);

        // Setup year spinner
        List<String> years = new ArrayList<>();
        years.add("Tất cả");
        years.add("2024-2025");
        years.add("2023-2024");
        years.add("2022-2023");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);

        // Setup listeners
        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadScores();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadScores();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadStudentInfo() {
        String prefId = prefsHelper.getCurrentStudentId();
        if (prefId != null && !prefId.isEmpty()) {
            currentStudentId = prefId;
        }

        Cursor studentCursor = dbHelper.getStudentById(currentStudentId);
        if (studentCursor != null && studentCursor.moveToFirst()) {
            String fullName = studentCursor.getString(studentCursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME));
            String studentId = studentCursor.getString(studentCursor.getColumnIndexOrThrow(DatabaseHelper.COL_STUDENT_ID));
            tvStudentInfo.setText("Sinh viên: " + fullName + " - " + studentId);
            studentCursor.close();
        }
    }

    private void loadScores() {
        try {
            Cursor scoresCursor = dbHelper.getScoresByFilters(currentStudentId, subjectQuery,
                    spinnerSemester.getSelectedItem() != null ? spinnerSemester.getSelectedItem().toString() : "Tất cả",
                    spinnerYear.getSelectedItem() != null ? spinnerYear.getSelectedItem().toString() : "Tất cả");
            
            if (scoresCursor != null && scoresCursor.getCount() > 0) {
                // Tính GPA và số môn học
                double totalScore = 0;
                int subjectCount = 0;
                
                scoresCursor.moveToFirst();
                java.util.List<BarEntry> entries = new java.util.ArrayList<>();
                java.util.List<String> labels = new java.util.ArrayList<>();
                int idx = 0;
                do {
                    double averageScore = scoresCursor.getDouble(scoresCursor.getColumnIndexOrThrow(DatabaseHelper.COL_AVERAGE_SCORE));
                    String subject = scoresCursor.getString(scoresCursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBJECT));
                    totalScore += averageScore;
                    subjectCount++;
                    entries.add(new BarEntry(idx, (float) averageScore));
                    labels.add(subject);
                    idx++;
                } while (scoresCursor.moveToNext());

                // Cập nhật thống kê
                if (subjectCount > 0) {
                    double gpa = totalScore / subjectCount;
                    tvGPA.setText(String.format("%.1f", gpa));
                    tvTotalSubjects.setText(String.valueOf(subjectCount));
                }

                // Reset cursor để adapter có thể sử dụng
                scoresCursor.moveToFirst();

                // Vẽ biểu đồ nếu có chart view
                if (chart != null) {
                    BarDataSet dataSet = new BarDataSet(entries, "Điểm TB theo môn");
                    dataSet.setColor(0xFF3F51B5);
                    BarData data = new BarData(dataSet);
                    data.setBarWidth(0.6f);
                    chart.setData(data);
                    chart.getAxisRight().setEnabled(false);
                    chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    chart.getDescription().setEnabled(false);
                    chart.getLegend().setEnabled(false);
                    chart.invalidate();
                }
            } else {
                tvGPA.setText("0.0");
                tvTotalSubjects.setText("0");
                if (chart != null) {
                    chart.clear();
                    chart.invalidate();
                }
            }

            scoreAdapter.updateCursor(scoresCursor);
        } catch (Exception e) {
            e.printStackTrace();
            tvGPA.setText("0.0");
            tvTotalSubjects.setText("0");
        }
    }
}
