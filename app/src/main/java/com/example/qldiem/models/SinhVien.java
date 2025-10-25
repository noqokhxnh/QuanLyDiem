package com.example.qldiem.models;

/**
 * Model class cho bảng SINHVIEN (Sinh viên) - đại diện cho thông tin sinh viên trong hệ thống
 *
 * CÔNG DỤNG:
 * - Chứa thông tin chi tiết về một sinh viên trong hệ thống
 * - Dùng để lưu trữ và truyền dữ liệu sinh viên giữa các lớp (database, activity, adapter)
 * - Đại diện cho một dòng trong bảng SINHVIEN của cơ sở dữ liệu
 * - Bao gồm cả thông tin mở rộng như tên lớp, tên chuyên ngành để hiển thị thuận tiện
 *
 * CÁC TRƯỜNG DỮ LIỆU:
 * - id: định danh duy nhất cho sinh viên (tự động tăng)
 * - maSv: mã sinh viên (định danh duy nhất)
 * - tenSV: tên đầy đủ của sinh viên
 * - email: địa chỉ email của sinh viên
 * - hinh: ảnh đại diện (dưới dạng chuỗi base64)
 * - maLop: mã lớp mà sinh viên đang học
 * - maChuyenNganh: mã chuyên ngành của sinh viên
 * - tenLop: tên lớp (thông tin mở rộng, không lưu trong DB)
 * - tenChuyenNganh: tên chuyên ngành (thông tin mở rộng, không lưu trong DB)
 *
 * NẾU KHÔNG CÓ LỚP NÀY:
 * - Không thể lưu trữ thông tin sinh viên trong code
 * - Không thể hiển thị danh sách sinh viên
 * - Không thể quản lý thông tin cá nhân của sinh viên
 */
public class SinhVien {
    // Định danh duy nhất cho sinh viên (khóa chính trong cơ sở dữ liệu)
    private int id;
    
    // Mã sinh viên (được sử dụng như ID định danh duy nhất)
    private String maSv;
    
    // Tên đầy đủ của sinh viên
    private String tenSV;
    
    // Email liên lạc của sinh viên
    private String email;
    
    // Ảnh đại diện của sinh viên (dưới dạng chuỗi base64)
    private String hinh;
    
    // Mã lớp mà sinh viên đang học
    private String maLop;
    
    // Mã chuyên ngành của sinh viên
    private String maChuyenNganh;
    
    // Tên lớp (thông tin mở rộng để hiển thị, không lưu trong DB)
    private String tenLop;
    
    // Tên chuyên ngành (thông tin mở rộng để hiển thị, không lưu trong DB)
    private String tenChuyenNganh;

    /**
     * Hàm khởi tạo mặc định không tham số cho đối tượng SinhVien
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng SinhVien rỗng
     * - Dùng khi cần tạo đối tượng rồi sau đó đặt giá trị cho các thuộc tính
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo đối tượng SinhVien rỗng
     * - Một số trường hợp sử dụng đối tượng sẽ không khả thi
     */
    public SinhVien() {
    }

    /**
     * Hàm khởi tạo đầy đủ tham số cho đối tượng SinhVien bao gồm ID
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng SinhVien với tất cả thông tin cơ bản
     * - Dùng khi lấy dữ liệu từ cơ sở dữ liệu (đã có ID)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int id: định danh duy nhất của sinh viên trong cơ sở dữ liệu
     * - String maSv: mã số sinh viên (định danh duy nhất)
     * - String tenSV: tên đầy đủ của sinh viên
     * - String email: địa chỉ email của sinh viên
     * - String hinh: ảnh đại diện dưới dạng chuỗi base64
     * - String maLop: mã lớp mà sinh viên đang học
     * - String maChuyenNganh: mã chuyên ngành của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo đối tượng từ dữ liệu đã có ID
     * - Khi lấy dữ liệu từ database sẽ khó ánh xạ toàn bộ thông tin
     */
    public SinhVien(int id, String maSv, String tenSV, String email, String hinh, String maLop, String maChuyenNganh) {
        this.id = id;
        this.maSv = maSv;
        this.tenSV = tenSV;
        this.email = email;
        this.hinh = hinh;
        this.maLop = maLop;
        this.maChuyenNganh = maChuyenNganh;
    }

