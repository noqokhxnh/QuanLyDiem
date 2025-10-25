package com.example.qldiem.models;

/**
 * Model class cho bảng LOP (Lớp học) - đại diện cho thông tin lớp học trong hệ thống
 *
 * CÔNG DỤNG:
 * - Chứa thông tin chi tiết về một lớp học trong hệ thống
 * - Mỗi lớp có thể có 1 giảng viên chủ nhiệm
 * - Dùng để lưu trữ và truyền dữ liệu lớp học giữa các lớp (database, activity, adapter)
 * - Đại diện cho một dòng trong bảng LOP của cơ sở dữ liệu
 * - Bao gồm cả thông tin mở rộng như tên giảng viên chủ nhiệm để hiển thị thuận tiện
 *
 * CÁC TRƯỜNG DỮ LIỆU:
 * - id: định danh duy nhất cho lớp học (tự động tăng)
 * - maLop: mã lớp học (định danh duy nhất)
 * - tenLop: tên đầy đủ của lớp học
 * - giangVienChuNhiem: username của giảng viên chủ nhiệm lớp (liên kết với bảng taiKhoan)
 * - tenGVCN: tên giảng viên chủ nhiệm (thông tin mở rộng, không lưu trong DB)
 *
 * NẾU KHÔNG CÓ LỚP NÀY:
 * - Không thể lưu trữ thông tin lớp học trong code
 * - Không thể hiển thị danh sách lớp học
 * - Không thể quản lý sinh viên theo lớp học
 * - Không thể gán giảng viên chủ nhiệm cho lớp học
 */
public class Lop {
    // Định danh duy nhất cho lớp học (khóa chính trong cơ sở dữ liệu)
    private int id;
    
    // Mã lớp học (định danh duy nhất, ví dụ: CNTT01)
    private String maLop;
    
    // Tên đầy đủ của lớp học (ví dụ: Công nghệ thông tin 01)
    private String tenLop;
    
    // Username của giảng viên chủ nhiệm lớp (liên kết với bảng taiKhoan)
    private String giangVienChuNhiem;
    
    // Tên giảng viên chủ nhiệm (thông tin mở rộng để hiển thị, không lưu trong DB)
    private String tenGVCN;

    /**
     * Hàm khởi tạo mặc định không tham số cho đối tượng Lop
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng Lop rỗng
     * - Dùng khi cần tạo đối tượng rồi sau đó đặt giá trị cho các thuộc tính
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo đối tượng Lop rỗng
     * - Một số trường hợp sử dụng đối tượng sẽ không khả thi
     */
    public Lop() {
    }

    /**
     * Hàm khởi tạo đầy đủ tham số cho đối tượng Lop bao gồm ID
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng Lop với tất cả thông tin cơ bản
     * - Dùng khi lấy dữ liệu từ cơ sở dữ liệu (đã có ID)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int id: định danh duy nhất của lớp học trong cơ sở dữ liệu
     * - String maLop: mã lớp học (định danh duy nhất)
     * - String tenLop: tên đầy đủ của lớp học
     * - String giangVienChuNhiem: username của giảng viên chủ nhiệm lớp
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo đối tượng từ dữ liệu đã có ID
     * - Khi lấy dữ liệu từ database sẽ khó ánh xạ toàn bộ thông tin
     */
    public Lop(int id, String maLop, String tenLop, String giangVienChuNhiem) {
        this.id = id;
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.giangVienChuNhiem = giangVienChuNhiem;
    }

