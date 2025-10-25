package com.example.qldiem.models;

/**
 * Model class cho bảng DIEM (Điểm số) - đại diện cho điểm số của sinh viên trong môn học
 *
 * CÔNG DỤNG:
 * - Chứa thông tin điểm số chi tiết của sinh viên trong một môn học
 * - Hỗ trợ hệ thống điểm 3 thành phần: Chuyên cần (10%), Giữa kỳ (30%), Cuối kỳ (60%)
 * - Tự động tính toán điểm tổng kết, điểm hệ 4 và điểm chữ
 * - Dùng để lưu trữ và truyền dữ liệu điểm giữa các lớp (database, activity, adapter)
 *
 * CÁC TRƯỜNG DỮ LIỆU:
 * - id: định danh duy nhất cho bản ghi điểm (tự động tăng)
 * - maSv: mã sinh viên
 * - maMH: mã môn học
 * - hocKy: học kỳ (1 hoặc 2)
 * - namHoc: năm học (ví dụ: 2024)
 * - diemChuyenCan: điểm chuyên cần (chiếm 10% tổng điểm)
 * - diemGiuaKy: điểm giữa kỳ (chiếm 30% tổng điểm)
 * - diemCuoiKy: điểm cuối kỳ (chiếm 60% tổng điểm)
 * - tenSV: tên sinh viên (thông tin mở rộng, không lưu trong DB)
 * - tenMonHoc: tên môn học (thông tin mở rộng, không lưu trong DB)
 *
 * NẾU KHÔNG CÓ LỚP NÀY:
 * - Không thể quản lý điểm số cho sinh viên
 * - Không thể tính toán điểm tổng kết theo công thức chuyên cần-giữa kỳ-cuối kỳ
 * - Không thể hiển thị điểm số trong giao diện người dùng
 */
public class Diem {
    // Định danh duy nhất cho bản ghi điểm (khóa chính trong cơ sở dữ liệu)
    private int id;
    
    // Mã sinh viên - tham chiếu đến bảng SINHVIEN
    private String maSv;
    
    // Mã môn học - tham chiếu đến bảng MONHOC
    private String maMH;
    
    // Học kỳ (1 hoặc 2)
    private int hocKy;
    
    // Năm học (ví dụ: 2024)
    private int namHoc;
    
    // Điểm chuyên cần - chiếm 10% tổng điểm
    private float diemChuyenCan;
    
    // Điểm giữa kỳ - chiếm 30% tổng điểm
    private float diemGiuaKy;
    
    // Điểm cuối kỳ - chiếm 60% tổng điểm
    private float diemCuoiKy;
    
    // Tên sinh viên (thông tin mở rộng để hiển thị, không lưu trong DB)
    private String tenSV;
    
    // Tên môn học (thông tin mở rộng để hiển thị, không lưu trong DB)
    private String tenMonHoc;

    /**
     * Hàm khởi tạo mặc định không tham số cho đối tượng Diem
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng Diem rỗng
     * - Thiết lập các điểm thành phần về 0
     * - Dùng khi cần tạo đối tượng rồi sau đó đặt giá trị cho các thuộc tính
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo đối tượng Diem rỗng
     * - Một số trường hợp sử dụng đối tượng sẽ không khả thi
     */
    public Diem() {
        this.diemChuyenCan = 0;
        this.diemGiuaKy = 0;
        this.diemCuoiKy = 0;
    }

    /**
     * Hàm khởi tạo cho đối tượng Diem với thông tin cơ bản
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng Diem với thông tin sinh viên, môn học, học kỳ và năm học
     * - Thiết lập các điểm thành phần về 0 ban đầu
     * - Dùng khi tạo điểm mới nhưng chưa có điểm số cụ thể
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maSv: mã sinh viên
     * - String maMH: mã môn học
     * - int hocKy: học kỳ (1 hoặc 2)
     * - int namHoc: năm học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tạo đối tượng điểm với thông tin xác định
     * - Không thể tạo bản ghi điểm mới trong cơ sở dữ liệu
     */
    public Diem(String maSv, String maMH, int hocKy, int namHoc) {
        this.maSv = maSv;
        this.maMH = maMH;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
        this.diemChuyenCan = 0;
        this.diemGiuaKy = 0;
        this.diemCuoiKy = 0;
    }

