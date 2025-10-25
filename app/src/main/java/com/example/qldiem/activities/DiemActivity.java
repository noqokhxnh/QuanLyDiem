package com.example.qldiem.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qldiem.R;
import com.example.qldiem.adapters.DiemAdapter;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.Diem;
import com.example.qldiem.models.MonHoc;
import com.example.qldiem.models.SinhVien;
import com.example.qldiem.utils.SessionManager;
import com.example.qldiem.utils.ValidationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity quản lý điểm số - nơi người dùng có thể xem, thêm, sửa, xóa điểm cho sinh viên
 *
 * CÔNG DỤNG:
 * - Hiển thị danh sách điểm của sinh viên
 * - Cho phép giảng viên thêm/sửa/xóa điểm
 * - Hiển thị điểm theo hệ 10, hệ 4 và hệ chữ
 * - Hỗ trợ hệ thống điểm 3 thành phần: Chuyên cần, Giữa kỳ, Cuối kỳ
 * - Phân quyền theo vai trò người dùng (Admin, Lecturer, Student)
 *
 * CÁC THÀNH PHẦN CHÍNH:
 * - recyclerView: hiển thị danh sách điểm
 * - adapter: adapter để hiển thị điểm trong RecyclerView
 * - db: DatabaseHelper để truy vấn cơ sở dữ liệu
 * - list: danh sách điểm để hiển thị
 * - svList: danh sách sinh viên để chọn trong dialog
 * - mhList: danh sách môn học để chọn trong dialog
 * - session: SessionManager để quản lý phiên làm việc
 * - fabAdd: nút thêm điểm (ẩn với sinh viên)
 *
 * NẾU KHÔNG CÓ ACTIVITY NÀY:
 * - Người dùng không thể quản lý điểm số
 * - Không thể thêm/sửa/xóa điểm
 * - Không thể theo dõi điểm số của sinh viên
 */
public class DiemActivity extends AppCompatActivity implements DiemAdapter.OnItemClickListener {
    // RecyclerView để hiển thị danh sách điểm
    private RecyclerView recyclerView;
    
    // Adapter để kết nối dữ liệu điểm với RecyclerView
    private DiemAdapter adapter;
    
    // Quản lý truy cập cơ sở dữ liệu
    private DatabaseHelper db;
    
    // Danh sách điểm để hiển thị trong RecyclerView
    private List<Diem> list;
    
    // Danh sách sinh viên để chọn trong dialog
    private List<SinhVien> svList;
    
    // Danh sách môn học để chọn trong dialog
    private List<MonHoc> mhList;
    
    // Quản lý phiên đăng nhập của người dùng
    private SessionManager session;
    
    // Nút thêm điểm (Floating Action Button)
    private FloatingActionButton fabAdd;

