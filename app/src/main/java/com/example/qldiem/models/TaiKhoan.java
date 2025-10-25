package com.example.qldiem.models;

/**
 * Model class cho bảng taiKhoan (Tài khoản người dùng) - đại diện cho thông tin tài khoản của người dùng
 *
 * CÔNG DỤNG:
 * - Chứa thông tin chi tiết về một tài khoản người dùng trong hệ thống
 * - Dùng để lưu trữ và truyền dữ liệu tài khoản giữa các lớp (database, activity, adapter)
 * - Đại diện cho một dòng trong bảng taiKhoan của cơ sở dữ liệu
 *
 * CÁC TRƯỜNG DỮ LIỆU:
 * - id: định danh duy nhất cho tài khoản (tự động tăng)
 * - tenTaiKhoan: tên đăng nhập của người dùng
 * - matKhau: mật khẩu (đã mã hóa) của người dùng
 * - vaiTro: quyền hạn của người dùng (Admin, Lecturer, Student)
 * - hoTen: họ tên đầy đủ của người dùng
 *
 * NẾU KHÔNG CÓ LỚP NÀY:
 * - Không thể lưu trữ thông tin tài khoản người dùng trong code
 * - Không thể truyền thông tin đăng nhập giữa các activity
 * - Không thể hiển thị thông tin người dùng trong giao diện
 */
public class TaiKhoan {
    // Định danh duy nhất cho tài khoản (khóa chính trong cơ sở dữ liệu)
    private int id;
    
    // Tên đăng nhập của người dùng (được sử dụng để đăng nhập)
    private String tenTaiKhoan;
    
    // Mật khẩu của người dùng (đã được mã hóa)
    private String matKhau;
    
    // Vai trò của người dùng trong hệ thống (Admin, Lecturer, Student)
    private String vaiTro;
    
    // Họ tên đầy đủ của người dùng
    private String hoTen;

    /**
     * Hàm khởi tạo mặc định không tham số cho đối tượng TaiKhoan
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng TaiKhoan rỗng
     * - Dùng khi cần tạo đối tượng rồi sau đó đặt giá trị cho các thuộc tính
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo đối tượng TaiKhoan rỗng
     * - Một số trường hợp sử dụng đối tượng sẽ không khả thi
     */
    public TaiKhoan() {
    }

    /**
     * Hàm khởi tạo đầy đủ tham số cho đối tượng TaiKhoan bao gồm ID
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng TaiKhoan với tất cả thông tin
     * - Dùng khi lấy dữ liệu từ cơ sở dữ liệu (đã có ID)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int id: định danh duy nhất của tài khoản trong cơ sở dữ liệu
     * - String tenTaiKhoan: tên đăng nhập của người dùng
     * - String matKhau: mật khẩu của người dùng (đã mã hóa)
     * - String vaiTro: vai trò của người dùng (Admin/Lecturer/Student)
     * - String hoTen: họ tên đầy đủ của người dùng
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo đối tượng từ dữ liệu đã có ID
     * - Khi lấy dữ liệu từ database sẽ khó ánh xạ toàn bộ thông tin
     */
    public TaiKhoan(int id, String tenTaiKhoan, String matKhau, String vaiTro, String hoTen) {
        this.id = id;
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.hoTen = hoTen;
    }