    /**
     * Hàm tính điểm tổng kết theo công thức: Chuyên cần*0.1 + Giữa kỳ*0.3 + Cuối kỳ*0.6
     *
     * CÔNG DỤNG:
     * - Tính điểm tổng kết dựa trên 3 thành phần điểm với trọng số tương ứng
     * - Chuyên cần chiếm 10%, Giữa kỳ chiếm 30%, Cuối kỳ chiếm 60%
     * - Trả về điểm trên thang điểm 10
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - float: điểm tổng kết trên thang điểm 10
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể tính toán điểm tổng kết tự động
     * - Người dùng phải tính điểm thủ công
     * - Không thể hiển thị điểm tổng kết trong giao diện
     */
    public float getDiemTongKet() {
        return (diemChuyenCan * 0.1f) + (diemGiuaKy * 0.3f) + (diemCuoiKy * 0.6f);
    }
    
    /**
     * Hàm quy đổi điểm tổng kết sang hệ 4
     *
     * CÔNG DỤNG:
     * - Chuyển đổi điểm từ thang điểm 10 sang thang điểm 4
     * - Sử dụng bảng quy đổi tiêu chuẩn của hệ thống giáo dục
     *
     * CÁC BIẾN TRONG HÀM:
     * - diemTK: điểm tổng kết được lấy từ phương thức getDiemTongKet()
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - float: điểm theo hệ 4 (từ 0.0 đến 4.0)
     *
     * BẢNG QUY ĐỔI:
     * - 9.0-10.0: 4.0
     * - 8.5-8.9: 3.7
     * - 8.0-8.4: 3.5
     * - 7.0-7.9: 3.0
     * - 6.5-6.9: 2.5
     * - 5.5-6.4: 2.0
     * - 5.0-5.4: 1.5
     * - 4.0-4.9: 1.0
     * - Dưới 4.0: 0.0
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể hiển thị điểm theo hệ 4
     * - Người dùng không có thang điểm chuẩn để so sánh
     */
    public float getDiemHe4() {
        float diemTK = getDiemTongKet();
        if (diemTK >= 9.0) return 4.0f;
        if (diemTK >= 8.5) return 3.7f;
        if (diemTK >= 8.0) return 3.5f;
        if (diemTK >= 7.0) return 3.0f;
        if (diemTK >= 6.5) return 2.5f;
        if (diemTK >= 5.5) return 2.0f;
        if (diemTK >= 5.0) return 1.5f;
        if (diemTK >= 4.0) return 1.0f;
        return 0.0f;
    }
    
    /**
     * Hàm quy đổi điểm tổng kết sang hệ chữ
     *
     * CÔNG DỤNG:
     * - Chuyển đổi điểm từ thang điểm 10 sang hệ chữ (A+, A, B+, B, v.v.)
     * - Sử dụng bảng quy đổi tiêu chuẩn của hệ thống giáo dục
     *
     * CÁC BIẾN TRONG HÀM:
     * - diemTK: điểm tổng kết được lấy từ phương thức getDiemTongKet()
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: điểm theo hệ chữ (A+, A, B+, B, C+, C, D+, D, F)
     *
     * BẢNG QUY ĐỔI:
     * - 9.0-10.0: A+
     * - 8.5-8.9: A
     * - 8.0-8.4: B+
     * - 7.0-7.9: B
     * - 6.5-6.9: C+
     * - 5.5-6.4: C
     * - 5.0-5.4: D+
     * - 4.0-4.9: D
     * - Dưới 4.0: F
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể hiển thị điểm theo hệ chữ
     * - Người dùng không có thang điểm dễ hiểu để đánh giá
     */
    public String getDiemChu() {
        float diemTK = getDiemTongKet();
        if (diemTK >= 9.0) return "A+";
        if (diemTK >= 8.5) return "A";
        if (diemTK >= 8.0) return "B+";
        if (diemTK >= 7.0) return "B";
        if (diemTK >= 6.5) return "C+";
        if (diemTK >= 5.5) return "C";
        if (diemTK >= 5.0) return "D+";
        if (diemTK >= 4.0) return "D";
        return "F";
    }
    