    /**
     * Hàm khởi tạo tham số cho đối tượng SinhVien không bao gồm ID
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng SinhVien với thông tin sinh viên cung cấp
     * - Dùng khi thêm sinh viên mới (ID sẽ tự động tăng)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maSv: mã số sinh viên (định danh duy nhất)
     * - String tenSV: tên đầy đủ của sinh viên
     * - String email: địa chỉ email của sinh viên
     * - String hinh: ảnh đại diện dưới dạng chuỗi base64
     * - String maLop: mã lớp mà sinh viên đang học
     * - String maChuyenNganh: mã chuyên ngành của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo sinh viên mới không có ID
     * - Khi thêm sinh viên sẽ không thể tạo đối tượng phù hợp
     */
    public SinhVien(String maSv, String tenSV, String email, String hinh, String maLop, String maChuyenNganh) {
        this.maSv = maSv;
        this.tenSV = tenSV;
        this.email = email;
        this.hinh = hinh;
        this.maLop = maLop;
        this.maChuyenNganh = maChuyenNganh;
    }

    /**
     * Hàm lấy ID của sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về định danh duy nhất của sinh viên
     * - Dùng để xác định sinh viên trong các thao tác cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - int: ID của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy ID của sinh viên
     * - Không thể thực hiện các thao tác cần ID sinh viên
     */
    public int getId() {
        return id;
    }

    /**
     * Hàm đặt ID cho sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt định danh duy nhất cho sinh viên
     * - Dùng khi ánh xạ dữ liệu từ cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int id: ID mới cần đặt cho sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập ID cho sinh viên đã tạo
     * - Không thể ánh xạ dữ liệu từ cơ sở dữ liệu
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Hàm lấy mã sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về mã số sinh viên (định danh duy nhất)
     * - Dùng để xác định sinh viên trong hệ thống
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: mã sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy mã sinh viên
     * - Không thể xác định danh tính sinh viên trong hệ thống
     */
    public String getMaSv() {
        return maSv;
    }

    /**
     * Hàm đặt mã sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt mã số sinh viên cho đối tượng
     * - Dùng khi tạo hoặc cập nhật thông tin sinh viên
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maSv: mã sinh viên mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập mã sinh viên
     * - Không thể cập nhật mã sinh viên
     */
    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    /**
     * Hàm lấy tên sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về tên đầy đủ của sinh viên
     * - Dùng để hiển thị tên sinh viên trong giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên đầy đủ của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy tên sinh viên để hiển thị
     * - Giao diện sẽ không thể hiển thị tên sinh viên
     */
    public String getTenSV() {
        return tenSV;
    }

    /**
     * Hàm đặt tên sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt tên đầy đủ cho sinh viên
     * - Dùng khi tạo hoặc cập nhật thông tin sinh viên
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenSV: tên đầy đủ mới của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập tên cho sinh viên
     * - Không thể cập nhật tên sinh viên
     */
    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    /**
     * Hàm lấy email của sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về địa chỉ email của sinh viên
     * - Dùng để liên lạc hoặc hiển thị thông tin liên hệ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: email của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy email để hiển thị hoặc liên lạc
     * - Tính năng liên hệ sẽ không hoạt động
     */
    public String getEmail() {
        return email;
    }

    /**
     * Hàm đặt email cho sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt địa chỉ email cho sinh viên
     * - Dùng khi tạo hoặc cập nhật thông tin liên hệ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String email: email mới của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập email cho sinh viên
     * - Không thể cập nhật thông tin liên hệ
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Hàm lấy ảnh đại diện của sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về ảnh đại diện của sinh viên dưới dạng chuỗi base64
     * - Dùng để hiển thị hình ảnh trong giao diện người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: ảnh đại diện dưới dạng chuỗi base64
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy ảnh đại diện để hiển thị
     * - Giao diện sẽ không hiển thị hình ảnh sinh viên
     */
    public String getHinh() {
        return hinh;
    }

    /**
     * Hàm đặt ảnh đại diện cho sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt ảnh đại diện cho sinh viên
     * - Dùng khi cập nhật hình ảnh mới
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String hinh: ảnh đại diện dưới dạng chuỗi base64
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập ảnh đại diện cho sinh viên
     * - Không thể cập nhật hình ảnh sinh viên
     */
    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    /**
     * Hàm lấy mã lớp của sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về mã lớp mà sinh viên đang học
     * - Dùng để xác định lớp của sinh viên
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: mã lớp của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy mã lớp của sinh viên
     * - Không thể xác định sinh viên thuộc lớp nào
     */
    public String getMaLop() {
        return maLop;
    }

