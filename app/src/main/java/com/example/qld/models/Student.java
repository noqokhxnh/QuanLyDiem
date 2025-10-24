package com.example.qld.models;

/**
 * Lớp đại diện cho học sinh trong hệ thống
 * Chứa thông tin cá nhân và điểm trung bình của học sinh
 */
public class Student {
    private int id;
    private String studentCode;
    private String fullName;
    private String className;
    private double average;

    /**
     * Constructor mặc định
     * 
     * Cách thức hoạt động:
     * 1. Khởi tạo một đối tượng Student mới với các giá trị mặc định
     * 2. Tất cả các thuộc tính sẽ có giá trị mặc định (0 cho int, null cho String, 0.0 cho double)
     * 3. Được sử dụng khi cần tạo một đối tượng Student trống để gán giá trị sau này
     * 
     * Ví dụ: Student student = new Student(); // Tạo một đối tượng Student trống
     */
    public Student() {}

    /**
     * Constructor đầy đủ tham số
     * 
     * Cách thức hoạt động:
     * 1. Khởi tạo một đối tượng Student mới với tất cả các thuộc tính được cung cấp
     * 2. Gán các giá trị tham số cho các thuộc tính tương ứng của đối tượng
     * 3. Được sử dụng khi cần tạo một đối tượng Student với tất cả thông tin đã biết
     * 
     * @param id ID của học sinh (số nguyên dương, duy nhất trong hệ thống)
     *           Ví dụ: 1, 2, 3, ...
     * @param studentCode Mã học sinh (chuỗi không null, duy nhất)
     *                   Dùng để nhận diện học sinh, ví dụ: "HS001", "HS002", "HS123"
     * @param fullName Họ tên đầy đủ của học sinh (chuỗi không null)
     *                 Bao gồm cả họ và tên, ví dụ: "Nguyễn Văn An", "Trần Thị Bình"
     * @param className Tên lớp của học sinh (chuỗi không null)
     *                 Ví dụ: "10A1", "10A2", "11B1", "12C3"
     * @param average Điểm trung bình của học sinh (số thực từ 0.0 đến 10.0)
     *                Ví dụ: 8.5, 7.2, 9.0, 6.8
     * 
     * Ví dụ: Student student = new Student(1, "HS001", "Nguyễn Văn An", "10A1", 8.5);
     */
    public Student(int id, String studentCode, String fullName, String className, double average) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.className = className;
        this.average = average;
    }

    // Getters and Setters
    /**
     * Lấy ID của học sinh
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính id
     * 2. ID là khóa chính duy nhất trong cơ sở dữ liệu
     * 3. Được sử dụng để xác định và truy vấn học sinh cụ thể
     * 
     * @return ID của học sinh (số nguyên dương nếu đã lưu trong DB, 0 nếu chưa gán)
     *         Ví dụ: 1, 2, 3, ...
     * 
     * Ví dụ: int studentId = student.getId(); // Trả về ID của học sinh, ví dụ: 123
     */
    public int getId() { return id; }
    /**
     * Thiết lập ID cho học sinh
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị id mới cho thuộc tính id của đối tượng
     * 2. Được sử dụng khi tạo mới học sinh hoặc cập nhật ID từ cơ sở dữ liệu
     * 3. ID phải là duy nhất trong hệ thống
     * 
     * @param id ID của học sinh (số nguyên dương, không được âm)
     *           Ví dụ: 1, 2, 3, ...
     * 
     * Ví dụ: student.setId(123); // Thiết lập ID của học sinh thành 123
     */
    public void setId(int id) { this.id = id; }

    /**
     * Lấy mã học sinh
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính studentCode
     * 2. studentCode là mã định danh duy nhất của học sinh trong hệ thống
     * 3. Được sử dụng để nhận diện học sinh trong các thao tác tìm kiếm và hiển thị
     * 
     * @return Mã học sinh (chuỗi không null nếu đã gán)
     *         Ví dụ: "HS001", "HS002", "HS123"
     * 
     * Ví dụ: String studentCode = student.getStudentCode(); // Trả về mã học sinh, ví dụ: "HS001"
     */
    public String getStudentCode() { return studentCode; }
    /**
     * Thiết lập mã học sinh
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị studentCode mới cho thuộc tính studentCode của đối tượng
     * 2. Được sử dụng khi tạo mới học sinh hoặc cập nhật mã học sinh
     * 3. studentCode phải là duy nhất trong hệ thống
     * 
     * @param studentCode Mã học sinh (chuỗi không null, không rỗng, duy nhất)
     *                   Ví dụ: "HS001", "HS002", "HS123"
     * 
     * Ví dụ: student.setStudentCode("HS001"); // Thiết lập mã học sinh thành "HS001"
     */
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    /**
     * Lấy họ tên đầy đủ của học sinh
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính fullName
     * 2. fullName bao gồm cả họ và tên của học sinh
     * 3. Được hiển thị trong giao diện người dùng và báo cáo
     * 
     * @return Họ tên đầy đủ của học sinh (chuỗi không null nếu đã gán)
     *         Ví dụ: "Nguyễn Văn An", "Trần Thị Bình"
     * 
     * Ví dụ: String fullName = student.getFullName(); // Trả về họ tên, ví dụ: "Nguyễn Văn An"
     */
    public String getFullName() { return fullName; }
    /**
     * Thiết lập họ tên đầy đủ cho học sinh
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị fullName mới cho thuộc tính fullName của đối tượng
     * 2. Được sử dụng khi tạo mới học sinh hoặc cập nhật họ tên
     * 3. Nên bao gồm cả họ và tên của học sinh
     * 
     * @param fullName Họ tên đầy đủ của học sinh (chuỗi không null, không rỗng)
     *                 Ví dụ: "Nguyễn Văn An", "Trần Thị Bình"
     * 
     * Ví dụ: student.setFullName("Nguyễn Văn Bảo"); // Thiết lập họ tên thành "Nguyễn Văn Bảo"
     */
    public void setFullName(String fullName) { this.fullName = fullName; }

    /**
     * Lấy tên lớp của học sinh
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính className
     * 2. className đại diện cho lớp học mà học sinh đang theo học
     * 3. Được sử dụng để phân loại học sinh theo lớp và tạo báo cáo
     * 
     * @return Tên lớp của học sinh (chuỗi không null nếu đã gán)
     *         Ví dụ: "10A1", "10A2", "11B1", "12C3"
     * 
     * Ví dụ: String className = student.getClassName(); // Trả về tên lớp, ví dụ: "10A1"
     */
    public String getClassName() { return className; }
    /**
     * Thiết lập tên lớp cho học sinh
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị className mới cho thuộc tính className của đối tượng
     * 2. Được sử dụng khi tạo mới học sinh hoặc cập nhật lớp học
     * 3. Nên tuân theo quy ước đặt tên lớp của nhà trường
     * 
     * @param className Tên lớp của học sinh (chuỗi không null, không rỗng)
     *                 Ví dụ: "10A1", "10A2", "11B1", "12C3"
     * 
     * Ví dụ: student.setClassName("10A2"); // Thiết lập tên lớp thành "10A2"
     */
    public void setClassName(String className) { this.className = className; }

    /**
     * Lấy điểm trung bình của học sinh
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính average
     * 2. average là điểm trung bình tổng kết của học sinh dựa trên các điểm thành phần
     * 3. Được sử dụng để đánh giá kết quả học tập và xếp loại học sinh
     * 
     * @return Điểm trung bình của học sinh (số thực từ 0.0 đến 10.0)
     *         Ví dụ: 8.5, 7.2, 9.0, 6.8
     * 
     * Ví dụ: double average = student.getAverage(); // Trả về điểm trung bình, ví dụ: 8.5
     */
    public double getAverage() { return average; }
    /**
     * Thiết lập điểm trung bình cho học sinh
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị average mới cho thuộc tính average của đối tượng
     * 2. Được sử dụng khi cập nhật điểm trung bình sau khi tính toán
     * 3. Giá trị nên nằm trong khoảng từ 0.0 đến 10.0
     * 
     * @param average Điểm trung bình của học sinh (số thực từ 0.0 đến 10.0)
     *                Ví dụ: 8.5, 7.2, 9.0, 6.8
     * 
     * Ví dụ: student.setAverage(8.7); // Thiết lập điểm trung bình thành 8.7
     */
    public void setAverage(double average) { this.average = average; }
}