    /**
     * Hàm khởi tạo cũ để duy trì tính tương thích với phiên bản trước
     *
     * CÔNG DỤNG:
     * - Tạo một đối tượng Diem với thông tin cơ bản từ phiên bản cũ
     * - Gán điểm truyền vào cho điểm cuối kỳ tạm thời
     * - Đặt học kỳ là 1 và năm học là 2024 mặc định
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maSv: mã sinh viên
     * - String maMH: mã môn học
     * - float diem: điểm số (sẽ được gán cho điểm cuối kỳ)
     *
     * GHI CHÚ:
     * - Phương thức này đã bị đánh dấu là @Deprecated và chỉ dùng để tương thích ngược
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Vẫn có thể tạo đối tượng điểm theo cách mới
     * - Không ảnh hưởng đến chức năng chính của ứng dụng
     */
    @Deprecated
    public Diem(String maSv, String maMH, float diem) {
        this.maSv = maSv;
        this.maMH = maMH;
        this.diemCuoiKy = diem; // Tạm thời map vào điểm cuối kỳ
        this.hocKy = 1;
        this.namHoc = 2024;
    }
    
    /**
     * Phương thức cũ để lấy điểm, duy trì tính tương thích với phiên bản trước
     *
     * CÔNG DỤNG:
     * - Trả về điểm tổng kết như là điểm chính
     * - Được dùng để duy trì tính tương thích với mã nguồn cũ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - float: điểm tổng kết
     *
     * GHI CHÚ:
     * - Phương thức này đã bị đánh dấu là @Deprecated và chỉ dùng để tương thích ngược
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không ảnh hưởng đến chức năng chính của ứng dụng
     * - Ứng dụng vẫn hoạt động bình thường với phương thức mới
     */
    @Deprecated
    public float getDiem() {
        return getDiemTongKet();
    }
    
    /**
     * Phương thức cũ để đặt điểm, duy trì tính tương thích với phiên bản trước
     *
     * CÔNG DỤNG:
     * - Đặt điểm vào trường điểm cuối kỳ
     * - Được dùng để duy trì tính tương thích với mã nguồn cũ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - float diem: điểm số mới cần đặt
     *
     * GHI CHÚ:
     * - Phương thức này đã bị đánh dấu là @Deprecated và chỉ dùng để tương thích ngược
     * - Chỉ ảnh hưởng đến điểm cuối kỳ, không cập nhật điểm chuyên cần và giữa kỳ
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không ảnh hưởng đến chức năng chính của ứng dụng
     * - Ứng dụng vẫn hoạt động bình thường với phương thức mới
     */
    @Deprecated
    public void setDiem(float diem) {
        this.diemCuoiKy = diem;
    }

    // Getters and Setters

    /**
     * Hàm lấy ID của bản ghi điểm
     *
     * CÔNG DỤNG:
     * - Trả về định danh duy nhất của bản ghi điểm
     * - Dùng để xác định điểm trong các thao tác cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - int: ID của bản ghi điểm
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy ID của bản ghi điểm
     * - Không thể thực hiện các thao tác cần ID điểm
     */
    public int getId() { return id; }
    
    /**
     * Hàm đặt ID cho bản ghi điểm
     *
     * CÔNG DỤNG:
     * - Đặt định danh duy nhất cho bản ghi điểm
     * - Dùng khi ánh xạ dữ liệu từ cơ sở dữ liệu
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int id: ID mới cần đặt cho bản ghi điểm
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập ID cho điểm đã tạo
     * - Không thể ánh xạ dữ liệu từ cơ sở dữ liệu
     */
    public void setId(int id) { this.id = id; }

    /**
     * Hàm lấy mã sinh viên của điểm
     *
     * CÔNG DỤNG:
     * - Trả về mã sinh viên liên kết với bản ghi điểm
     * - Dùng để xác định sinh viên nào có điểm này
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
     * - Không thể xác định sinh viên nào có điểm này
     * - Không thể hiển thị điểm theo sinh viên
     */
    public String getMaSv() { return maSv; }
    
    /**
     * Hàm đặt mã sinh viên cho điểm
     *
     * CÔNG DỤNG:
     * - Đặt mã sinh viên liên kết với bản ghi điểm
     * - Dùng khi tạo hoặc cập nhật thông tin điểm
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maSv: mã sinh viên mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể liên kết điểm với sinh viên
     * - Không thể cập nhật sinh viên cho điểm
     */
    public void setMaSv(String maSv) { this.maSv = maSv; }

    /**
     * Hàm lấy mã môn học của điểm
     *
     * CÔNG DỤNG:
     * - Trả về mã môn học liên kết với bản ghi điểm
     * - Dùng để xác định điểm thuộc môn học nào
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: mã môn học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xác định điểm thuộc môn học nào
     * - Không thể hiển thị điểm theo môn học
     */
    public String getMaMH() { return maMH; }
    