    /**
     * Hàm đặt mã lớp cho sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt mã lớp mà sinh viên đang học
     * - Dùng khi tạo hoặc thay đổi lớp cho sinh viên
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maLop: mã lớp mới cho sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập lớp cho sinh viên
     * - Không thể cập nhật lớp học của sinh viên
     */
    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    /**
     * Hàm lấy mã chuyên ngành của sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về mã chuyên ngành của sinh viên
     * - Dùng để xác định chuyên ngành của sinh viên
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: mã chuyên ngành của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy mã chuyên ngành của sinh viên
     * - Không thể xác định sinh viên học chuyên ngành nào
     */
    public String getMaChuyenNganh() {
        return maChuyenNganh;
    }

    /**
     * Hàm đặt mã chuyên ngành cho sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt mã chuyên ngành cho sinh viên
     * - Dùng khi tạo hoặc thay đổi chuyên ngành cho sinh viên
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maChuyenNganh: mã chuyên ngành mới cho sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập chuyên ngành cho sinh viên
     * - Không thể cập nhật chuyên ngành của sinh viên
     */
    public void setMaChuyenNganh(String maChuyenNganh) {
        this.maChuyenNganh = maChuyenNganh;
    }

    /**
     * Hàm lấy tên lớp của sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về tên lớp mà sinh viên đang học (thông tin mở rộng, không lưu trong DB)
     * - Dùng để hiển thị tên lớp thay vì chỉ mã lớp trong giao diện người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên lớp của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể hiển thị tên lớp trong giao diện
     * - Người dùng chỉ thấy mã lớp thay vì tên lớp dễ hiểu
     */
    public String getTenLop() {
        return tenLop;
    }

    /**
     * Hàm đặt tên lớp cho sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt tên lớp cho sinh viên (thông tin mở rộng, không lưu trong DB)
     * - Dùng khi cần thiết lập tên lớp để hiển thị trong giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenLop: tên lớp mới cho sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập tên lớp để hiển thị
     * - Giao diện sẽ không thể hiển thị tên lớp đầy đủ
     */
    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    /**
     * Hàm lấy tên chuyên ngành của sinh viên
     *
     * CÔNG DỤNG:
     * - Trả về tên chuyên ngành của sinh viên (thông tin mở rộng, không lưu trong DB)
     * - Dùng để hiển thị tên chuyên ngành thay vì chỉ mã chuyên ngành trong giao diện người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên chuyên ngành của sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể hiển thị tên chuyên ngành trong giao diện
     * - Người dùng chỉ thấy mã chuyên ngành thay vì tên đầy đủ
     */
    public String getTenChuyenNganh() {
        return tenChuyenNganh;
    }

    /**
     * Hàm đặt tên chuyên ngành cho sinh viên
     *
     * CÔNG DỤNG:
     * - Đặt tên chuyên ngành cho sinh viên (thông tin mở rộng, không lưu trong DB)
     * - Dùng khi cần thiết lập tên chuyên ngành để hiển thị trong giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenChuyenNganh: tên chuyên ngành mới cho sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập tên chuyên ngành để hiển thị
     * - Giao diện sẽ không thể hiển thị tên chuyên ngành đầy đủ
     */
    public void setTenChuyenNganh(String tenChuyenNganh) {
        this.tenChuyenNganh = tenChuyenNganh;
    }

    /**
     * Hàm chuyển đổi đối tượng SinhVien thành chuỗi
     *
     * CÔNG DỤNG:
     * - Trả về biểu diễn chuỗi của đối tượng SinhVien
     * - Trong trường hợp này, trả về mã sinh viên và tên sinh viên
     * - Dùng khi đối tượng cần được hiển thị dưới dạng chuỗi (trong Spinner, ListView, v.v.)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: chuỗi gồm mã sinh viên và tên sinh viên (ví dụ: "SV001 - Nguyễn Văn A")
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Khi hiển thị đối tượng trong giao diện người dùng sẽ chỉ hiện mã hash
     * - Không thể hiển thị sinh viên một cách rõ ràng trong các thành phần UI
     */
    @Override
    public String toString() {
        return maSv + " - " + tenSV;
    }
}
