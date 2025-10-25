package com.example.qldiem.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qldiem.R;
import com.example.qldiem.adapters.SinhVienAdapter;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.SinhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity quản lý danh sách sinh viên - nơi hiển thị và quản lý tất cả sinh viên trong hệ thống
 *
 * CÔNG DỤNG:
 * - Hiển thị danh sách tất cả sinh viên trong hệ thống dưới dạng RecyclerView
 * - Cho phép tìm kiếm sinh viên theo tên, mã sinh viên hoặc email
 * - Cho phép thêm sinh viên mới thông qua AddEditSinhVienActivity
 * - Cho phép xem chi tiết hoặc chỉnh sửa thông tin sinh viên
 * - Cho phép xóa sinh viên không cần thiết
 * - Tự động tải lại danh sách khi quay lại activity
 *
 * CÁC THÀNH PHẦN CHÍNH:
 * - recyclerView: hiển thị danh sách sinh viên
 * - adapter: adapter để hiển thị sinh viên trong RecyclerView
 * - db: DatabaseHelper để truy vấn cơ sở dữ liệu
 * - sinhVienList: danh sách sinh viên để hiển thị
 * - fabAdd: Floating Action Button để thêm sinh viên mới
 * - searchView: SearchView để tìm kiếm sinh viên
 *
 * NẾU KHÔNG CÓ ACTIVITY NÀY:
 * - Không thể quản lý danh sách sinh viên trong hệ thống
 * - Không thể tìm kiếm sinh viên theo tiêu chí
 * - Không thể thêm, sửa hoặc xóa thông tin sinh viên
 * - Tính năng quản lý sinh viên sẽ không hoạt động
 */
public class SinhVienActivity extends AppCompatActivity implements SinhVienAdapter.OnItemClickListener {

    // RecyclerView để hiển thị danh sách sinh viên
    private RecyclerView recyclerView;
    
    // Adapter để kết nối dữ liệu sinh viên với RecyclerView
    private SinhVienAdapter adapter;
    
    // Quản lý truy cập cơ sở dữ liệu
    private DatabaseHelper db;
    
    // Danh sách sinh viên để hiển thị trong RecyclerView
    private List<SinhVien> sinhVienList;
    
    // Floating Action Button để thêm sinh viên mới
    private FloatingActionButton fabAdd;
    
    // SearchView để tìm kiếm sinh viên
    private SearchView searchView;

    /**
     * Hàm khởi tạo activity quản lý sinh viên, thiết lập giao diện và các thành phần
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_sinh_vien.xml)
     * - Thiết lập tiêu đề và nút back cho ActionBar
     * - Khởi tạo DatabaseHelper để truy vấn cơ sở dữ liệu
     * - Ánh xạ các thành phần giao diện (RecyclerView, FAB)
     * - Thiết lập RecyclerView với adapter
     * - Tải danh sách sinh viên để hiển thị
     * - Gán sự kiện click cho nút thêm sinh viên
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     * - intent: đối tượng Intent để chuyển sang AddEditSinhVienActivity
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity quản lý sinh viên sẽ không được khởi tạo đúng cách
     * - Giao diện quản lý sinh viên sẽ không hiển thị
     * - Không thể tương tác với các thành phần giao diện
     * - Ứng dụng sẽ bị lỗi khi mở activity quản lý sinh viên
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien);

        // Hiển thị nút back
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý sinh viên");
        }

        // Khởi tạo database
        db = new DatabaseHelper(this);

        // Ánh xạ views
        recyclerView = findViewById(R.id.recyclerViewSinhVien);
        fabAdd = findViewById(R.id.fabAddSinhVien);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sinhVienList = new ArrayList<>();
        adapter = new SinhVienAdapter(this, sinhVienList, this);
        recyclerView.setAdapter(adapter);

        // Load dữ liệu
        loadData();

        // Xử lý thêm sinh viên mới
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SinhVienActivity.this, AddEditSinhVienActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Hàm tải danh sách tất cả sinh viên từ cơ sở dữ liệu và cập nhật cho RecyclerView
     *
     * CÔNG DỤNG:
     * - Làm mới danh sách sinh viên để hiển thị
     * - Gọi phương thức database để lấy tất cả sinh viên
     * - Cập nhật adapter để hiển thị dữ liệu mới trên giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (db, sinhVienList, adapter)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Danh sách sinh viên sẽ không được tải và hiển thị
     * - Người dùng không thấy bất kỳ dữ liệu sinh viên nào
     * - Giao diện sẽ trống không có thông tin
     */
    private void loadData() {
        sinhVienList.clear();
        sinhVienList.addAll(db.getAllSinhVien());
        adapter.notifyDataSetChanged();
    }