    /**
     * Hàm đặt mã môn học cho điểm
     *
     * CÔNG DỤNG:
     * - Đặt mã môn học liên kết với bản ghi điểm
     * - Dùng khi tạo hoặc cập nhật thông tin điểm
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String maMH: mã môn học mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể liên kết điểm với môn học
     * - Không thể cập nhật môn học cho điểm
     */
    public void setMaMH(String maMH) { this.maMH = maMH; }

    /**
     * Hàm lấy học kỳ của điểm
     *
     * CÔNG DỤNG:
     * - Trả về học kỳ (1 hoặc 2) mà điểm này thuộc về
     * - Dùng để phân loại điểm theo học kỳ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - int: học kỳ (1 hoặc 2)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xác định điểm thuộc học kỳ nào
     * - Không thể phân tích điểm theo học kỳ
     */
    public int getHocKy() { return hocKy; }
    
    /**
     * Hàm đặt học kỳ cho điểm
     *
     * CÔNG DỤNG:
     * - Đặt học kỳ (1 hoặc 2) cho bản ghi điểm
     * - Dùng khi tạo hoặc cập nhật thông tin điểm
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int hocKy: học kỳ mới (1 hoặc 2)
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập học kỳ cho điểm
     * - Không thể cập nhật học kỳ của điểm
     */
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    /**
     * Hàm lấy năm học của điểm
     *
     * CÔNG DỤNG:
     * - Trả về năm học mà điểm này thuộc về
     * - Dùng để phân loại điểm theo năm học
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - int: năm học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể xác định điểm thuộc năm học nào
     * - Không thể phân tích điểm theo năm học
     */
    public int getNamHoc() { return namHoc; }
    
    /**
     * Hàm đặt năm học cho điểm
     *
     * CÔNG DỤNG:
     * - Đặt năm học cho bản ghi điểm
     * - Dùng khi tạo hoặc cập nhật thông tin điểm
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - int namHoc: năm học mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập năm học cho điểm
     * - Không thể cập nhật năm học của điểm
     */
    public void setNamHoc(int namHoc) { this.namHoc = namHoc; }

    /**
     * Hàm lấy điểm chuyên cần
     *
     * CÔNG DỤNG:
     * - Trả về điểm chuyên cần (chiếm 10% tổng điểm)
     * - Dùng để hiển thị và tính toán điểm tổng kết
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - float: điểm chuyên cần
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy điểm chuyên cần
     * - Không thể tính toán điểm tổng kết chính xác
     */
    public float getDiemChuyenCan() { return diemChuyenCan; }
    
    /**
     * Hàm đặt điểm chuyên cần
     *
     * CÔNG DỤNG:
     * - Đặt điểm chuyên cần (chiếm 10% tổng điểm)
     * - Dùng khi tạo hoặc cập nhật điểm chuyên cần
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - float diemChuyenCan: điểm chuyên cần mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập điểm chuyên cần
     * - Không thể cập nhật điểm chuyên cần
     */
    public void setDiemChuyenCan(float diemChuyenCan) { this.diemChuyenCan = diemChuyenCan; }

    /**
     * Hàm lấy điểm giữa kỳ
     *
     * CÔNG DỤNG:
     * - Trả về điểm giữa kỳ (chiếm 30% tổng điểm)
     * - Dùng để hiển thị và tính toán điểm tổng kết
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - float: điểm giữa kỳ
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy điểm giữa kỳ
     * - Không thể tính toán điểm tổng kết chính xác
     */
    public float getDiemGiuaKy() { return diemGiuaKy; }
    
    /**
     * Hàm đặt điểm giữa kỳ
     *
     * CÔNG DỤNG:
     * - Đặt điểm giữa kỳ (chiếm 30% tổng điểm)
     * - Dùng khi tạo hoặc cập nhật điểm giữa kỳ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - float diemGiuaKy: điểm giữa kỳ mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập điểm giữa kỳ
     * - Không thể cập nhật điểm giữa kỳ
     */
    public void setDiemGiuaKy(float diemGiuaKy) { this.diemGiuaKy = diemGiuaKy; }