    /**
     * Hàm khởi tạo tham số cho đối tượng TaiKhoan không bao gồm ID
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng TaiKhoan với thông tin người dùng cung cấp
     * - Dùng khi thêm tài khoản mới (ID sẽ tự động tăng)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenTaiKhoan: tên đăng nhập của người dùng
     * - String matKhau: mật khẩu của người dùng (sẽ được mã hóa sau)
     * - String vaiTro: vai trò của người dùng (Admin/Lecturer/Student)
     * - String hoTen: họ tên đầy đủ của người dùng
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo tài khoản mới không có ID
     * - Khi đăng ký tài khoản sẽ không thể tạo đối tượng phù hợp
     */
    public TaiKhoan(String tenTaiKhoan, String matKhau, String vaiTro, String hoTen) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.hoTen = hoTen;
    }

    /**
     * Hàm lấy ID của tài khoản
     *
     * CÔNG DỤNG:
     * - Trả về định danh duy nhất của tài khoản
     * - Dùng để xác định tài khoản trong các thao tác cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - int: ID của tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy ID của tài khoản
     * - Không thể thực hiện các thao tác cần ID tài khoản
     */
    public int getId() {
        return id;
    }

    /**
     * Hàm đặt ID cho tài khoản
     *
     * CÔNG DỤNG:
     * - Đặt định danh duy nhất cho tài khoản
     * - Dùng khi ánh xạ dữ liệu từ cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int id: ID mới cần đặt cho tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập ID cho tài khoản đã tạo
     * - Không thể ánh xạ dữ liệu từ cơ sở dữ liệu
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Hàm lấy tên đăng nhập của tài khoản
     *
     * CÔNG DỤNG:
     * - Trả về tên đăng nhập của người dùng
     * - Dùng để xác định danh tính người dùng trong hệ thống
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên đăng nhập của tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy tên đăng nhập của tài khoản
     * - Không thể xác định danh tính người dùng
     */
    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    /**
     * Hàm đặt tên đăng nhập cho tài khoản
     *
     * CÔNG DỤNG:
     * - Đặt tên đăng nhập cho tài khoản
     * - Dùng khi tạo hoặc cập nhật thông tin tài khoản
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenTaiKhoan: tên đăng nhập mới cho tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập tên đăng nhập cho tài khoản
     * - Không thể cập nhật tên đăng nhập
     */
    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    /**
     * Hàm lấy mật khẩu của tài khoản
     *
     * CÔNG DỤNG:
     * - Trả về mật khẩu của người dùng (đã mã hóa)
     * - Dùng trong quá trình xác thực đăng nhập
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: mật khẩu đã mã hóa của tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy mật khẩu để xác thực
     * - Tính năng đăng nhập sẽ không hoạt động
     */
    public String getMatKhau() {
        return matKhau;
    }

    /**
     * Hàm đặt mật khẩu cho tài khoản
     *
     * CÔNG DỤNG:
     * - Đặt mật khẩu cho tài khoản
     * - Dùng khi tạo hoặc thay đổi mật khẩu tài khoản
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String matKhau: mật khẩu mới cho tài khoản (sẽ được mã hóa)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập mật khẩu cho tài khoản
     * - Không thể thay đổi mật khẩu tài khoản
     */
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    /**
     * Hàm lấy vai trò của tài khoản
     *
     * CÔNG DỤNG:
     * - Trả về vai trò của người dùng (Admin, Lecturer, Student)
     * - Dùng để phân quyền truy cập trong ứng dụng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: vai trò của tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy vai trò của tài khoản
     * - Không thể phân quyền truy cập trong ứng dụng
     */
    public String getVaiTro() {
        return vaiTro;
    }

    /**
     * Hàm đặt vai trò cho tài khoản
     *
     * CÔNG DỤNG:
     * - Đặt vai trò cho tài khoản (Admin, Lecturer, Student)
     * - Dùng khi tạo hoặc cập nhật quyền hạn tài khoản
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String vaiTro: vai trò mới cho tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập vai trò cho tài khoản
     * - Không thể phân quyền truy cập
     */
    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    /**
     * Hàm lấy họ tên đầy đủ của tài khoản
     *
     * CÔNG DỤNG:
     * - Trả về họ tên đầy đủ của người dùng
     * - Dùng để hiển thị tên người dùng trong giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: họ tên đầy đủ của tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy họ tên của tài khoản
     * - Không thể hiển thị tên người dùng trong giao diện
     */
    public String getHoTen() {
        return hoTen;
    }

    /**
     * Hàm đặt họ tên cho tài khoản
     *
     * CÔNG DỤNG:
     * - Đặt họ tên đầy đủ cho tài khoản
     * - Dùng khi tạo hoặc cập nhật thông tin cá nhân
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String hoTen: họ tên đầy đủ mới cho tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập họ tên cho tài khoản
     * - Không thể cập nhật thông tin cá nhân
     */
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    /**
     * Hàm chuyển đổi đối tượng TaiKhoan thành chuỗi
     *
     * CÔNG DỤNG:
     * - Trả về biểu diễn chuỗi của đối tượng TaiKhoan
     * - Trong trường hợp này, trả về tên đăng nhập
     * - Dùng khi đối tượng cần được hiển thị dưới dạng chuỗi (trong Spinner, ListView, v.v.)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên đăng nhập của tài khoản
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Khi hiển thị đối tượng trong giao diện người dùng sẽ chỉ hiện mã hash
     * - Không thể hiển thị tên tài khoản một cách rõ ràng trong các thành phần UI
     */
    @Override
    public String toString() {
        return tenTaiKhoan;
    }
}
