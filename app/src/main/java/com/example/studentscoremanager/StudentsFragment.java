package com.example.studentscoremanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StudentsFragment extends Fragment {

    private TextView tvStudentName, tvStudentId, tvStudentClass;
    private DatabaseHelper dbHelper;
    private String currentStudentId = "20201234";
    private SharedPreferencesHelper prefsHelper;

    public StudentsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DatabaseHelper(getContext());
        prefsHelper = new SharedPreferencesHelper(requireContext());


        tvStudentName = view.findViewById(R.id.tvStudentName);
        tvStudentId = view.findViewById(R.id.tvStudentId);
        tvStudentClass = view.findViewById(R.id.tvStudentClass);


        loadStudentInfo();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tự động tải lại thông tin sinh viên khi màn hình hiển thị lại
        loadStudentInfo();
    }

    private void loadStudentInfo() {
        String prefId = prefsHelper.getCurrentStudentId();
        if (prefId != null && !prefId.isEmpty()) {
            currentStudentId = prefId;
        }

        Cursor cursor = dbHelper.getStudentById(currentStudentId);
        if (cursor != null && cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME));
            String studentId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STUDENT_ID));
            String className = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CLASS));

            // Gán dữ liệu từ DB vào layout
            tvStudentName.setText(fullName);
            tvStudentId.setText("MSSV: " + studentId);
            tvStudentClass.setText("Lớp: " + className);

            cursor.close();
        } else {
            setDefaultStudentInfo();
        }
    }

    private void setDefaultStudentInfo() {
        tvStudentName.setText("Nguyễn Văn A");
        tvStudentId.setText("MSSV: 20201234");
        tvStudentClass.setText("Lớp: CNTT01");
        Toast.makeText(getContext(), "Không tìm thấy thông tin sinh viên, hiển thị dữ liệu mặc định!", Toast.LENGTH_SHORT).show();
    }
}
