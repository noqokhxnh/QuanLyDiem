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
import com.example.qldiem.adapters.TaiKhoanAdapter;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.TaiKhoan;
import com.example.qldiem.models.SinhVien;
import com.example.qldiem.models.Lop;
import com.example.qldiem.models.ChuyenNganh;
import com.example.qldiem.utils.PasswordUtils;
import com.example.qldiem.utils.SessionManager;
import com.example.qldiem.utils.ValidationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity quản lý tài khoản - nơi Admin quản lý tất cả tài khoản trong hệ thống
 *
 * CÔNG DỤNG:
 * - Hiển thị danh sách tất cả tài khoản trong hệ thống
 * - Cho phép Admin tạo tài khoản mới (Student hoặc Lecturer)
 * - Cho phép Admin sửa thông tin tài khoản (trừ tài khoản Admin khác)
 * - Cho phép Admin xóa tài khoản (trừ tài khoản Admin khác)
 * - Khi tạo tài khoản Student, tự động tạo hồ sơ sinh viên tương ứng
 * - Phân quyền chặt chẽ: chỉ Admin mới được truy cập
 *
 * CÁC THÀNH PHẦN CHÍNH:
 * - recyclerView: hiển thị danh sách tài khoản
 * - adapter: adapter để hiển thị tài khoản trong RecyclerView
 * - db: DatabaseHelper để truy vấn cơ sở dữ liệu
 * - list: danh sách tài khoản để hiển thị
 * - session: SessionManager để kiểm tra quyền Admin
 *
 * NẾU KHÔNG CÓ ACTIVITY NÀY:
 * - Admin không thể quản lý tài khoản người dùng
 * - Không thể tạo tài khoản mới cho giảng viên hoặc sinh viên
 * - Không thể cập nhật hoặc xóa tài khoản không cần thiết
 */
public class TaiKhoanActivity extends AppCompatActivity implements TaiKhoanAdapter.OnItemClickListener {
    // RecyclerView để hiển thị danh sách tài khoản
    private RecyclerView recyclerView;
    
    // Adapter để kết nối dữ liệu tài khoản với RecyclerView
    private TaiKhoanAdapter adapter;
    
    // Quản lý truy cập cơ sở dữ liệu
    private DatabaseHelper db;
    
    // Danh sách tài khoản để hiển thị trong RecyclerView
    private List<TaiKhoan> list;
    
    // Quản lý phiên đăng nhập của người dùng
    private SessionManager session;

    /**
     * Hàm khởi tạo activity quản lý tài khoản, thiết lập giao diện và kiểm tra quyền truy cập
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_tai_khoan.xml)
     * - Khởi tạo SessionManager để kiểm tra quyền Admin
     * - Kiểm tra nếu người dùng không phải Admin thì từ chối truy cập
     * - Thiết lập tiêu đề và nút back cho ActionBar
     * - Khởi tạo DatabaseHelper và các thành phần UI
     * - Thiết lập RecyclerView với adapter
     * - Tải danh sách tài khoản để hiển thị
     * - Gán sự kiện click cho nút thêm tài khoản
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity quản lý tài khoản sẽ không được khởi tạo đúng cách
     * - Giao diện quản lý tài khoản sẽ không hiển thị
     * - Không thể kiểm tra quyền Admin trước khi cho phép truy cập
     * - Ứng dụng sẽ bị lỗi khi mở activity quản lý tài khoản
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);

        session = new SessionManager(this);
        
        // Chỉ Admin mới được vào
        if (!session.isAdmin()) {
            Toast.makeText(this, "Chỉ Admin mới có quyền quản lý tài khoản!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý tài khoản");
        }

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewTaiKhoan);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddTaiKhoan);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new TaiKhoanAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

        loadData();
        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    /**
     * Hàm tải danh sách tài khoản từ cơ sở dữ liệu và cập nhật cho RecyclerView
     *
     * CÔNG DỤNG:
     * - Làm mới danh sách tài khoản để hiển thị
     * - Gọi phương thức database để lấy tất cả tài khoản
     * - Cập nhật adapter để hiển thị dữ liệu mới trên giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (db, list, adapter)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Danh sách tài khoản sẽ không được tải và hiển thị
     * - Người dùng không thấy bất kỳ dữ liệu tài khoản nào
     * - Giao diện sẽ trống không có thông tin
     */
    private void loadData() {
        list.clear();
        list.addAll(db.getAllTaiKhoan());
        adapter.notifyDataSetChanged();
    }

