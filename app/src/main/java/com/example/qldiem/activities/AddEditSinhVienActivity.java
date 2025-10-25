package com.example.qldiem.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qldiem.R;
import com.example.qldiem.database.DatabaseHelper;
import com.example.qldiem.models.ChuyenNganh;
import com.example.qldiem.models.Lop;
import com.example.qldiem.models.SinhVien;
import com.example.qldiem.utils.ImageUtils;
import com.example.qldiem.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity thêm/sửa sinh viên - nơi thêm sinh viên mới hoặc chỉnh sửa thông tin sinh viên hiện có
 *
 * CÔNG DỤNG:
 * - Cho phép thêm sinh viên mới với đầy đủ thông tin: mã SV, tên, email, lớp, chuyên ngành, ảnh đại diện
 * - Cho phép chỉnh sửa thông tin sinh viên hiện có
 * - Xác thực thông tin đầu vào để đảm bảo dữ liệu hợp lệ
 * - Hỗ trợ chọn và tải ảnh đại diện cho sinh viên
 * - Tự động phát hiện chế độ thêm mới hoặc chỉnh sửa dựa trên Intent
 *
 * CÁC THÀNH PHẦN CHÍNH:
 * - edtMaSV, edtTenSV, edtEmail: EditText để nhập thông tin cơ bản của sinh viên
 * - spinnerLop, spinnerChuyenNganh: Spinner để chọn lớp và chuyên ngành
 * - imgAvatar: ImageView để hiển thị ảnh đại diện của sinh viên
 * - btnSave, btnChooseImage: Button để lưu thông tin và chọn ảnh
 * - db: DatabaseHelper để truy vấn cơ sở dữ liệu
 * - maSvEdit: chuỗi lưu mã sinh viên khi ở chế độ chỉnh sửa
 * - base64Image: chuỗi lưu ảnh đại diện dưới dạng base64
 * - isEditMode: boolean để xác định đang ở chế độ thêm mới hay chỉnh sửa
 * - lopList, chuyenNganhList: danh sách lớp và chuyên ngành để đổ vào Spinner
 *
 * NẾU KHÔNG CÓ ACTIVITY NÀY:
 * - Không thể thêm sinh viên mới vào hệ thống
 * - Không thể chỉnh sửa thông tin sinh viên hiện có
 * - Tính năng quản lý sinh viên sẽ không đầy đủ
 * - Không thể lưu trữ ảnh đại diện cho sinh viên
 */
public class AddEditSinhVienActivity extends AppCompatActivity {

    // Mã yêu cầu để chọn ảnh từ thư viện
    private static final int PICK_IMAGE_REQUEST = 1;

    // EditText để nhập mã sinh viên
    private EditText edtMaSV;
    
    // EditText để nhập tên sinh viên
    private EditText edtTenSV;
    
    // EditText để nhập email sinh viên
    private EditText edtEmail;
    
    // Spinner để chọn lớp học
    private Spinner spinnerLop;
    
    // Spinner để chọn chuyên ngành
    private Spinner spinnerChuyenNganh;
    
    // ImageView để hiển thị ảnh đại diện
    private ImageView imgAvatar;
    
    // Button để lưu thông tin sinh viên
    private Button btnSave;
    
    // Button để chọn ảnh từ thư viện
    private Button btnChooseImage;
    
    // Quản lý truy cập cơ sở dữ liệu
    private DatabaseHelper db;
    
    // Mã sinh viên đang chỉnh sửa (null nếu đang thêm mới)
    private String maSvEdit = null;
    
    // Ảnh đại diện dưới dạng base64
    private String base64Image = null;
    
    // Cờ để xác định đang ở chế độ chỉnh sửa hay thêm mới
    private boolean isEditMode = false;

    // Danh sách lớp học để đổ vào spinner
    private List<Lop> lopList;
    
    // Danh sách chuyên ngành để đổ vào spinner
    private List<ChuyenNganh> chuyenNganhList;

