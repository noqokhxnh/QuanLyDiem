package com.example.qldiem.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qldiem.R;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.SinhVien;
import com.example.qldiem.models.TaiKhoan;
import com.example.qldiem.utils.PasswordUtils;
import com.example.qldiem.utils.SessionManager;
import com.example.qldiem.utils.ValidationUtils;

/**
 * Màn hình thông tin cá nhân
 * Student/Lecturer có thể đổi mật khẩu
 */
public class ProfileActivity extends AppCompatActivity {
    
    // Các TextView để hiển thị thông tin chi tiết của người dùng
    private TextView tvUsername, tvFullName, tvRole, tvEmail, tvClass, tvMajor;
    
    // Các Button cho các hành động người dùng có thể thực hiện
    private Button btnChangePassword, btnLogout;
    
    // Quản lý session người dùng (đăng nhập, thông tin đăng nhập tạm thời)
    private SessionManager session;
    
    // Quản lý truy cập và thao tác với cơ sở dữ liệu
    private DatabaseHelper db;

    /**
     * Hàm khởi tạo activity, thiết lập giao diện và ánh xạ các thành phần
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_profile.xml)
     * - Khởi tạo SessionManager và DatabaseHelper
     * - Thiết lập tiêu đề và nút back cho ActionBar
     * - Ánh xạ các view từ layout với các biến trong class
     * - Gán sự kiện click cho các nút chức năng
     * - Tải thông tin người dùng để hiển thị
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity sẽ không được khởi tạo đúng cách
     * - Giao diện sẽ không hiển thị
     * - Không thể tương tác với các thành phần giao diện
     * - Ứng dụng sẽ bị lỗi khi mở activity này
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        session = new SessionManager(this);
        db = new DatabaseHelper(this);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thông tin cá nhân");
        }
        
        // Ánh xạ views
        tvUsername = findViewById(R.id.tvUsername);
        tvFullName = findViewById(R.id.tvFullName);
        tvRole = findViewById(R.id.tvRole);
        tvEmail = findViewById(R.id.tvEmail);
        tvClass = findViewById(R.id.tvClass);
        tvMajor = findViewById(R.id.tvMajor);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
        
        loadUserInfo();
        
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
        btnLogout.setOnClickListener(v -> logout());
    }
    
    /**
     * Hàm tải thông tin người dùng từ database và hiển thị lên giao diện
     *
     * CÔNG DỤNG:
     * - Lấy thông tin tài khoản người dùng từ database
     * - Hiển thị thông tin lên các TextView tương ứng
     * - Phân biệt loại tài khoản (Student/Lecturer/Admin) để hiển thị thông tin phù hợp
     *
     * CÁC BIẾN TRONG HÀM:
     * - username: lưu tên đăng nhập hiện tại của người dùng từ session
     * - taiKhoan: lưu thông tin tài khoản lấy từ database
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (session, db)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng sẽ không thấy được thông tin cá nhân của mình
     * - Giao diện profile sẽ trống không có thông tin
     * - Không thể phân biệt được loại tài khoản để hiển thị thông tin phù hợp
     */
    private void loadUserInfo() {
        String username = session.getUsername();
        
        // Lấy thông tin tài khoản từ database để đảm bảo dữ liệu mới nhất
        TaiKhoan taiKhoan = db.getTaiKhoanByUsername(username);
        if (taiKhoan != null) {
            tvUsername.setText("Tên đăng nhập: " + taiKhoan.getTenTaiKhoan());
            tvFullName.setText("Họ tên: " + taiKhoan.getHoTen());
            tvRole.setText("Vai trò: " + taiKhoan.getVaiTro());
        } else {
            // Fallback nếu không lấy được từ database
            tvUsername.setText("Tên đăng nhập: " + username);
            tvFullName.setText("Họ tên: " + session.getFullName());
            tvRole.setText("Vai trò: " + session.getRole());
        }
        
        // Nếu là Student, hiển thị thêm thông tin lớp/ngành
        if (session.isStudent()) {
            SinhVien sv = db.getSinhVienByMa(username);
            if (sv != null) {
                tvEmail.setVisibility(View.VISIBLE);
                tvClass.setVisibility(View.VISIBLE);
                tvMajor.setVisibility(View.VISIBLE);
                
                tvEmail.setText("Email: " + sv.getEmail());
                tvClass.setText("Lớp: " + sv.getMaLop());
                tvMajor.setText("Chuyên ngành: " + sv.getMaChuyenNganh());
            }
        } else {
            tvEmail.setVisibility(View.GONE);
            tvClass.setVisibility(View.GONE);
            tvMajor.setVisibility(View.GONE);
        }
    }
    