    /**
     * Hàm hiển thị dialog để tạo tài khoản mới và tự động tạo hồ sơ sinh viên nếu cần
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog với các trường nhập: tên đăng nhập, mật khẩu, họ tên, email, vai trò
     * - Tạo Spinner để chọn vai trò (Student hoặc Lecturer)
     * - Tải danh sách lớp và chuyên ngành để chọn khi tạo tài khoản Student
     * - Thực hiện validation để đảm bảo thông tin hợp lệ
     * - Kiểm tra tên đăng nhập đã tồn tại chưa
     * - Tạo tài khoản mới trong cơ sở dữ liệu
     * - Nếu tạo tài khoản Student, tự động tạo hồ sơ sinh viên tương ứng
     * - Hiển thị thông báo kết quả và tải lại danh sách
     *
     * CÁC BIẾN TRONG HÀM:
     * - view: layout của dialog tạo tài khoản
     * - edtUsername, edtPassword, edtFullName, edtEmail: các EditText để nhập thông tin
     * - spinnerRole, spinnerLop, spinnerChuyenNganh: các Spinner để chọn vai trò, lớp, chuyên ngành
     * - roles: mảng các vai trò có thể chọn
     * - roleAdapter: ArrayAdapter cho vai trò
     * - lopList: danh sách lớp học từ database
     * - lopNames: danh sách tên lớp để hiển thị trong Spinner
     * - lopAdapter: ArrayAdapter cho lớp học
     * - cnList: danh sách chuyên ngành từ database
     * - cnNames: danh sách tên chuyên ngành để hiển thị trong Spinner
     * - cnAdapter: ArrayAdapter cho chuyên ngành
     * - username, password, fullName, email, role: chuỗi giá trị từ các trường nhập
     * - taiKhoan: đối tượng tài khoản mới tạo
     * - result: kết quả từ thao tác thêm tài khoản vào database
     * - lopPos, cnPos: vị trí được chọn trong Spinner lớp và chuyên ngành
     * - maLop, maChuyenNganh: mã lớp và chuyên ngành được chọn
     * - sv: đối tượng sinh viên mới tạo
     * - svResult: kết quả từ thao tác thêm sinh viên vào database
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (db)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo tài khoản mới trong hệ thống
     * - Admin không thể thêm người dùng mới (Student hoặc Lecturer)
     * - Không thể tự động tạo hồ sơ sinh viên khi tạo tài khoản Student
     * - Tính năng quản lý tài khoản sẽ không đầy đủ
     */
    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_tai_khoan, null);
        EditText edtUsername = view.findViewById(R.id.edtUsername);
        EditText edtPassword = view.findViewById(R.id.edtPassword);
        EditText edtFullName = view.findViewById(R.id.edtFullName);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        Spinner spinnerRole = view.findViewById(R.id.spinnerRole);
        Spinner spinnerLop = view.findViewById(R.id.spinnerLop);
        Spinner spinnerChuyenNganh = view.findViewById(R.id.spinnerChuyenNganh);

        // Chỉ cho phép tạo Student và Lecturer
        String[] roles = {"Student", "Lecturer"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        // Load danh sách lớp
        List<Lop> lopList = db.getAllLop();
        List<String> lopNames = new ArrayList<>();
        lopNames.add("-- Chọn lớp --");
        for (Lop l : lopList) {
            lopNames.add(l.getMaLop() + " - " + l.getTenLop());
        }
        ArrayAdapter<String> lopAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lopNames);
        lopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(lopAdapter);

        // Load danh sách chuyên ngành
        List<ChuyenNganh> cnList = db.getAllChuyenNganh();
        List<String> cnNames = new ArrayList<>();
        cnNames.add("-- Chọn chuyên ngành --");
        for (ChuyenNganh cn : cnList) {
            cnNames.add(cn.getMaChuyenNganh() + " - " + cn.getTenChuyenNganh());
        }
        ArrayAdapter<String> cnAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cnNames);
        cnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChuyenNganh.setAdapter(cnAdapter);

        new AlertDialog.Builder(this)
                .setTitle("Tạo tài khoản mới")
                .setView(view)
                .setPositiveButton("Tạo", (dialog, which) -> {
                    String username = edtUsername.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();
                    String fullName = edtFullName.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String role = spinnerRole.getSelectedItem().toString();

                    // Validation
                    if (!ValidationUtils.isValidUsername(username)) {
                        Toast.makeText(this, "Tên đăng nhập không hợp lệ (3-20 ký tự)", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!PasswordUtils.isValidPassword(password)) {
                        Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!ValidationUtils.isValidName(fullName)) {
                        Toast.makeText(this, "Họ tên không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (db.checkTaiKhoanExists(username)) {
                        Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Tạo tài khoản
                    TaiKhoan taiKhoan = new TaiKhoan(username, password, role, fullName);
                    long result = db.addTaiKhoan(taiKhoan);

                    if (result > 0) {
                        // Nếu là Student thì tạo luôn hồ sơ sinh viên
                        if ("Student".equals(role)) {
                            int lopPos = spinnerLop.getSelectedItemPosition();
                            int cnPos = spinnerChuyenNganh.getSelectedItemPosition();

                            if (lopPos == 0 || cnPos == 0) {
                                Toast.makeText(this, "Vui lòng chọn lớp và chuyên ngành cho sinh viên", Toast.LENGTH_LONG).show();
                                return;
                            }

                            if (!ValidationUtils.isValidEmail(email)) {
                                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Lấy mã lớp và chuyên ngành
                            String maLop = lopList.get(lopPos - 1).getMaLop();
                            String maChuyenNganh = cnList.get(cnPos - 1).getMaChuyenNganh();

                            // Tạo hồ sơ sinh viên (maSV = username)
                            SinhVien sv = new SinhVien();
                            sv.setMaSv(username); // Mã SV = tên đăng nhập
                            sv.setTenSV(fullName);
                            sv.setEmail(email);
                            sv.setMaLop(maLop);
                            sv.setMaChuyenNganh(maChuyenNganh);
                            sv.setHinh(""); // Chưa có hình

                            long svResult = db.addSinhVien(sv);
                            if (svResult > 0) {
                                Toast.makeText(this, "Tạo tài khoản và hồ sơ sinh viên thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Tạo tài khoản thành công nhưng lỗi khi tạo hồ sơ sinh viên", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Tạo tài khoản Lecturer thành công!", Toast.LENGTH_SHORT).show();
                        }
                        loadData();
                    } else {
                        Toast.makeText(this, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    /**
     * Hàm xử lý sự kiện khi người dùng nhấn nút sửa tài khoản
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog chỉnh sửa thông tin tài khoản với giá trị hiện tại
     * - Ngăn không cho sửa tài khoản Admin (bảo vệ tài khoản hệ thống)
     * - Cho phép chỉnh sửa họ tên, vai trò và mật khẩu (nếu muốn đổi)
     * - Thực hiện validation để đảm bảo thông tin hợp lệ
     * - Cập nhật tài khoản trong cơ sở dữ liệu nếu validation pass
     * - Tải lại danh sách để cập nhật giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - view: layout của dialog sửa tài khoản
     * - edtPassword, edtFullName: các EditText để nhập mật khẩu mới và họ tên
     * - spinnerRole: Spinner để chọn vai trò mới
     * - roles: mảng các vai trò có thể chọn
     * - roleAdapter: ArrayAdapter cho vai trò
     * - newPassword, newFullName, newRole: chuỗi giá trị từ các trường nhập
     * - result: kết quả từ thao tác cập nhật tài khoản vào database
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - TaiKhoan taiKhoan: đối tượng tài khoản cần sửa thông tin
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể chỉnh sửa thông tin tài khoản đã tạo
     * - Admin không thể cập nhật thông tin người dùng khi cần thiết
     * - Tính năng quản lý tài khoản sẽ không đầy đủ
     */
    @Override
    public void onEditClick(TaiKhoan taiKhoan) {
        // Không cho sửa tài khoản admin
        if ("Admin".equals(taiKhoan.getVaiTro())) {
            Toast.makeText(this, "Không thể sửa tài khoản Admin", Toast.LENGTH_SHORT).show();
            return;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_tai_khoan, null);
        EditText edtPassword = view.findViewById(R.id.edtPasswordEdit);
        EditText edtFullName = view.findViewById(R.id.edtFullNameEdit);
        Spinner spinnerRole = view.findViewById(R.id.spinnerRoleEdit);

        edtFullName.setText(taiKhoan.getHoTen());

        String[] roles = {"Student", "Lecturer"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        // Set vai trò hiện tại
        spinnerRole.setSelection(taiKhoan.getVaiTro().equals("Student") ? 0 : 1);

        new AlertDialog.Builder(this)
                .setTitle("Sửa tài khoản: " + taiKhoan.getTenTaiKhoan())
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newPassword = edtPassword.getText().toString().trim();
                    String newFullName = edtFullName.getText().toString().trim();
                    String newRole = spinnerRole.getSelectedItem().toString();

                    // Validation
                    if (!ValidationUtils.isValidName(newFullName)) {
                        Toast.makeText(this, "Họ tên không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Cập nhật thông tin
                    taiKhoan.setHoTen(newFullName);
                    taiKhoan.setVaiTro(newRole);

                    // Nếu có đổi mật khẩu
                    if (!newPassword.isEmpty()) {
                        if (!PasswordUtils.isValidPassword(newPassword)) {
                            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        taiKhoan.setMatKhau(PasswordUtils.hashPassword(newPassword));
                    }

                    int result = db.updateTaiKhoan(taiKhoan);
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

    /**
     * Hàm xử lý sự kiện khi người dùng nhấn nút xóa tài khoản
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog xác nhận xóa tài khoản
     * - Ngăn không cho xóa tài khoản Admin (bảo vệ tài khoản hệ thống)
     * - Cảnh báo người dùng về hậu quả khi xóa tài khoản Student (xóa cả hồ sơ và điểm)
     * - Xóa tài khoản khỏi cơ sở dữ liệu nếu người dùng xác nhận
     * - Nếu là tài khoản Student thì xóa cả hồ sơ sinh viên (và điểm tự động theo cascade)
     * - Hiển thị thông báo kết quả và tải lại danh sách
     *
     * CÁC BIẾN TRONG HÀM:
     * - result: kết quả từ thao tác xóa tài khoản khỏi database (số bản ghi bị ảnh hưởng)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - TaiKhoan taiKhoan: đối tượng tài khoản cần xóa
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xóa tài khoản không cần thiết khỏi hệ thống
     * - Admin không thể dọn dẹp tài khoản cũ hoặc không hợp lệ
     * - Tính năng quản lý tài khoản sẽ không đầy đủ
     */
    @Override
    public void onDeleteClick(TaiKhoan taiKhoan) {
        // Không cho xóa tài khoản admin
        if ("Admin".equals(taiKhoan.getVaiTro())) {
            Toast.makeText(this, "Không thể xóa tài khoản Admin", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa tài khoản " + taiKhoan.getTenTaiKhoan() + "?" +
                        ("Student".equals(taiKhoan.getVaiTro()) ? 
                        "\n\n⚠️ Hồ sơ sinh viên và điểm sẽ bị xóa!" : ""))
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Nếu là Student thì xóa cả hồ sơ sinh viên
                    if ("Student".equals(taiKhoan.getVaiTro())) {
                        // Xóa sinh viên (sẽ cascade xóa điểm)
                        db.deleteSinhVien(taiKhoan.getTenTaiKhoan());
                    }
                    
                    // Xóa tài khoản
                    int result = db.deleteTaiKhoan(taiKhoan.getTenTaiKhoan());
                    if (result > 0) {
                        Toast.makeText(this, "Đã xóa tài khoản" + 
                            ("Student".equals(taiKhoan.getVaiTro()) ? " và hồ sơ sinh viên" : ""), 
                            Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

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
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}