    /**
     * Hàm khởi tạo activity thêm/sửa sinh viên, thiết lập giao diện và xử lý sự kiện
     *
     * CÔNG DỤNG:
     * - Thiết lập layout cho activity (activity_add_edit_sinh_vien.xml)
     * - Khởi tạo DatabaseHelper để truy vấn cơ sở dữ liệu
     * - Ánh xạ các thành phần giao diện (EditText, Spinner, ImageView, Button)
     * - Tải dữ liệu cho các Spinner (lớp học, chuyên ngành)
     * - Kiểm tra Intent để xác định chế độ thêm mới hay chỉnh sửa
     * - Thiết lập tiêu đề phù hợp với chế độ (Thêm mới hoặc Chỉnh sửa)
     * - Hiển thị nút back trên ActionBar
     * - Gán sự kiện click cho nút chọn ảnh và nút lưu
     *
     * CÁC BIẾN TRONG HÀM:
     * - savedInstanceState: bundle chứa dữ liệu lưu trước đó khi activity bị hủy (nếu có)
     * - intent: Intent nhận được khi mở activity (chứa mã SV nếu ở chế độ chỉnh sửa)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - savedInstanceState: chứa trạng thái trước đó của activity, giúp khôi phục dữ liệu nếu activity bị hủy do hệ thống
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Activity thêm/sửa sinh viên sẽ không được khởi tạo đúng cách
     * - Giao diện thêm/sửa sinh viên sẽ không hiển thị
     * - Không thể tương tác với các thành phần giao diện
     * - Ứng dụng sẽ bị lỗi khi mở activity thêm/sửa sinh viên
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_sinh_vien);

        // Khởi tạo database
        db = new DatabaseHelper(this);

        // Ánh xạ views
        edtMaSV = findViewById(R.id.edtMaSV);
        edtTenSV = findViewById(R.id.edtTenSV);
        edtEmail = findViewById(R.id.edtEmail);
        spinnerLop = findViewById(R.id.spinnerLop);
        spinnerChuyenNganh = findViewById(R.id.spinnerChuyenNganh);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnSave = findViewById(R.id.btnSave);
        btnChooseImage = findViewById(R.id.btnChooseImage);

        // Load data cho spinners
        loadSpinnerData();

        // Kiểm tra chế độ edit
        Intent intent = getIntent();
        if (intent.hasExtra("maSv")) {
            isEditMode = true;
            maSvEdit = intent.getStringExtra("maSv");
            loadSinhVienData();
            edtMaSV.setEnabled(false);
            
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Chỉnh sửa sinh viên");
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Thêm sinh viên mới");
            }
        }

        // Hiển thị nút back
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Chọn ảnh
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSinhVien();
            }
        });
    }

    /**
     * Hàm tải dữ liệu cho các Spinner (lớp học và chuyên ngành) từ cơ sở dữ liệu
     *
     * CÔNG DỤNG:
     * - Lấy danh sách tất cả lớp học từ cơ sở dữ liệu và đổ vào spinnerLop
     * - Lấy danh sách tất cả chuyên ngành từ cơ sở dữ liệu và đổ vào spinnerChuyenNganh
     * - Thiết lập ArrayAdapter với layout phù hợp cho hiển thị và dropdown
     *
     * CÁC BIẾN TRONG HÀM:
     * - lopList: danh sách lớp học lấy từ database
     * - lopAdapter: ArrayAdapter để hiển thị lớp học trong spinnerLop
     * - chuyenNganhList: danh sách chuyên ngành lấy từ database
     * - cnAdapter: ArrayAdapter để hiển thị chuyên ngành trong spinnerChuyenNganh
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (db, spinnerLop, spinnerChuyenNganh)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Các Spinner lớp học và chuyên ngành sẽ trống không có dữ liệu
     * - Người dùng không thể chọn lớp học và chuyên ngành cho sinh viên
     * - Không thể lưu thông tin sinh viên vì thiếu thông tin lớp và chuyên ngành
     */
    private void loadSpinnerData() {
        // Load lớp học
        lopList = db.getAllLop();
        ArrayAdapter<Lop> lopAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lopList);
        lopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(lopAdapter);

