package com.example.qld.models;

/**
 * Lớp đại diện cho điểm số trong hệ thống
 * Chứa thông tin về học sinh, môn học, loại điểm và điểm số
 */
public class Score {
    private int id;
    private int studentId;
    private int subjectId;
    private String scoreType; // 'mieng', '15phut', '1tiet', 'hocky'
    private double score;
    private String dateCreated;
    private int teacherId;

    /**
     * Constructor mặc định
     * 
     * Cách thức hoạt động:
     * 1. Khởi tạo một đối tượng Score mới với các giá trị mặc định
     * 2. Tất cả các thuộc tính sẽ có giá trị mặc định (0 cho int, null cho String, 0.0 cho double)
     * 3. Được sử dụng khi cần tạo một đối tượng Score trống để gán giá trị sau này
     * 
     * Ví dụ: Score score = new Score(); // Tạo một đối tượng Score trống
     */
    public Score() {}

    /**
     * Constructor đầy đủ tham số
     * 
     * Cách thức hoạt động:
     * 1. Khởi tạo một đối tượng Score mới với tất cả các thuộc tính được cung cấp
     * 2. Gán các giá trị tham số cho các thuộc tính tương ứng của đối tượng
     * 3. Được sử dụng khi cần tạo một đối tượng Score với tất cả thông tin đã biết
     * 
     * @param id ID của điểm số (số nguyên dương, duy nhất trong hệ thống)
     *           Ví dụ: 1, 2, 3, ...
     * @param studentId ID của học sinh (số nguyên dương, tham chiếu đến bảng Students)
     *                  Ví dụ: 1, 2, 3, ... (ID của học sinh mà điểm này thuộc về)
     * @param subjectId ID của môn học (số nguyên dương, tham chiếu đến bảng Subjects)
     *                  Ví dụ: 1, 2, 3, ... (ID của môn học mà điểm này thuộc về)
     * @param scoreType Loại điểm ('mieng', '15phut', '1tiet', 'hocky')
     *                  - 'mieng': Điểm miệng
     *                  - '15phut': Kiểm tra 15 phút
     *                  - '1tiet': Kiểm tra 1 tiết
     *                  - 'hocky': Thi học kỳ
     * @param score Điểm số (số thực từ 0.0 đến 10.0)
     *              Ví dụ: 8.5, 7.0, 9.2, 6.8
     * @param dateCreated Ngày tạo điểm (chuỗi có thể null)
     *                    Định dạng: "YYYY-MM-DD HH:MM:SS" hoặc null nếu chưa có
     *                    Ví dụ: "2023-10-15 14:30:00"
     * @param teacherId ID của giáo viên tạo điểm (số nguyên dương, tham chiếu đến bảng Users)
     *                  Ví dụ: 1, 2, 3, ... (ID của giáo viên đã nhập điểm này)
     * 
     * Ví dụ: Score score = new Score(1, 123, 456, "mieng", 8.5, "2023-10-15 14:30:00", 789);
     */
    public Score(int id, int studentId, int subjectId, String scoreType, double score, String dateCreated, int teacherId) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.scoreType = scoreType;
        this.score = score;
        this.dateCreated = dateCreated;
        this.teacherId = teacherId;
    }

    // Getters and Setters
    /**
     * Lấy ID của điểm số
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính id
     * 2. ID là khóa chính duy nhất trong cơ sở dữ liệu
     * 3. Được sử dụng để xác định và truy vấn điểm số cụ thể
     * 
     * @return ID của điểm số (số nguyên dương nếu đã lưu trong DB, 0 nếu chưa gán)
     *         Ví dụ: 1, 2, 3, ...
     * 
     * Ví dụ: int scoreId = score.getId(); // Trả về ID của điểm số, ví dụ: 123
     */
    public int getId() { return id; }
    /**
     * Thiết lập ID cho điểm số
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị id mới cho thuộc tính id của đối tượng
     * 2. Được sử dụng khi tạo mới điểm số hoặc cập nhật ID từ cơ sở dữ liệu
     * 3. ID phải là duy nhất trong hệ thống
     * 
     * @param id ID của điểm số (số nguyên dương, không được âm)
     *           Ví dụ: 1, 2, 3, ...
     * 
     * Ví dụ: score.setId(123); // Thiết lập ID của điểm số thành 123
     */
    public void setId(int id) { this.id = id; }

    /**
     * Lấy ID của học sinh
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính studentId
     * 2. studentId là khóa ngoại tham chiếu đến bảng Students
     * 3. Được sử dụng để liên kết điểm số với học sinh tương ứng
     * 
     * @return ID của học sinh (số nguyên dương nếu đã gán, 0 nếu chưa gán)
     *         Ví dụ: 1, 2, 3, ... (ID của học sinh mà điểm này thuộc về)
     * 
     * Ví dụ: int studentId = score.getStudentId(); // Trả về ID học sinh, ví dụ: 456
     */
    public int getStudentId() { return studentId; }
    /**
     * Thiết lập ID cho học sinh
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị studentId mới cho thuộc tính studentId của đối tượng
     * 2. Được sử dụng khi tạo mới điểm số hoặc cập nhật ID học sinh
     * 3. studentId phải là ID hợp lệ của học sinh trong bảng Students
     * 
     * @param studentId ID của học sinh (số nguyên dương, tham chiếu đến bảng Students)
     *                  Ví dụ: 1, 2, 3, ... (ID của học sinh mà điểm này thuộc về)
     * 
     * Ví dụ: score.setStudentId(456); // Thiết lập ID học sinh thành 456
     */
    public void setStudentId(int studentId) { this.studentId = studentId; }

    /**
     * Lấy ID của môn học
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính subjectId
     * 2. subjectId là khóa ngoại tham chiếu đến bảng Subjects
     * 3. Được sử dụng để liên kết điểm số với môn học tương ứng
     * 
     * @return ID của môn học (số nguyên dương nếu đã gán, 0 nếu chưa gán)
     *         Ví dụ: 1, 2, 3, ... (ID của môn học mà điểm này thuộc về)
     * 
     * Ví dụ: int subjectId = score.getSubjectId(); // Trả về ID môn học, ví dụ: 789
     */
    public int getSubjectId() { return subjectId; }
    /**
     * Thiết lập ID cho môn học
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị subjectId mới cho thuộc tính subjectId của đối tượng
     * 2. Được sử dụng khi tạo mới điểm số hoặc cập nhật ID môn học
     * 3. subjectId phải là ID hợp lệ của môn học trong bảng Subjects
     * 
     * @param subjectId ID của môn học (số nguyên dương, tham chiếu đến bảng Subjects)
     *                  Ví dụ: 1, 2, 3, ... (ID của môn học mà điểm này thuộc về)
     * 
     * Ví dụ: score.setSubjectId(789); // Thiết lập ID môn học thành 789
     */
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    /**
     * Lấy loại điểm
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính scoreType
     * 2. scoreType xác định loại hình đánh giá mà điểm số này thuộc về
     * 3. Có bốn giá trị hợp lệ: 'mieng', '15phut', '1tiet', 'hocky'
     * 
     * @return Loại điểm ('mieng', '15phut', '1tiet', 'hocky' hoặc null nếu chưa gán)
     *         - 'mieng': Điểm miệng
     *         - '15phut': Kiểm tra 15 phút
     *         - '1tiet': Kiểm tra 1 tiết
     *         - 'hocky': Thi học kỳ
     * 
     * Ví dụ: String scoreType = score.getScoreType(); // Trả về loại điểm, ví dụ: "mieng"
     */
    public String getScoreType() { return scoreType; }
    /**
     * Thiết lập loại điểm
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị scoreType mới cho thuộc tính scoreType của đối tượng
     * 2. Được sử dụng khi tạo mới điểm số hoặc cập nhật loại điểm
     * 3. scoreType phải là một trong bốn giá trị hợp lệ: 'mieng', '15phut', '1tiet', 'hocky'
     * 
     * @param scoreType Loại điểm ('mieng', '15phut', '1tiet', 'hocky')
     *                  - 'mieng': Điểm miệng
     *                  - '15phut': Kiểm tra 15 phút
     *                  - '1tiet': Kiểm tra 1 tiết
     *                  - 'hocky': Thi học kỳ
     * 
     * Ví dụ: score.setScoreType("15phut"); // Thiết lập loại điểm thành kiểm tra 15 phút
     */
    public void setScoreType(String scoreType) { this.scoreType = scoreType; }

    /**
     * Lấy điểm số
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính score
     * 2. score là giá trị điểm số thực tế mà học sinh đạt được
     * 3. Được sử dụng để tính toán điểm trung bình và xếp loại học sinh
     * 
     * @return Điểm số (số thực từ 0.0 đến 10.0 nếu đã gán)
     *         Ví dụ: 8.5, 7.0, 9.2, 6.8
     * 
     * Ví dụ: double scoreValue = score.getScore(); // Trả về điểm số, ví dụ: 8.5
     */
    public double getScore() { return score; }
    /**
     * Thiết lập điểm số
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị score mới cho thuộc tính score của đối tượng
     * 2. Được sử dụng khi tạo mới điểm số hoặc cập nhật điểm số
     * 3. Giá trị nên nằm trong khoảng từ 0.0 đến 10.0
     * 
     * @param score Điểm số (số thực từ 0.0 đến 10.0)
     *              Ví dụ: 8.5, 7.0, 9.2, 6.8
     * 
     * Ví dụ: score.setScore(8.7); // Thiết lập điểm số thành 8.7
     */
    public void setScore(double score) { this.score = score; }

    /**
     * Lấy ngày tạo điểm
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính dateCreated
     * 2. dateCreated đại diện cho thời điểm điểm số được tạo
     * 3. Được sử dụng để theo dõi lịch sử nhập điểm và sắp xếp theo thời gian
     * 
     * @return Ngày tạo điểm (chuỗi có thể null nếu chưa gán)
     *         Định dạng: "YYYY-MM-DD HH:MM:SS" hoặc null
     *         Ví dụ: "2023-10-15 14:30:00"
     * 
     * Ví dụ: String dateCreated = score.getDateCreated(); // Trả về ngày tạo, ví dụ: "2023-10-15 14:30:00"
     */
    public String getDateCreated() { return dateCreated; }
    /**
     * Thiết lập ngày tạo điểm
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị dateCreated mới cho thuộc tính dateCreated của đối tượng
     * 2. Được sử dụng khi tạo mới điểm số hoặc cập nhật thời gian tạo
     * 3. Nên tuân theo định dạng chuẩn để dễ xử lý
     * 
     * @param dateCreated Ngày tạo điểm (chuỗi có thể null)
     *                    Định dạng: "YYYY-MM-DD HH:MM:SS" hoặc null
     *                    Ví dụ: "2023-10-15 14:30:00"
     * 
     * Ví dụ: score.setDateCreated("2023-10-15 14:30:00"); // Thiết lập ngày tạo thành "2023-10-15 14:30:00"
     */
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }

    /**
     * Lấy ID của giáo viên tạo điểm
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính teacherId
     * 2. teacherId là khóa ngoại tham chiếu đến bảng Users
     * 3. Được sử dụng để liên kết điểm số với giáo viên đã nhập điểm
     * 
     * @return ID của giáo viên tạo điểm (số nguyên dương nếu đã gán, 0 nếu chưa gán)
     *         Ví dụ: 1, 2, 3, ... (ID của giáo viên đã nhập điểm này)
     * 
     * Ví dụ: int teacherId = score.getTeacherId(); // Trả về ID giáo viên, ví dụ: 789
     */
    public int getTeacherId() { return teacherId; }
    /**
     * Thiết lập ID cho giáo viên tạo điểm
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị teacherId mới cho thuộc tính teacherId của đối tượng
     * 2. Được sử dụng khi tạo mới điểm số hoặc cập nhật ID giáo viên
     * 3. teacherId phải là ID hợp lệ của giáo viên trong bảng Users
     * 
     * @param teacherId ID của giáo viên tạo điểm (số nguyên dương, tham chiếu đến bảng Users)
     *                  Ví dụ: 1, 2, 3, ... (ID của giáo viên đã nhập điểm này)
     * 
     * Ví dụ: score.setTeacherId(789); // Thiết lập ID giáo viên thành 789
     */
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
}