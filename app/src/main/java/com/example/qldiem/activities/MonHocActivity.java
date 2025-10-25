package com.example.qldiem.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qldiem.R;
import com.example.qldiem.adapters.MonHocAdapter;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.MonHoc;
import com.example.qldiem.utils.ValidationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MonHocActivity extends AppCompatActivity implements MonHocAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private MonHocAdapter adapter;
    private DatabaseHelper db;
    private List<MonHoc> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý môn học");
        }
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewMonHoc);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddMonHoc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new MonHocAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        loadData();
        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void loadData() {
        list.clear();
        list.addAll(db.getAllMonHoc());
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_mon_hoc, null);
        EditText edtMa = view.findViewById(R.id.edtMaMH);
        EditText edtTen = view.findViewById(R.id.edtTenMH);
        new AlertDialog.Builder(this).setTitle("Thêm môn học mới").setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String ma = edtMa.getText().toString().trim();
                    String ten = edtTen.getText().toString().trim();
                    if (ValidationUtils.isValidMaMH(ma) && ValidationUtils.isValidName(ten)) {
                        long result = db.addMonHoc(new MonHoc(ma, ten));
                        Toast.makeText(this, result > 0 ? "Thêm thành công" : "Mã đã tồn tại", Toast.LENGTH_SHORT).show();
                        if (result > 0) loadData();
                    } else {
                        Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public void onEditClick(MonHoc mh) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_mon_hoc, null);
        EditText edtMa = view.findViewById(R.id.edtMaMH);
        EditText edtTen = view.findViewById(R.id.edtTenMH);
        edtMa.setText(mh.getMaMH());
        edtMa.setEnabled(false);
        edtTen.setText(mh.getTenMonHoc());
        new AlertDialog.Builder(this).setTitle("Chỉnh sửa").setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    mh.setTenMonHoc(edtTen.getText().toString().trim());
                    Toast.makeText(this, db.updateMonHoc(mh) > 0 ? "Cập nhật thành công" : "Thất bại", Toast.LENGTH_SHORT).show();
                    loadData();
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public void onDeleteClick(MonHoc mh) {
        new AlertDialog.Builder(this).setTitle("Xác nhận xóa")
                .setMessage("Xóa " + mh.getTenMonHoc() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(this, db.deleteMonHoc(mh.getMaMH()) > 0 ? "Đã xóa" : "Thất bại", Toast.LENGTH_SHORT).show();
                    loadData();
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }

    @Override
    protected void onDestroy() { super.onDestroy(); if (db != null) db.close(); }
}