    /**
     * Hàm lấy điểm cuối kỳ
     *
     * CÔNG DỤNG:
     * - Trả về điểm cuối kỳ (chiếm 60% tổng điểm)
     * - Dùng để hiển thị và tính toán điểm tổng kết
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - float: điểm cuối kỳ
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể lấy điểm cuối kỳ
     * - Không thể tính toán điểm tổng kết chính xác
     */
    public float getDiemCuoiKy() { return diemCuoiKy; }
    
    /**
     * Hàm đặt điểm cuối kỳ
     *
     * CÔNG DỤNG:
     * - Đặt điểm cuối kỳ (chiếm 60% tổng điểm)
     * - Dùng khi tạo hoặc cập nhật điểm cuối kỳ
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - float diemCuoiKy: điểm cuối kỳ mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập điểm cuối kỳ
     * - Không thể cập nhật điểm cuối kỳ
     */
    public void setDiemCuoiKy(float diemCuoiKy) { this.diemCuoiKy = diemCuoiKy; }

    /**
     * Hàm lấy tên sinh viên (thông tin mở rộng)
     *
     * CÔNG DỤNG:
     * - Trả về tên sinh viên (thông tin mở rộng, không lưu trong DB)
     * - Dùng để hiển thị tên sinh viên trong giao diện người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên sinh viên
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể hiển thị tên sinh viên trong giao diện
     * - Người dùng chỉ thấy mã sinh viên thay vì tên đầy đủ
     */
    public String getTenSV() { return tenSV; }
    
    /**
     * Hàm đặt tên sinh viên (thông tin mở rộng)
     *
     * CÔNG DỤNG:
     * - Đặt tên sinh viên (thông tin mở rộng, không lưu trong DB)
     * - Dùng khi cần thiết lập tên sinh viên để hiển thị trong giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenSV: tên sinh viên mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập tên sinh viên để hiển thị
     * - Giao diện sẽ không thể hiển thị tên sinh viên đầy đủ
     */
    public void setTenSV(String tenSV) { this.tenSV = tenSV; }

    /**
     * Hàm lấy tên môn học (thông tin mở rộng)
     *
     * CÔNG DỤNG:
     * - Trả về tên môn học (thông tin mở rộng, không lưu trong DB)
     * - Dùng để hiển thị tên môn học trong giao diện người dùng
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: tên môn học
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể hiển thị tên môn học trong giao diện
     * - Người dùng chỉ thấy mã môn học thay vì tên đầy đủ
     */
    public String getTenMonHoc() { return tenMonHoc; }
    
    /**
     * Hàm đặt tên môn học (thông tin mở rộng)
     *
     * CÔNG DỤNG:
     * - Đặt tên môn học (thông tin mở rộng, không lưu trong DB)
     * - Dùng khi cần thiết lập tên môn học để hiển thị trong giao diện
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - String tenMonHoc: tên môn học mới
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Không thể thiết lập tên môn học để hiển thị
     * - Giao diện sẽ không thể hiển thị tên môn học đầy đủ
     */
    public void setTenMonHoc(String tenMonHoc) { this.tenMonHoc = tenMonHoc; }

    /**
     * Hàm chuyển đổi đối tượng Diem thành chuỗi
     *
     * CÔNG DỤNG:
     * - Trả về biểu diễn chuỗi của đối tượng Diem
     * - Trong trường hợp này, trả về thông tin sinh viên, môn học, học kỳ, năm học và điểm tổng kết
     * - Dùng khi đối tượng cần được hiển thị dưới dạng chuỗi (trong Spinner, ListView, v.v.)
     *
     * CÁC BIẾN TRONG HÀM:
     * - Không có biến cục bộ
     *
     * Ý NGHĨA CỦA CÁC ĐỐI SỐ:
     * - Không có đối số đầu vào
     *
     * GIÁ TRỊ TRẢ VỀ:
     * - String: chuỗi gồm mã sinh viên, mã môn học, học kỳ/năm học và điểm tổng kết
     *
     * NẾU KHÔNG CÓ HÀM NÀY:
     * - Khi hiển thị đối tượng trong giao diện người dùng sẽ chỉ hiện mã hash
     * - Không thể hiển thị điểm một cách rõ ràng trong các thành phần UI
     */
    @Override
    public String toString() {
        return maSv + " - " + maMH + " (HK" + hocKy + "/" + namHoc + "): " + String.format("%.2f", getDiemTongKet());
    }
}