        // Load chuyên ngành
        chuyenNganhList = db.getAllChuyenNganh();
        ArrayAdapter<ChuyenNganh> cnAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, chuyenNganhList);
        cnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChuyenNganh.setAdapter(cnAdapter);
    }

    /**
     * Hàm tải thông tin sinh viên cần chỉnh sửa từ cơ sở dữ liệu và hiển thị lên giao diện
     *
     * CÔNG DỤNG:
     * - Lấy thông tin sinh viên từ cơ sở dữ liệu theo mã sinh viên
     * - Hiển thị thông tin lên các trường nhập liệu (mã SV, tên, email)
     * - Hiển thị ảnh đại diện nếu có
     * - Chọn lựa chọn phù hợp trong các Spinner (lớp, chuyên ngành)
     *
     * CÁC BIẾN TRONG HÀM:
     * - sv: đối tượng SinhVien lấy từ database theo mã sinh viên
     * - i: biến đếm trong vòng lặp để tìm vị trí lớp/chuyên ngành trong danh sách
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (maSvEdit, db, edtMaSV, v.v.)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể hiển thị thông tin sinh viên cần chỉnh sửa
     * - Người dùng sẽ thấy form trống khi mở ở chế độ chỉnh sửa
     * - Không thể cập nhật thông tin sinh viên vì thiếu dữ liệu gốc
     */
    private void loadSinhVienData() {
        SinhVien sv = db.getSinhVienByMa(maSvEdit);
        if (sv != null) {
            edtMaSV.setText(sv.getMaSv());
            edtTenSV.setText(sv.getTenSV());
            edtEmail.setText(sv.getEmail());
            base64Image = sv.getHinh();
            
            // Load ảnh
            if (base64Image != null && !base64Image.isEmpty()) {
                imgAvatar.setImageBitmap(ImageUtils.base64ToBitmap(base64Image));
            }

            // Set spinner selections
            for (int i = 0; i < lopList.size(); i++) {
                if (lopList.get(i).getMaLop().equals(sv.getMaLop())) {
                    spinnerLop.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < chuyenNganhList.size(); i++) {
                if (chuyenNganhList.get(i).getMaChuyenNganh().equals(sv.getMaChuyenNganh())) {
                    spinnerChuyenNganh.setSelection(i);
                    break;
                }
            }
        }
    }

    /**
     * Hàm mở trình chọn ảnh để người dùng chọn ảnh đại diện cho sinh viên
     *
     * CÔNG DỤNG:
     * - Tạo Intent để mở ứng dụng chọn ảnh (Gallery, Photos, v.v.)
     * - Chỉ định loại nội dung là ảnh từ thư viện ngoài
     * - Gọi startActivityForResult để nhận kết quả sau khi người dùng chọn ảnh
     *
     * CÁC BIẾN TRONG HÀM:
     * - intent: đối tượng Intent để mở trình chọn ảnh
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng hằng số PICK_IMAGE_REQUEST
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Người dùng không thể chọn ảnh đại diện cho sinh viên
     * - Tính năng chọn ảnh sẽ không hoạt động
     * - Sinh viên sẽ không có ảnh đại diện
     */
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Hàm xử lý kết quả trả về từ các activity con (trình chọn ảnh trong trường hợp này)
     *
     * CÔNG DỤNG:
     * - Nhận kết quả sau khi người dùng chọn ảnh từ thư viện
     * - Kiểm tra mã yêu cầu và mã kết quả để xác định nguồn trả về
     * - Lấy URI của ảnh được chọn
     * - Hiển thị ảnh trên ImageView imgAvatar
     * - Chuyển đổi ảnh sang dạng base64 để lưu vào cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - imageUri: URI của ảnh được chọn từ thư viện
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int requestCode: mã yêu cầu được gửi khi gọi startActivityForResult
     * - int resultCode: mã kết quả trả về (RESULT_OK nếu thành công)
     * - Intent data: Intent chứa dữ liệu trả về (bao gồm URI của ảnh)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể nhận kết quả từ trình chọn ảnh
     * - Người dùng chọn ảnh nhưng ảnh không được hiển thị hoặc lưu trữ
     * - Tính năng chọn ảnh sẽ không hoạt động hoàn chỉnh
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                imgAvatar.setImageURI(imageUri);
                base64Image = ImageUtils.uriToBase64(this, imageUri);
            }
        }
    }

    /**
     * Hàm lưu thông tin sinh viên (thêm mới hoặc cập nhật) vào cơ sở dữ liệu
     *
     * CÔNG DỤNG:
     * - Lấy thông tin từ các trường nhập liệu (mã SV, tên, email)
     * - Lấy lựa chọn từ các Spinner (lớp, chuyên ngành)
     * - Thực hiện validation để đảm bảo thông tin hợp lệ
     * - Tạo đối tượng SinhVien với thông tin đã nhập
     * - Gọi phương thức database phù hợp (thêm mới hoặc cập nhật)
     * - Hiển thị thông báo kết quả và đóng activity nếu thành công
     *
     * CÁC BIẾN TRONG HÀM:
     * - maSv: chuỗi mã sinh viên lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - tenSV: chuỗi tên sinh viên lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - email: chuỗi email lấy từ EditText (đã loại bỏ khoảng trắng dư)
     * - selectedLop: đối tượng Lop được chọn từ spinnerLop
     * - selectedCN: đối tượng ChuyenNganh được chọn từ spinnerChuyenNganh
     * - sinhVien: đối tượng SinhVien mới tạo với thông tin đã nhập
     * - result: kết quả từ thao tác thêm/cập nhật vào database (số bản ghi bị ảnh hưởng)
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào, vì hàm sử dụng các thành viên của class (edtMaSV, edtTenSV, v.v.)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lưu thông tin sinh viên vào cơ sở dữ liệu
     * - Người dùng nhập thông tin nhưng không được lưu trữ
     * - Tính năng thêm/sửa sinh viên sẽ không hoạt động
     * - Dữ liệu sinh viên sẽ không được cập nhật trong hệ thống
     */
    private void saveSinhVien() {
        String maSv = edtMaSV.getText().toString().trim();
        String tenSV = edtTenSV.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        
        Lop selectedLop = (Lop) spinnerLop.getSelectedItem();
        ChuyenNganh selectedCN = (ChuyenNganh) spinnerChuyenNganh.getSelectedItem();

        // Validation
        if (!ValidationUtils.isValidMaSV(maSv)) {
            edtMaSV.setError("Mã SV không hợp lệ (3-20 ký tự, chữ và số)");
            edtMaSV.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidName(tenSV)) {
            edtTenSV.setError("Tên sinh viên không hợp lệ");
            edtTenSV.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (selectedLop == null || selectedCN == null) {
            Toast.makeText(this, "Vui lòng chọn lớp và chuyên ngành", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng sinh viên
        SinhVien sinhVien = new SinhVien(maSv, tenSV, email, base64Image,
                selectedLop.getMaLop(), selectedCN.getMaChuyenNganh());

        long result;
        if (isEditMode) {
            result = db.updateSinhVien(sinhVien);
            if (result > 0) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        } else {
            result = db.addSinhVien(sinhVien);
            if (result > 0) {
                Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại. Mã SV có thể đã tồn tại.", Toast.LENGTH_SHORT).show();
            }
        }
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
