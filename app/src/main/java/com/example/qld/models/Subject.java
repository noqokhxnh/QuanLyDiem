package com.example.qld.models;

/**
 * Lớp đại diện cho môn học trong hệ thống
 * Chứa thông tin về tên và mã môn học
 */
public class Subject {
    private int id;
    private String subjectName;
    private String subjectCode;

    /**
     * Constructor mặc định
     * 
     * Cách thức hoạt động:
     * 1. Khởi tạo một đối tượng Subject mới với các giá trị mặc định
     * 2. Tất cả các thuộc tính sẽ có giá trị mặc định (0 cho int, null cho String)
     * 3. Được sử dụng khi cần tạo một đối tượng Subject trống để gán giá trị sau này
     * 
     * Ví dụ: Subject subject = new Subject(); // Tạo một đối tượng Subject trống
     */
    public Subject() {}

    /**
     * Constructor đầy đủ tham số
     * 
     * Cách thức hoạt động:
     * 1. Khởi tạo một đối tượng Subject mới với tất cả các thuộc tính được cung cấp
     * 2. Gán các giá trị tham số cho các thuộc tính tương ứng của đối tượng
     * 3. Được sử dụng khi cần tạo một đối tượng Subject với tất cả thông tin đã biết
     * 
     * @param id ID của môn học (số nguyên dương, duy nhất trong hệ thống)
     *           Ví dụ: 1, 2, 3, ...
     * @param subjectName Tên của môn học (chuỗi không null, không rỗng)
     *                   Ví dụ: "Toán", "Lý", "Hóa", "Văn", "Anh"
     * @param subjectCode Mã của môn học (chuỗi không null, duy nhất)
     *                   Ví dụ: "TOAN", "LY", "HOA", "VAN", "ANH"
     * 
     * Ví dụ: Subject subject = new Subject(1, "Toán", "TOAN");
     */
    public Subject(int id, String subjectName, String subjectCode) {
        this.id = id;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    // Getters and Setters
    /**
     * Lấy ID của môn học
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính id
     * 2. ID là khóa chính duy nhất trong cơ sở dữ liệu
     * 3. Được sử dụng để xác định và truy vấn môn học cụ thể
     * 
     * @return ID của môn học (số nguyên dương nếu đã lưu trong DB, 0 nếu chưa gán)
     *         Ví dụ: 1, 2, 3, ...
     * 
     * Ví dụ: int subjectId = subject.getId(); // Trả về ID của môn học, ví dụ: 123
     */
    public int getId() { return id; }
    /**
     * Thiết lập ID cho môn học
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị id mới cho thuộc tính id của đối tượng
     * 2. Được sử dụng khi tạo mới môn học hoặc cập nhật ID từ cơ sở dữ liệu
     * 3. ID phải là duy nhất trong hệ thống
     * 
     * @param id ID của môn học (số nguyên dương, không được âm)
     *           Ví dụ: 1, 2, 3, ...
     * 
     * Ví dụ: subject.setId(123); // Thiết lập ID của môn học thành 123
     */
    public void setId(int id) { this.id = id; }

    /**
     * Lấy tên môn học
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính subjectName
     * 2. subjectName là tên đầy đủ của môn học được sử dụng trong giao diện
     * 3. Được hiển thị trong danh sách môn học và báo cáo
     * 
     * @return Tên môn học (chuỗi không null nếu đã gán)
     *         Ví dụ: "Toán", "Lý", "Hóa", "Văn", "Anh"
     * 
     * Ví dụ: String subjectName = subject.getSubjectName(); // Trả về tên môn học, ví dụ: "Toán"
     */
    public String getSubjectName() { return subjectName; }
    /**
     * Thiết lập tên môn học
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị subjectName mới cho thuộc tính subjectName của đối tượng
     * 2. Được sử dụng khi tạo mới môn học hoặc cập nhật tên môn học
     * 3. Nên là tên đầy đủ và dễ hiểu cho người dùng
     * 
     * @param subjectName Tên môn học (chuỗi không null, không rỗng)
     *                   Ví dụ: "Toán", "Lý", "Hóa", "Văn", "Anh"
     * 
     * Ví dụ: subject.setSubjectName("Toán"); // Thiết lập tên môn học thành "Toán"
     */
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    /**
     * Lấy mã môn học
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính subjectCode
     * 2. subjectCode là mã định danh ngắn gọn của môn học
     * 3. Được sử dụng trong các thao tác nhập liệu và tìm kiếm nhanh
     * 
     * @return Mã môn học (chuỗi không null nếu đã gán)
     *         Ví dụ: "TOAN", "LY", "HOA", "VAN", "ANH"
     * 
     * Ví dụ: String subjectCode = subject.getSubjectCode(); // Trả về mã môn học, ví dụ: "TOAN"
     */
    public String getSubjectCode() { return subjectCode; }
    /**
     * Thiết lập mã môn học
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị subjectCode mới cho thuộc tính subjectCode của đối tượng
     * 2. Được sử dụng khi tạo mới môn học hoặc cập nhật mã môn học
     * 3. subjectCode phải là duy nhất trong hệ thống và thường viết hoa
     * 
     * @param subjectCode Mã môn học (chuỗi không null, không rỗng, duy nhất)
     *                   Ví dụ: "TOAN", "LY", "HOA", "VAN", "ANH"
     * 
     * Ví dụ: subject.setSubjectCode("TOAN"); // Thiết lập mã môn học thành "TOAN"
     */
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
}