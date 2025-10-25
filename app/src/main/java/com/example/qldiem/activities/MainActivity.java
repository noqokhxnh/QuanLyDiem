package com.example.qldiem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.qldiem.R;
import com.example.qldiem.utils.SessionManager;

/**
 * Activity màn hình chính với dashboard - trung tâm điều hướng cho toàn bộ ứng dụng
 *
 * CÔNG DỤNG:
 * - Là màn hình chính sau khi người dùng đăng nhập thành công
 * - Hiển thị dashboard với các chức năng chính dưới dạng CardView
 * - Phân quyền truy cập theo vai trò người dùng (Admin, Lecturer, Student)
 * - Cung cấp menu với các tùy chọn như xem thông tin cá nhân và đăng xuất
 * - Là điểm khởi đầu cho tất cả các tính năng trong hệ thống
 *
 * CÁC THÀNH PHẦN CHÍNH:
 * - session: SessionManager để kiểm tra trạng thái đăng nhập và vai trò
 * - cardSinhVien: CardView để vào chức năng quản lý sinh viên
 * - cardLop: CardView để vào chức năng quản lý lớp học
 * - cardChuyenNganh: CardView để vào chức năng quản lý chuyên ngành
 * - cardMonHoc: CardView để vào chức năng quản lý môn học
 * - cardDiem: CardView để vào chức năng quản lý điểm
 * - cardEvent: CardView để vào chức năng quản lý sự kiện
 * - cardTaiKhoan: CardView để vào chức năng quản lý tài khoản (chỉ Admin)
 *
 * NẾU KHÔNG CÓ ACTIVITY NÀY:
 * - Người dùng không có màn hình trung tâm để điều hướng
 * - Không có nơi để truy cập các chức năng chính của ứng dụng
 * - Ứng dụng thiếu giao diện trung tâm điều hướng
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Quản lý phiên đăng nhập của người dùng
    private SessionManager session;
    
    // CardView cho chức năng quản lý sinh viên
    private CardView cardSinhVien;
    
    // CardView cho chức năng quản lý lớp học
    private CardView cardLop;
    
    // CardView cho chức năng quản lý chuyên ngành
    private CardView cardChuyenNganh;
    
    // CardView cho chức năng quản lý môn học
    private CardView cardMonHoc;
    
    // CardView cho chức năng quản lý điểm
    private CardView cardDiem;
    
    // CardView cho chức năng quản lý sự kiện
    private CardView cardEvent;
    
    // CardView cho chức năng quản lý tài khoản
    private CardView cardTaiKhoan;

    /**
     * Hàm khởi tạo activity màn hình chính, thiết lập giao diện và kiểm tra đăng nhập
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_main.xml)
     * - Khởi tạo SessionManager để kiểm tra trạng thái đăng nhập
     * - Kiểm tra nếu người dùng chưa đăng nhập thì chuyển về màn hình đăng nhập
     * - Thiết lập tiêu đề ActionBar với tên và vai trò người dùng
     * - Ánh xạ các CardView từ layout
     * - Gán sự kiện click cho tất cả CardView
     * - Thiết lập quyền truy cập theo vai trò người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity màn hình chính sẽ không được khởi tạo đúng cách
     * - Giao diện chính sẽ không hiển thị
     * - Không thể điều hướng đến các chức năng khác
     * - Không có kiểm tra đăng nhập khi mở activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo session
        session = new SessionManager(this);

        // Kiểm tra đăng nhập
        if (!session.isLoggedIn()) {
            navigateToLogin();
            return;
        }
        
        // Set title với tên người dùng
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(session.getFullName() + " - " + session.getRole());
        }

        // Ánh xạ views
        cardSinhVien = findViewById(R.id.cardSinhVien);
        cardLop = findViewById(R.id.cardLop);
        cardChuyenNganh = findViewById(R.id.cardChuyenNganh);
        cardMonHoc = findViewById(R.id.cardMonHoc);
        cardDiem = findViewById(R.id.cardDiem);
        cardEvent = findViewById(R.id.cardEvent);
        cardTaiKhoan = findViewById(R.id.cardTaiKhoan);

        // Set click listeners
        cardSinhVien.setOnClickListener(this);
        cardLop.setOnClickListener(this);
        cardChuyenNganh.setOnClickListener(this);
        cardMonHoc.setOnClickListener(this);
        cardDiem.setOnClickListener(this);
        cardEvent.setOnClickListener(this);
        cardTaiKhoan.setOnClickListener(this);

        // Ẩn các chức năng theo quyền
        setupPermissions();
    }

    /**
     * Hàm thiết lập quyền truy cập giao diện theo vai trò người dùng
     *
     * CÔNG DỤNG:
     * - Ẩn các chức năng không phù hợp với vai trò người dùng hiện tại
     * - Sinh viên chỉ được xem điểm và sự kiện
     * - Giảng viên không được quản lý lớp, chuyên ngành và tài khoản
     * - Quản trị viên (Admin) có quyền truy cập tất cả chức năng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (session, card views)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Tất cả người dùng đều thấy tất cả chức năng bất kể vai trò
     * - Vi phạm nguyên tắc phân quyền trong hệ thống
     * - Người dùng có thể truy cập các chức năng không được phép
     */
    private void setupPermissions() {
        if (session.isStudent()) {
            // Sinh viên chỉ xem được điểm và sự kiện
            cardLop.setVisibility(View.GONE);
            cardChuyenNganh.setVisibility(View.GONE);
            cardMonHoc.setVisibility(View.GONE);
            cardSinhVien.setVisibility(View.GONE);
            cardTaiKhoan.setVisibility(View.GONE);
        } else if (session.isLecturer()) {
            // Giảng viên không quản lý lớp, chuyên ngành và tài khoản
            cardLop.setVisibility(View.GONE);
            cardChuyenNganh.setVisibility(View.GONE);
            cardTaiKhoan.setVisibility(View.GONE);
        }
        // Admin có đầy đủ quyền, bao gồm quản lý tài khoản
    }

    /**
     * Hàm xử lý sự kiện click cho các CardView trên dashboard
     *
     * CÔNG DỤNG:
     * - Điều hướng người dùng đến activity tương ứng khi click vào CardView
     * - Mỗi CardView đại diện cho một chức năng chính trong hệ thống
     * - Tạo Intent để mở activity tương ứng với từng chức năng
     *
     * CÁC BIẾN TRONG HÀM:
     * - id: ID của view được click (được lấy từ v.getId())
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - View v: view được click (một trong các CardView)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Các CardView sẽ không có chức năng khi click
     * - Người dùng không thể điều hướng đến các chức năng khác
     * - Dashboard sẽ không hoạt động như một trung tâm điều hướng
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        
        if (id == R.id.cardSinhVien) {
            startActivity(new Intent(this, SinhVienActivity.class));
        } else if (id == R.id.cardLop) {
            startActivity(new Intent(this, LopActivity.class));
        } else if (id == R.id.cardChuyenNganh) {
            startActivity(new Intent(this, ChuyenNganhActivity.class));
        } else if (id == R.id.cardMonHoc) {
            startActivity(new Intent(this, MonHocActivity.class));
        } else if (id == R.id.cardDiem) {
            startActivity(new Intent(this, DiemActivity.class));
        } else if (id == R.id.cardEvent) {
            startActivity(new Intent(this, EventActivity.class));
        } else if (id == R.id.cardTaiKhoan) {
            startActivity(new Intent(this, TaiKhoanActivity.class));
        }
    }

    /**
     * Hàm tạo menu cho ActionBar
     *
     * CÔNG DỤNG:
     * - Inflate menu_main.xml để hiển thị các tùy chọn trong ActionBar
     * - Thêm các mục menu như thông tin cá nhân và đăng xuất
     * - Trả về true để hiển thị menu trên giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Menu menu: đối tượng menu để thêm các mục vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - boolean: true để hiển thị menu trên giao diện
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Menu trên ActionBar sẽ không được hiển thị
     * - Người dùng không thể truy cập các tùy chọn như xem thông tin cá nhân và đăng xuất
     * - Giao diện thiếu các tùy chọn quan trọng
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Hàm xử lý sự kiện khi người dùng chọn một mục trong menu
     *
     * CÔNG DỤNG:
     * - Xử lý các mục được chọn trong menu ActionBar
     * - Điều hướng đến ProfileActivity khi chọn mục thông tin cá nhân
     * - Gọi hàm đăng xuất khi chọn mục đăng xuất
     * - Trả về super.onOptionsItemSelected(item) cho các mục khác
     *
     * CÁC BIẾN TRONG HÀM:
     * - id: ID của mục menu được chọn
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - MenuItem item: mục menu được người dùng chọn
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - boolean: true nếu xử lý thành công, false nếu sử dụng hành vi mặc định
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Các mục trong menu sẽ không có chức năng khi được chọn
     * - Tính năng xem thông tin cá nhân và đăng xuất sẽ không hoạt động
     * - Người dùng không thể sử dụng các tùy chọn trong menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_profile) {
            // Điều hướng đến activity thông tin cá nhân để người dùng xem và quản lý thông tin của mình
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    /**
     * Hàm xử lý đăng xuất người dùng khỏi ứng dụng
     *
     * CÔNG DỤNG:
     * - Gọi phương thức logout từ SessionManager để xóa thông tin đăng nhập
     * - Hiển thị thông báo đăng xuất thành công
     * - Điều hướng người dùng đến màn hình đăng nhập
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
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
        session.logout();
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }

    /**
     * Hàm điều hướng người dùng từ màn hình chính sang màn hình đăng nhập
     *
     * CÔNG DỤNG:
     * - Tạo Intent để chuyển sang LoginActivity
     * - Đặt cờ để xóa toàn bộ activity trong stack và bắt đầu LoginActivity như là root
     * - Kết thúc MainActivity để người dùng không thể quay lại màn hình chính khi nhấn nút back
     *
     * CÁC BIẾN TRONG HÀM:
     * - intent: đối tượng Intent để điều hướng giữa các activity
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng context của class hiện tại
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể chuyển từ màn hình chính sang màn hình đăng nhập
     * - Người dùng sẽ ở lại màn hình chính sau khi đăng xuất
     * - Trải nghiệm người dùng sẽ bị gián đoạn
     */
    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