    /**
     * Hàm khởi tạo activity quản lý điểm, thiết lập giao diện và quyền truy cập
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_diem.xml)
     * - Khởi tạo SessionManager để kiểm tra vai trò người dùng
     * - Thiết lập tiêu đề phù hợp với vai trò (Xem điểm cho sinh viên, Quản lý điểm cho GV/Quản trị)
     * - Khởi tạo DatabaseHelper và các thành phần UI
     * - Thiết lập RecyclerView với adapter
     * - Tải danh sách sinh viên và môn học để sử dụng trong dialog
     * - Ẩn nút thêm điểm nếu là sinh viên (chỉ được xem)
     * - Gán sự kiện click cho nút thêm điểm (nếu có quyền)
     * - Tải dữ liệu điểm để hiển thị
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity quản lý điểm sẽ không được khởi tạo đúng cách
     * - Giao diện quản lý điểm sẽ không hiển thị
     * - Không thể tương tác với các thành phần giao diện
     * - Ứng dụng sẽ bị lỗi khi mở activity quản lý điểm
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diem);
        
        session = new SessionManager(this);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(session.isStudent() ? "Xem điểm" : "Quản lý điểm");
        }
        
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewDiem);
        fabAdd = findViewById(R.id.fabAddDiem);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new DiemAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        
        svList = db.getAllSinhVien();
        mhList = db.getAllMonHoc();
        
        // Ẩn nút thêm điểm nếu là Student
        if (session.isStudent()) {
            fabAdd.setVisibility(View.GONE);
        } else {
            fabAdd.setOnClickListener(v -> showAddDialog());
        }
        
        loadData();
    }

    /**
     * Hàm tải danh sách điểm từ cơ sở dữ liệu và cập nhật cho RecyclerView
     *
     * CÔNG DỤNG:
     * - Làm mới danh sách điểm để hiển thị
     * - Phân biệt quyền người dùng: sinh viên chỉ xem điểm của mình, GV/QTV xem tất cả
     * - Gọi phương thức database để lấy điểm theo mã sinh viên
     * - Cập nhật adapter để hiển thị dữ liệu mới trên giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - maSV: mã sinh viên (lấy từ username của người dùng hiện tại nếu là sinh viên)
     * - sv: đối tượng sinh viên trong vòng lặp (khi người dùng là GV/QTV)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (session, db, list)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Danh sách điểm sẽ không được tải và hiển thị
     * - Người dùng không thấy bất kỳ dữ liệu điểm nào
     * - Giao diện sẽ trống không có thông tin
     */
    private void loadData() {
        list.clear();
        
        if (session.isStudent()) {
            // Student chỉ xem điểm của mình
            String maSV = session.getUsername();
            list.addAll(db.getDiemBySinhVien(maSV));
        } else {
            // Admin/Lecturer xem tất cả
            for (SinhVien sv : svList) {
                list.addAll(db.getDiemBySinhVien(sv.getMaSv()));
            }
        }
        
        adapter.notifyDataSetChanged();
    }

