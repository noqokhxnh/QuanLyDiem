package com.example.qldiem.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qldiem.R;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.TaiKhoan;
import com.example.qldiem.utils.SecurityUtils;
import com.example.qldiem.utils.ValidationUtils;

/**
 * Activity đăng ký tài khoản - nơi người dùng tạo tài khoản mới trong hệ thống
 *
 * CÔNG DỤNG:
 * - Cung cấp giao diện cho người dùng nhập thông tin đăng ký
 * - Xác thực thông tin đăng ký (tên đăng nhập, mật khẩu, họ tên)
 * - Kiểm tra tên đăng nhập đã tồn tại chưa
 * - Tạo tài khoản mới trong cơ sở dữ liệu
 * - Hạn chế vai trò chỉ có thể là Student hoặc Lecturer
 *
 * CÁC THÀNH PHẦN CHÍNH:
 * - edtUsername: EditText để nhập tên đăng nhập
 * - edtPassword: EditText để nhập mật khẩu
 * - edtConfirmPassword: EditText để xác nhận mật khẩu
 * - edtFullName: EditText để nhập họ tên đầy đủ
 * - spinnerRole: Spinner để chọn vai trò (Student/Lecturer)
 * - btnRegister: Button để thực hiện đăng ký
 * - db: DatabaseHelper để truy vấn cơ sở dữ liệu
 *
 * NẾU KHÔNG CÓ ACTIVITY NÀY:
 * - Người dùng không thể tạo tài khoản mới
 * - Không thể mở rộng người dùng cho hệ thống
 * - Tính năng đăng ký tài khoản sẽ không hoạt động
 */
public class RegisterActivity extends AppCompatActivity {

    // EditText để nhập tên đăng nhập
    private EditText edtUsername;
    
    // EditText để nhập mật khẩu
    private EditText edtPassword;
    
    // EditText để xác nhận mật khẩu
    private EditText edtConfirmPassword;
    
    // EditText để nhập họ tên đầy đủ
    private EditText edtFullName;
    
    // Spinner để chọn vai trò (Student/Lecturer)
    private Spinner spinnerRole;
    
    // Button để thực hiện đăng ký
    private Button btnRegister;
    
    // Quản lý truy cập cơ sở dữ liệu
    private DatabaseHelper db;

    /**
     * Hàm khởi tạo activity đăng ký, thiết lập giao diện và xử lý sự kiện
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_register.xml)
     * - Khởi tạo DatabaseHelper để truy vấn cơ sở dữ liệu
     * - Ánh xạ các thành phần giao diện (EditText, Spinner, Button)
     * - Thiết lập Spinner cho vai trò với các tùy chọn Student và Lecturer
     * - Gắn sự kiện click cho nút đăng ký
     * - Thiết lập nút back trên ActionBar
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     * - roles: mảng các vai trò có thể chọn (Student, Lecturer)
     * - adapter: ArrayAdapter để hiển thị các vai trò trong Spinner
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity đăng ký sẽ không được khởi tạo đúng cách
     * - Giao diện đăng ký sẽ không hiển thị
     * - Không thể tương tác với các thành phần giao diện
     * - Ứng dụng sẽ bị lỗi khi mở activity đăng ký
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo database
        db = new DatabaseHelper(this);

        // Ánh xạ views
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtFullName = findViewById(R.id.edtFullName);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnRegister = findViewById(R.id.btnRegister);

        // Setup spinner vai trò
        String[] roles = {"Student", "Lecturer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // Xử lý sự kiện đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Nút quay lại
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Hàm xử lý quá trình đăng ký tài khoản mới
     *
     * CÔNG DỤNG:
     * - Lấy thông tin đăng ký từ các trường nhập liệu
     * - Thực hiện validation để đảm bảo thông tin hợp lệ
     * - Kiểm tra tên đăng nhập đã tồn tại trong hệ thống chưa
     * - Tạo tài khoản mới trong cơ sở dữ liệu nếu tất cả validation đều pass
     * - Hiển thị thông báo kết quả đăng ký và quay lại màn hình trước
     *
     * CÁC BIẾN TRONG HÀM:
     * - username: tên đăng nhập lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - password: mật khẩu lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - confirmPassword: xác nhận mật khẩu lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - fullName: họ tên đầy đủ lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - role: vai trò được chọn từ Spinner
     * - taiKhoan: đối tượng tài khoản để lưu thông tin đăng ký
     * - result: kết quả từ thao tác thêm tài khoản vào database (số bản ghi bị ảnh hưởng)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (edtUsername, edtPassword, v.v.)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng không thể thực hiện đăng ký tài khoản
     * - Không có cơ chế thêm tài khoản mới vào hệ thống
     * - Tính năng đăng ký tài khoản sẽ không hoạt động
     */
    private void register() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        // Validation
        if (!ValidationUtils.isValidUsername(username)) {
            edtUsername.setError("Tên đăng nhập phải từ 3-20 ký tự (chữ, số, _)");
            edtUsername.requestFocus();
            return;
        }

        if (!SecurityUtils.isPasswordStrong(password)) {
            edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edtPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            edtConfirmPassword.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidName(fullName)) {
            edtFullName.setError("Họ tên không hợp lệ");
            edtFullName.requestFocus();
            return;
        }

        // Kiểm tra tài khoản đã tồn tại
        if (db.checkTaiKhoanExists(username)) {
            edtUsername.setError("Tên đăng nhập đã tồn tại");
            edtUsername.requestFocus();
            return;
        }

        // Tạo tài khoản mới
        TaiKhoan taiKhoan = new TaiKhoan(username, password, role, fullName);
        long result = db.addTaiKhoan(taiKhoan);

        if (result > 0) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hàm xử lý sự kiện khi người dùng nhấn nút back/up trên ActionBar
     *
     * CÔNG DỤNG:
     * - Kích hoạt hành động quay lại màn hình trước đó (thường là LoginActivity)
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
        if (db != null) {
            db.close();
        }
    }
}
