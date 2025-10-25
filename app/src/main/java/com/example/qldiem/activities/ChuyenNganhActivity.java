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
import com.example.qldiem.adapters.ChuyenNganhAdapter;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.ChuyenNganh;
import com.example.qldiem.utils.ValidationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ChuyenNganhActivity extends AppCompatActivity implements ChuyenNganhAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ChuyenNganhAdapter adapter;
    private DatabaseHelper db;
    private List<ChuyenNganh> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_nganh);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý chuyên ngành");
        }
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewChuyenNganh);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddChuyenNganh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new ChuyenNganhAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        loadData();
        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void loadData() {
        list.clear();
        list.addAll(db.getAllChuyenNganh());
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_chuyen_nganh, null);
        EditText edtMa = view.findViewById(R.id.edtMaChuyenNganh);
        EditText edtTen = view.findViewById(R.id.edtTenChuyenNganh);
        new AlertDialog.Builder(this).setTitle("Thêm chuyên ngành mới").setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String ma = edtMa.getText().toString().trim();
                    String ten = edtTen.getText().toString().trim();
                    if (ValidationUtils.isValidMaChuyenNganh(ma) && ValidationUtils.isValidName(ten)) {
                        long result = db.addChuyenNganh(new ChuyenNganh(ma, ten));
                        Toast.makeText(this, result > 0 ? "Thêm thành công" : "Mã đã tồn tại", Toast.LENGTH_SHORT).show();
                        if (result > 0) loadData();
                    } else {
                        Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public void onEditClick(ChuyenNganh cn) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_chuyen_nganh, null);
        EditText edtMa = view.findViewById(R.id.edtMaChuyenNganh);
        EditText edtTen = view.findViewById(R.id.edtTenChuyenNganh);
        edtMa.setText(cn.getMaChuyenNganh());
        edtMa.setEnabled(false);
        edtTen.setText(cn.getTenChuyenNganh());
        new AlertDialog.Builder(this).setTitle("Chỉnh sửa").setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    cn.setTenChuyenNganh(edtTen.getText().toString().trim());
                    Toast.makeText(this, db.updateChuyenNganh(cn) > 0 ? "Cập nhật thành công" : "Thất bại", Toast.LENGTH_SHORT).show();
                    loadData();
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public void onDeleteClick(ChuyenNganh cn) {
        new AlertDialog.Builder(this).setTitle("Xác nhận xóa")
                .setMessage("Xóa " + cn.getTenChuyenNganh() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(this, db.deleteChuyenNganh(cn.getMaChuyenNganh()) > 0 ? "Đã xóa" : "Thất bại", Toast.LENGTH_SHORT).show();
                    loadData();
                }).setNegativeButton("Hủy", null).show();
    }

    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }

    @Override
    protected void onDestroy() { super.onDestroy(); if (db != null) db.close(); }
}