    /**
     * Hàm hiển thị dialog để thêm điểm mới cho sinh viên
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog với các trường nhập: sinh viên, môn học, học kỳ, năm học, các loại điểm
     * - Tạo Spinner để chọn sinh viên và môn học từ danh sách đã tải
     * - Tạo Spinner để chọn học kỳ (1 hoặc 2)
     * - Thiết lập năm học mặc định là năm hiện tại
     * - Xử lý sự kiện khi người dùng nhấn "Lưu" để thêm điểm vào cơ sở dữ liệu
     * - Thực hiện validation trước khi lưu điểm
     *
     * CÁC BIẾN TRONG HÀM:
     * - view: layout của dialog thêm điểm
     * - spinnerSV: spinner chọn sinh viên
     * - spinnerMH: spinner chọn môn học
     * - spinnerHocKy: spinner chọn học kỳ
     * - edtNamHoc: EditText nhập năm học
     * - edtDiemChuyenCan: EditText nhập điểm chuyên cần
     * - edtDiemGiuaKy: EditText nhập điểm giữa kỳ
     * - edtDiemCuoiKy: EditText nhập điểm cuối kỳ
     * - sv, mh: đối tượng sinh viên/môn học được chọn từ spinner
     * - hocKyStr, namHocStr, ccStr, gkStr, ckStr: chuỗi giá trị từ các trường nhập
     * - cc, gk, ck: giá trị điểm đã chuyển đổi sang float
     * - result: kết quả từ thao tác thêm điểm vào database (số bản ghi bị ảnh hưởng)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (svList, mhList, db)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thêm điểm mới cho sinh viên
     * - Giảng viên không thể nhập điểm vào hệ thống
     * - Tính năng quản lý điểm sẽ không đầy đủ
     */
    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_diem, null);
        Spinner spinnerSV = view.findViewById(R.id.spinnerSinhVien);
        Spinner spinnerMH = view.findViewById(R.id.spinnerMonHoc);
        Spinner spinnerHocKy = view.findViewById(R.id.spinnerHocKy);
        EditText edtNamHoc = view.findViewById(R.id.edtNamHoc);
        EditText edtDiemChuyenCan = view.findViewById(R.id.edtDiemChuyenCan);
        EditText edtDiemGiuaKy = view.findViewById(R.id.edtDiemGiuaKy);
        EditText edtDiemCuoiKy = view.findViewById(R.id.edtDiemCuoiKy);
        
        spinnerSV.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, svList));
        spinnerMH.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mhList));
        
        // Setup học kỳ spinner
        String[] hocKyOptions = {"Học kỳ 1", "Học kỳ 2"};
        spinnerHocKy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hocKyOptions));
        
        // Set năm hiện tại
        edtNamHoc.setText(String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
        
        new AlertDialog.Builder(this).setTitle("Nhập điểm").setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    SinhVien sv = (SinhVien) spinnerSV.getSelectedItem();
                    MonHoc mh = (MonHoc) spinnerMH.getSelectedItem();
                    String hocKyStr = spinnerHocKy.getSelectedItem().toString();
                    String namHocStr = edtNamHoc.getText().toString().trim();
                    String ccStr = edtDiemChuyenCan.getText().toString().trim();
                    String gkStr = edtDiemGiuaKy.getText().toString().trim();
                    String ckStr = edtDiemCuoiKy.getText().toString().trim();
                    
                    if (sv == null || mh == null || hocKyStr.isEmpty() || namHocStr.isEmpty()) {
                        Toast.makeText(this, "Vui lòng chọn sinh viên, môn học, học kỳ và năm học", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    int hocKy = hocKyStr.equals("Học kỳ 1") ? 1 : 2;
                    int namHoc = Integer.parseInt(namHocStr);
                    
                    // Validate điểm
                    if (!ValidationUtils.isValidDiem(ccStr) || 
                        !ValidationUtils.isValidDiem(gkStr) || 
                        !ValidationUtils.isValidDiem(ckStr)) {
                        Toast.makeText(this, "Vui lòng nhập điểm hợp lệ (0-10)", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    float cc = Float.parseFloat(ccStr);
                    float gk = Float.parseFloat(gkStr);
                    float ck = Float.parseFloat(ckStr);
                    
                    Diem diem = new Diem(sv.getMaSv(), mh.getMaMH(), hocKy, namHoc);
                    diem.setDiemChuyenCan(cc);
                    diem.setDiemGiuaKy(gk);
                    diem.setDiemCuoiKy(ck);
                    
                    long result = db.addDiem(diem);
                    if(result > 0) {
                        Toast.makeText(this, "Thêm điểm thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Lỗi khi thêm điểm", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Hủy", null).show();
    }

    /**
     * Hàm xử lý sự kiện khi người dùng nhấn nút sửa điểm
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog chỉnh sửa điểm với giá trị hiện tại
     * - Cho phép người dùng chỉnh sửa các thành phần điểm: chuyên cần, giữa kỳ, cuối kỳ
     * - Xác thực các giá trị điểm sau khi người dùng nhập
     * - Cập nhật điểm vào cơ sở dữ liệu nếu xác thực thành công
     * - Tải lại dữ liệu để cập nhật giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - view: layout của dialog sửa điểm
     * - edtDiemChuyenCan, edtDiemGiuaKy, edtDiemCuoiKy: các EditText để nhập điểm
     * - ccStr, gkStr, ckStr: chuỗi giá trị điểm từ các trường nhập
     * - cc, gk, ck: giá trị điểm đã chuyển đổi sang float
     * - result: kết quả từ thao tác cập nhật điểm vào database (số bản ghi bị ảnh hưởng)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Diem diem: đối tượng điểm cần sửa thông tin
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể chỉnh sửa điểm đã nhập
     * - Giảng viên không thể cập nhật điểm nếu nhập sai
     * - Tính năng quản lý điểm sẽ không đầy đủ
     */
    @Override
    public void onEditClick(Diem diem) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_diem, null);
        EditText edtDiemChuyenCan = view.findViewById(R.id.edtDiemChuyenCanEdit);
        EditText edtDiemGiuaKy = view.findViewById(R.id.edtDiemGiuaKyEdit);
        EditText edtDiemCuoiKy = view.findViewById(R.id.edtDiemCuoiKyEdit);
        
        // Set giá trị hiện tại cho các trường điểm
        edtDiemChuyenCan.setText(String.valueOf(diem.getDiemChuyenCan()));
        edtDiemGiuaKy.setText(String.valueOf(diem.getDiemGiuaKy()));
        edtDiemCuoiKy.setText(String.valueOf(diem.getDiemCuoiKy()));
        
        new AlertDialog.Builder(this).setTitle("Sửa điểm").setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String ccStr = edtDiemChuyenCan.getText().toString().trim();
                    String gkStr = edtDiemGiuaKy.getText().toString().trim();
                    String ckStr = edtDiemCuoiKy.getText().toString().trim();
                    
                    // Validate điểm
                    if (!ValidationUtils.isValidDiem(ccStr) || 
                        !ValidationUtils.isValidDiem(gkStr) || 
                        !ValidationUtils.isValidDiem(ckStr)) {
                        Toast.makeText(this, "Vui lòng nhập điểm hợp lệ (0-10)", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    float cc = Float.parseFloat(ccStr);
                    float gk = Float.parseFloat(gkStr);
                    float ck = Float.parseFloat(ckStr);
                    
                    diem.setDiemChuyenCan(cc);
                    diem.setDiemGiuaKy(gk);
                    diem.setDiemCuoiKy(ck);
                    
                    int result = db.updateDiem(diem);
                    if(result > 0) {
                        Toast.makeText(this, "Cập nhật điểm thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Lỗi khi cập nhật điểm", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Hủy", null).show();
    }

    /**
     * Hàm xử lý sự kiện khi người dùng nhấn nút xóa điểm
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog xác nhận xóa điểm
     * - Gọi phương thức xóa điểm từ database nếu người dùng xác nhận
     * - Hiển thị thông báo kết quả xóa
     * - Tải lại dữ liệu để cập nhật giao diện sau khi xóa
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Diem diem: đối tượng điểm cần xóa khỏi cơ sở dữ liệu
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xóa điểm đã nhập
     * - Giảng viên không thể loại bỏ điểm không cần thiết
     * - Tính năng quản lý điểm sẽ không đầy đủ
     */
    @Override
    public void onDeleteClick(Diem diem) {
        new AlertDialog.Builder(this).setTitle("Xác nhận xóa")
                .setMessage("Xóa điểm này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Toast.makeText(this, db.deleteDiem(diem.getMaSv(), diem.getMaMH()) > 0 ? "Đã xóa" : "Thất bại", Toast.LENGTH_SHORT).show();
                    loadData();
                }).setNegativeButton("Hủy", null).show();
    }

    /**
     * Hàm xử lý sự kiện khi người dùng nhấn nút back/up trên ActionBar
     *
     * CÔNG DỤNG:
     * - Kích hoạt hành động quay lại màn hình trước đó
     * - Trả về true để báo hiệu rằng sự kiện đã được xử lý
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - boolean: true để báo hiệu rằng sự kiện đã được xử lý
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Nút back/up trên ActionBar sẽ không hoạt động
     * - Người dùng không thể quay lại màn hình trước đó qua nút này
     * - Trải nghiệm người dùng bị ảnh hưởng tiêu cực
     */
    @Override
    public boolean onSupportNavigateUp() { finish(); return true; }

    /**
     * Hàm được gọi khi activity bị hủy, dùng để giải phóng tài nguyên
     *
     * CÔNG DỤNG:
     * - Đảm bảo đóng kết nối cơ sở dữ liệu nếu còn mở
     * - Giải phóng tài nguyên để tránh rò rỉ bộ nhớ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Kết nối cơ sở dữ liệu có thể không được đóng đúng cách
     * - Có thể xảy ra rò rỉ tài nguyên/bộ nhớ
     * - Ứng dụng có thể gặp lỗi khi chạy lâu dài
     */
    @Override
    protected void onDestroy() { super.onDestroy(); if (db != null) db.close(); }
}