    /**
     * Hàm khởi tạo tham số cho đối tượng Lop không bao gồm ID
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng Lop với thông tin lớp học cung cấp
     * - Dùng khi thêm lớp học mới (ID sẽ tự động tăng)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maLop: mã lớp học (định danh duy nhất)
     * - String tenLop: tên đầy đủ của lớp học
     * - String giangVienChuNhiem: username của giảng viên chủ nhiệm lớp
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo lớp học mới không có ID
     * - Khi thêm lớp học sẽ không thể tạo đối tượng phù hợp
     */
    public Lop(String maLop, String tenLop, String giangVienChuNhiem) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.giangVienChuNhiem = giangVienChuNhiem;
    }
    
    /**
     * Hàm khởi tạo cũ để duy trì tính tương thích với phiên bản trước
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng Lop với mã lớp và tên lớp (không có giảng viên chủ nhiệm)
     * - Dùng để duy trì tính tương thích ngược với mã nguồn cũ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maLop: mã lớp học (định danh duy nhất)
     * - String tenLop: tên đầy đủ của lớp học
     *
     * GHI CHÚ:
     * - Phương thức này đã bị đánh dấu là @Deprecated và chỉ dùng để tương thích ngược
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Vẫn có thể tạo đối tượng lớp học theo cách mới
     * - Không ảnh hưởng đến chức năng chính của ứng dụng
     */
    // Constructor cũ để tương thích
    @Deprecated
    public Lop(String maLop, String tenLop) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.giangVienChuNhiem = null;
    }

    /**
     * Hàm lấy ID của lớp học
     *
     * CÔNG DỤNG:
     * - Trả về định danh duy nhất của lớp học
     * - Dùng để xác định lớp học trong các thao tác cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - int: ID của lớp học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy ID của lớp học
     * - Không thể thực hiện các thao tác cần ID lớp học
     */
    public int getId() {
        return id;
    }

    /**
     * Hàm đặt ID cho lớp học
     *
     * CÔNG DỤNG:
     * - Đặt định danh duy nhất cho lớp học
     * - Dùng khi ánh xạ dữ liệu từ cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int id: ID mới cần đặt cho lớp học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập ID cho lớp học đã tạo
     * - Không thể ánh xạ dữ liệu từ cơ sở dữ liệu
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Hàm lấy mã lớp học
     *
     * CÔNG DỤNG:
     * - Trả về mã lớp học (định danh duy nhất)
     * - Dùng để xác định lớp học trong hệ thống
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: mã lớp học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy mã lớp học
     * - Không thể xác định danh tính lớp học trong hệ thống
     */
    public String getMaLop() {
        return maLop;
    }

    /**
     * Hàm đặt mã lớp học
     *
     * CÔNG DỤNG:
     * - Đặt mã lớp học cho đối tượng
     * - Dùng khi tạo hoặc cập nhật thông tin lớp học
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maLop: mã lớp học mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập mã lớp học
     * - Không thể cập nhật mã lớp học
     */
    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    /**
     * Hàm lấy tên lớp học
     *
     * CÔNG DỤNG:
     * - Trả về tên đầy đủ của lớp học
     * - Dùng để hiển thị tên lớp học trong giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên đầy đủ của lớp học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy tên lớp học để hiển thị
     * - Giao diện sẽ không thể hiển thị tên lớp học
     */
    public String getTenLop() {
        return tenLop;
    }

    /**
     * Hàm đặt tên lớp học
     *
     * CÔNG DỤNG:
     * - Đặt tên đầy đủ cho lớp học
     * - Dùng khi tạo hoặc cập nhật thông tin lớp học
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenLop: tên đầy đủ mới của lớp học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập tên cho lớp học
     * - Không thể cập nhật tên lớp học
     */
    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    /**
     * Hàm lấy username của giảng viên chủ nhiệm lớp
     *
     * CÔNG DỤNG:
     * - Trả về username của giảng viên chủ nhiệm lớp học
     * - Dùng để liên kết với tài khoản giảng viên trong hệ thống
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: username của giảng viên chủ nhiệm
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy thông tin giảng viên chủ nhiệm
     * - Không thể xác định giảng viên phụ trách lớp học
     */
    public String getGiangVienChuNhiem() {
        return giangVienChuNhiem;
    }

    /**
     * Hàm đặt giảng viên chủ nhiệm cho lớp học
     *
     * CÔNG DỤNG:
     * - Đặt username của giảng viên chủ nhiệm cho lớp học
     * - Dùng khi tạo hoặc cập nhật giảng viên chủ nhiệm lớp học
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String giangVienChuNhiem: username của giảng viên chủ nhiệm mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập giảng viên chủ nhiệm cho lớp học
     * - Không thể cập nhật giảng viên chủ nhiệm lớp học
     */
    public void setGiangVienChuNhiem(String giangVienChuNhiem) {
        this.giangVienChuNhiem = giangVienChuNhiem;
    }

    /**
     * Hàm lấy tên giảng viên chủ nhiệm lớp (thông tin mở rộng)
     *
     * CÔNG DỤNG:
     * - Trả về tên giảng viên chủ nhiệm lớp học (thông tin mở rộng, không lưu trong DB)
     * - Dùng để hiển thị tên giảng viên chủ nhiệm trong giao diện người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên giảng viên chủ nhiệm
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể hiển thị tên giảng viên chủ nhiệm trong giao diện
     * - Người dùng chỉ thấy username thay vì tên đầy đủ
     */
    public String getTenGVCN() {
        return tenGVCN;
    }

    /**
     * Hàm đặt tên giảng viên chủ nhiệm lớp (thông tin mở rộng)
     *
     * CÔNG DỤNG:
     * - Đặt tên giảng viên chủ nhiệm cho lớp học (thông tin mở rộng, không lưu trong DB)
     * - Dùng khi cần thiết lập tên giảng viên chủ nhiệm để hiển thị trong giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenGVCN: tên giảng viên chủ nhiệm mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập tên giảng viên chủ nhiệm để hiển thị
     * - Giao diện sẽ không thể hiển thị tên giảng viên chủ nhiệm đầy đủ
     */
    public void setTenGVCN(String tenGVCN) {
        this.tenGVCN = tenGVCN;
    }

    /**
     * Hàm chuyển đổi đối tượng Lop thành chuỗi
     *
     * CÔNG DỤNG:
     * - Trả về biểu diễn chuỗi của đối tượng Lop
     * - Trong trường hợp này, trả về tên lớp và tên giảng viên chủ nhiệm (nếu có)
     * - Dùng khi đối tượng cần được hiển thị dưới dạng chuỗi (trong Spinner, ListView, v.v.)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: chuỗi gồm tên lớp và tên giảng viên chủ nhiệm (nếu có)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Khi hiển thị đối tượng trong giao diện người dùng sẽ chỉ hiện mã hash
     * - Không thể hiển thị lớp học một cách rõ ràng trong các thành phần UI
     */
    @Override
    public String toString() {
        return tenLop + (tenGVCN != null ? " (GVCN: " + tenGVCN + ")" : "");
    }
}
