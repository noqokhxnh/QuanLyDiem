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
import com.example.qldiem.adapters.LopAdapter;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.Lop;
import com.example.qldiem.utils.ValidationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity quản lý lớp học - nơi hiển thị và quản lý tất cả lớp học trong hệ thống
 *
 * CÔNG DỤNG:
 * - Hiển thị danh sách tất cả lớp học trong hệ thống dưới dạng RecyclerView
 * - Cho phép thêm lớp học mới với đầy đủ thông tin (mã lớp, tên lớp)
 * - Cho phép xem chi tiết hoặc chỉnh sửa thông tin lớp học
 * - Cho phép xóa lớp học không cần thiết
 * - Tự động tải lại danh sách khi quay lại activity
 *
 * CÁC THÀNH PHẦN CHÍNH:
 * - recyclerView: hiển thị danh sách lớp học
 * - adapter: adapter để hiển thị lớp học trong RecyclerView
 * - db: DatabaseHelper để truy vấn cơ sở dữ liệu
 * - lopList: danh sách lớp học để hiển thị
 * - fabAdd: Floating Action Button để thêm lớp học mới
 *
 * NẾU KHÔNG CÓ ACTIVITY NÀY:
 * - Không thể quản lý danh sách lớp học trong hệ thống
 * - Không thể thêm, sửa hoặc xóa thông tin lớp học
 * - Tính năng quản lý lớp học sẽ không hoạt động
 * - Sinh viên không thể được phân bổ vào lớp học phù hợp
 */
public class LopActivity extends AppCompatActivity implements LopAdapter.OnItemClickListener {

    // RecyclerView để hiển thị danh sách lớp học
    private RecyclerView recyclerView;
    
    // Adapter để kết nối dữ liệu lớp học với RecyclerView
    private LopAdapter adapter;
    
    // Quản lý truy cập cơ sở dữ liệu
    private DatabaseHelper db;
    
    // Danh sách lớp học để hiển thị trong RecyclerView
    private List<Lop> lopList;
    
    // Floating Action Button để thêm lớp học mới
    private FloatingActionButton fabAdd;

    /**
     * Hàm khởi tạo activity quản lý lớp học, thiết lập giao diện và các thành phần
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_lop.xml)
     * - Thiết lập tiêu đề và nút back cho ActionBar
     * - Khởi tạo DatabaseHelper để truy vấn cơ sở dữ liệu
     * - Ánh xạ các thành phần giao diện (RecyclerView, FAB)
     * - Thiết lập RecyclerView với adapter
     * - Tải danh sách lớp học để hiển thị
     * - Gán sự kiện click cho nút thêm lớp học
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity quản lý lớp học sẽ không được khởi tạo đúng cách
     * - Giao diện quản lý lớp học sẽ không hiển thị
     * - Không thể tương tác với các thành phần giao diện
     * - Ứng dụng sẽ bị lỗi khi mở activity quản lý lớp học
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lop);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý lớp học");
        }

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewLop);
        fabAdd = findViewById(R.id.fabAddLop);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lopList = new ArrayList<>();
        adapter = new LopAdapter(this, lopList, this);
        recyclerView.setAdapter(adapter);

        loadData();

        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    /**
     * Hàm tải danh sách tất cả lớp học từ cơ sở dữ liệu và cập nhật cho RecyclerView
     *
     * CÔNG DỤNG:
     * - Làm mới danh sách lớp học để hiển thị
     * - Gọi phương thức database để lấy tất cả lớp học
     * - Cập nhật adapter để hiển thị dữ liệu mới trên giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (db, lopList, adapter)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Danh sách lớp học sẽ không được tải và hiển thị
     * - Người dùng không thấy bất kỳ dữ liệu lớp học nào
     * - Giao diện sẽ trống không có thông tin
     */
    private void loadData() {
        lopList.clear();
        lopList.addAll(db.getAllLop());
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_lop, null);
        EditText edtMaLop = view.findViewById(R.id.edtMaLop);
        EditText edtTenLop = view.findViewById(R.id.edtTenLop);

        new AlertDialog.Builder(this)
                .setTitle("Thêm lớp học mới")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String maLop = edtMaLop.getText().toString().trim();
                    String tenLop = edtTenLop.getText().toString().trim();

                    if (!ValidationUtils.isValidMaLop(maLop)) {
                        Toast.makeText(this, "Mã lớp không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!ValidationUtils.isValidName(tenLop)) {
                        Toast.makeText(this, "Tên lớp không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Lop lop = new Lop(maLop, tenLop);
                    long result = db.addLop(lop);
                    
                    if (result > 0) {
                        Toast.makeText(this, "Thêm lớp thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Mã lớp đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onEditClick(Lop lop) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_lop, null);
        EditText edtMaLop = view.findViewById(R.id.edtMaLop);
        EditText edtTenLop = view.findViewById(R.id.edtTenLop);

        edtMaLop.setText(lop.getMaLop());
        edtMaLop.setEnabled(false);
        edtTenLop.setText(lop.getTenLop());

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa lớp học")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String tenLop = edtTenLop.getText().toString().trim();

                    if (!ValidationUtils.isValidName(tenLop)) {
                        Toast.makeText(this, "Tên lớp không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    lop.setTenLop(tenLop);
                    int result = db.updateLop(lop);
                    
                    if (result > 0) {
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onDeleteClick(Lop lop) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa lớp " + lop.getTenLop() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = db.deleteLop(lop.getMaLop());
                    if (result > 0) {
                        Toast.makeText(this, "Đã xóa lớp", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}