    /**
     * Hàm được gọi khi activity trở lại trạng thái hoạt động, dùng để tải lại dữ liệu
     *
     * CÔNG DỤNG:
     * - Tự động tải lại danh sách sinh viên khi người dùng quay lại activity
     * - Đảm bảo dữ liệu luôn cập nhật sau khi thêm, sửa hoặc xóa sinh viên
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Danh sách sinh viên sẽ không được cập nhật tự động khi quay lại activity
     * - Người dùng có thể thấy dữ liệu cũ sau khi thực hiện thay đổi
     * - Trải nghiệm người dùng bị ảnh hưởng tiêu cực
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * Hàm tạo menu cho ActionBar, bao gồm chức năng tìm kiếm sinh viên
     *
     * CÔNG DỤNG:
     * - Inflate menu_search.xml để hiển thị chức năng tìm kiếm trong ActionBar
     * - Khởi tạo SearchView và gán sự kiện lắng nghe thay đổi văn bản
     * - Gọi hàm tìm kiếm khi người dùng nhập văn bản vào SearchView
     * - Trả về true để hiển thị menu trên giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - searchItem: mục menu tìm kiếm từ menu
     * - query: chuỗi truy vấn khi người dùng nhấn Enter (không sử dụng)
     * - newText: chuỗi văn bản mới khi người dùng nhập vào SearchView
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Menu menu: đối tượng menu để thêm các mục vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - boolean: true để hiển thị menu trên giao diện
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Menu tìm kiếm sẽ không được hiển thị trên ActionBar
     * - Người dùng không thể tìm kiếm sinh viên theo tiêu chí
     * - Tính năng tìm kiếm sẽ không hoạt động
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSinhVien(newText);
                return true;
            }
        });
        
        return true;
    }

    /**
     * Hàm tìm kiếm sinh viên theo từ khóa và cập nhật danh sách hiển thị
     *
     * CÔNG DỤNG:
     * - Tìm kiếm sinh viên trong cơ sở dữ liệu theo từ khóa
     * - Nếu từ khóa rỗng, tải lại tất cả sinh viên
     * - Nếu có từ khóa, tìm kiếm sinh viên phù hợp và hiển thị kết quả
     * - Cập nhật adapter để hiển thị kết quả tìm kiếm trên giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - keyword: từ khóa tìm kiếm do người dùng nhập vào
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String keyword: từ khóa để tìm kiếm sinh viên (tên, mã sinh viên, email)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tìm kiếm sinh viên theo tiêu chí
     * - Người dùng phải cuộn qua toàn bộ danh sách để tìm sinh viên cần thiết
     * - Tính năng tìm kiếm sẽ không hoạt động
     */
    private void searchSinhVien(String keyword) {
        if (keyword.isEmpty()) {
            loadData();
        } else {
            sinhVienList.clear();
            sinhVienList.addAll(db.searchSinhVien(keyword));
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Hàm xử lý sự kiện khi người dùng nhấn vào một sinh viên trong danh sách
     *
     * CÔNG DỤNG:
     * - Chuyển sang AddEditSinhVienActivity để xem chi tiết hoặc chỉnh sửa thông tin sinh viên
     * - Truyền mã sinh viên qua Intent để AddEditSinhVienActivity biết sinh viên nào cần xem/sửa
     *
     * CÁC BIẾN TRONG HÀM:
     * - intent: đối tượng Intent để chuyển sang AddEditSinhVienActivity
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - SinhVien sinhVien: đối tượng sinh viên được người dùng chọn
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xem chi tiết hoặc chỉnh sửa thông tin sinh viên
     * - Người dùng không thể truy cập chức năng xem/sửa sinh viên
     * - Tính năng quản lý sinh viên sẽ không đầy đủ
     */
    @Override
    public void onItemClick(SinhVien sinhVien) {
        // Xem chi tiết / chỉnh sửa
        Intent intent = new Intent(this, AddEditSinhVienActivity.class);
        intent.putExtra("maSv", sinhVien.getMaSv());
        startActivity(intent);
    }

    /**
     * Hàm xử lý sự kiện khi người dùng nhấn nút xóa sinh viên
     *
     * CÔNG DỤNG:
     * - Hiển thị dialog xác nhận xóa sinh viên
     * - Gọi phương thức database để xóa sinh viên khỏi cơ sở dữ liệu
     * - Hiển thị thông báo kết quả xóa
     * - Tải lại danh sách để cập nhật giao diện nếu xóa thành công
     *
     * CÁC BIẾN TRONG HÀM:
     * - result: kết quả từ thao tác xóa sinh viên khỏi database (số bản ghi bị ảnh hưởng)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - SinhVien sinhVien: đối tượng sinh viên cần xóa
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xóa sinh viên không cần thiết khỏi hệ thống
     * - Admin không thể dọn dẹp hồ sơ sinh viên cũ hoặc không hợp lệ
     * - Tính năng quản lý sinh viên sẽ không đầy đủ
     */
    @Override
    public void onDeleteClick(SinhVien sinhVien) {
        // Xác nhận xóa
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sinh viên " + sinhVien.getTenSV() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = db.deleteSinhVien(sinhVien.getMaSv());
                    if (result > 0) {
                        Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show();
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
        if (db != null) {
            db.close();
        }
    }
}