    /**
     * Hàm hiển thị dialog đổi mật khẩu để người dùng thay đổi mật khẩu hiện tại
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog có 3 trường nhập: mật khẩu cũ, mật khẩu mới, xác nhận mật khẩu mới
     * - Xác thực mật khẩu cũ trước khi cho phép đổi
     * - Cập nhật mật khẩu mới vào database sau khi xác thực thành công
     *
     * CÁC BIẾN TRONG HÀM:
     * - view: layout của dialog đổi mật khẩu
     * - edtOldPassword: EditText chứa mật khẩu cũ
     * - edtNewPassword: EditText chứa mật khẩu mới
     * - edtConfirmPassword: EditText chứa xác nhận mật khẩu mới
     * - oldPwd: chuỗi mật khẩu cũ từ EditText
     * - newPwd: chuỗi mật khẩu mới từ EditText
     * - confirmPwd: chuỗi xác nhận mật khẩu từ EditText
     * - tk: đối tượng tài khoản hiện tại
     * - result: kết quả của thao tác cập nhật mật khẩu (số dòng bị ảnh hưởng)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (session, db)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng sẽ không thể thay đổi mật khẩu của mình
     * - Tính năng bảo mật cho tài khoản sẽ bị thiếu
     * - Người dùng phải liên hệ quản trị viên mỗi khi muốn đổi mật khẩu
     */
    private void showChangePasswordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);
        EditText edtOldPassword = view.findViewById(R.id.edtOldPassword);
        EditText edtNewPassword = view.findViewById(R.id.edtNewPassword);
        EditText edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        
        new AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(view)
                .setPositiveButton("Đổi", (dialog, which) -> {
                    String oldPwd = edtOldPassword.getText().toString().trim();
                    String newPwd = edtNewPassword.getText().toString().trim();
                    String confirmPwd = edtConfirmPassword.getText().toString().trim();
                    
                    // Validation
                    if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    if (!ValidationUtils.isValidPassword(newPwd)) {
                        Toast.makeText(this, "Mật khẩu mới phải từ 6-20 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    if (!newPwd.equals(confirmPwd)) {
                        Toast.makeText(this, "Xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    // Kiểm tra mật khẩu cũ
                    TaiKhoan tk = db.getTaiKhoanByUsername(session.getUsername());
                    if (tk == null || !PasswordUtils.verifyPassword(oldPwd, tk.getMatKhau())) {
                        Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    // Đổi mật khẩu
                    tk.setMatKhau(PasswordUtils.hashPassword(newPwd));
                    int result = db.updateTaiKhoan(tk);
                    
                    if (result > 0) {
                        Toast.makeText(this, "✅ Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "❌ Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    
    /**
     * Hàm xử lý đăng xuất người dùng khỏi ứng dụng
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog xác nhận đăng xuất
     * - Gọi phương thức logout từ SessionManager để xóa thông tin đăng nhập
     * - Đóng activity hiện tại để quay về màn hình đăng nhập
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ, chỉ sử dụng đối tượng session của class
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (session)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng không thể đăng xuất khỏi ứng dụng
     * - Thông tin đăng nhập sẽ lưu trữ trên thiết bị mãi mãi
     * - Vi phạm nguyên tắc bảo mật cơ bản của ứng dụng
     */
    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    session.logout();
                    finish();
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
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Nút back/up trên ActionBar sẽ không hoạt động
     * - Người dùng không thể quay lại màn hình trước đó qua nút này
     * - Trải nghiệm người dùng bị ảnh hưởng tiêu cực
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
