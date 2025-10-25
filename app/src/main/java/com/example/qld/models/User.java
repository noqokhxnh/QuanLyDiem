package com.example.qld.models;

/**
 * Lớp đại diện cho người dùng trong hệ thống
 * Chứa thông tin cá nhân và vai trò của người dùng
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String role; // 'ADMIN', 'TEACHER', 'STUDENT'
    private int studentId; // Foreign key to Students table
    private String createdDate;

    /**
     * Constructor mặc định
     * 
     * Cách thức hoạt động:
     * 1. Khởi tạo một đối tượng User mới với các giá trị mặc định
     * 2. Tất cả các thuộc tính sẽ có giá trị mặc định (0 cho int, null cho String)
     * 3. Được sử dụng khi cần tạo một đối tượng User trống để gán giá trị sau này
     * 
     * Ví dụ: User user = new User(); // Tạo một đối tượng User trống
     */
    public User() {}

    /**
     * Constructor đầy đủ tham số
     * 
     * Cách thức hoạt động:
     * 1. Khởi tạo một đối tượng User mới với tất cả các thuộc tính được cung cấp
     * 2. Gán các giá trị tham số cho các thuộc tính tương ứng của đối tượng
     * 3. Được sử dụng khi cần tạo một đối tượng User với tất cả thông tin đã biết
     * 
     * @param id ID của người dùng (số nguyên dương, duy nhất trong hệ thống)
     *           Ví dụ: 1, 2, 3, ...
     * @param username Tên đăng nhập của người dùng (chuỗi không null, duy nhất)
     *                 Dùng để đăng nhập vào hệ thống, ví dụ: "admin", "teacher1", "student001"
     * @param password Mật khẩu của người dùng (chuỗi không null, đã được mã hóa)
     *                Dùng để xác thực người dùng khi đăng nhập, ví dụ: "hashed_password_123..."
     * @param fullName Họ tên đầy đủ của người dùng (chuỗi có thể null)
     *               Bao gồm cả họ và tên, ví dụ: "Nguyễn Văn An", "Trần Thị Bình"
     * @param role Vai trò của người dùng ('ADMIN', 'TEACHER', 'STUDENT')
     *             Xác định quyền hạn và chức năng mà người dùng có thể truy cập
     *             - 'ADMIN': Quản trị viên - có quyền cao nhất
     *             - 'TEACHER': Giáo viên - có quyền quản lý học sinh và điểm
     *             - 'STUDENT': Học sinh - có quyền xem điểm và thông tin cá nhân
     * @param studentId ID của học sinh liên kết với người dùng (nếu có, số nguyên dương hoặc 0)
     *                  Chỉ áp dụng cho người dùng có vai trò 'STUDENT'
     *                  Ví dụ: 1, 2, 3, ... (nếu là học sinh) hoặc 0 (nếu không phải học sinh)
     * @param createdDate Ngày tạo tài khoản (chuỗi có thể null)
     *                    Định dạng: "YYYY-MM-DD HH:MM:SS" hoặc null nếu chưa có
     *                    Ví dụ: "2023-10-15 14:30:00"
     * 
     * Ví dụ: User user = new User(1, "admin", "hashed_pass", "Quản Trị Viên", "ADMIN", 0, "2023-10-15 14:30:00");
     */
    public User(int id, String username, String password, String fullName, String role, int studentId, String createdDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.studentId = studentId;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    /**
     * Lấy ID của người dùng
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính id
     * 2. ID là khóa chính duy nhất trong cơ sở dữ liệu
     * 3. Được sử dụng để xác định và truy vấn người dùng cụ thể
     * 
     * @return ID của người dùng (số nguyên dương nếu đã lưu trong DB, 0 nếu chưa gán)
     *         Ví dụ: 1, 2, 3, ...
     * 
     * Ví dụ: int userId = user.getId(); // Trả về ID của người dùng, ví dụ: 123
     */
    public int getId() { return id; }
    /**
     * Thiết lập ID cho người dùng
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị id mới cho thuộc tính id của đối tượng
     * 2. Được sử dụng khi tạo mới người dùng hoặc cập nhật ID từ cơ sở dữ liệu
     * 3. ID phải là duy nhất trong hệ thống
     * 
     * @param id ID của người dùng (số nguyên dương, không được âm)
     *           Ví dụ: 1, 2, 3, ...
     * 
     * Ví dụ: user.setId(123); // Thiết lập ID của người dùng thành 123
     */
    public void setId(int id) { this.id = id; }

    /**
     * Lấy tên đăng nhập của người dùng
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính username
     * 2. Username là duy nhất trong hệ thống và được sử dụng để đăng nhập
     * 3. Không phân biệt chữ hoa/thường trong một số trường hợp
     * 
     * @return Tên đăng nhập của người dùng (chuỗi không null nếu đã gán)
     *         Ví dụ: "admin", "teacher1", "student001"
     * 
     * Ví dụ: String username = user.getUsername(); // Trả về tên đăng nhập, ví dụ: "admin"
     */
    public String getUsername() { return username; }
    /**
     * Thiết lập tên đăng nhập cho người dùng
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị username mới cho thuộc tính username của đối tượng
     * 2. Được sử dụng khi tạo mới người dùng hoặc cập nhật tên đăng nhập
     * 3. Username phải là duy nhất trong hệ thống
     * 
     * @param username Tên đăng nhập của người dùng (chuỗi không null, không rỗng)
     *                 Ví dụ: "admin", "teacher1", "student001"
     * 
     * Ví dụ: user.setUsername("newadmin"); // Thiết lập tên đăng nhập thành "newadmin"
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Lấy mật khẩu của người dùng
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính password
     * 2. Mật khẩu đã được mã hóa (hashed) để bảo mật
     * 3. Được sử dụng để xác thực người dùng khi đăng nhập
     * 
     * @return Mật khẩu của người dùng (chuỗi đã được mã hóa, có thể null nếu chưa gán)
     *         Ví dụ: "hashed_password_123...", "$2a$10$..."
     * 
     * Ví dụ: String password = user.getPassword(); // Trả về mật khẩu đã mã hóa
     */
    public String getPassword() { return password; }
    /**
     * Thiết lập mật khẩu cho người dùng
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị password mới cho thuộc tính password của đối tượng
     * 2. Mật khẩu nên được mã hóa trước khi gán để đảm bảo bảo mật
     * 3. Được sử dụng khi tạo mới người dùng hoặc cập nhật mật khẩu
     * 
     * @param password Mật khẩu của người dùng (chuỗi không null, nên được mã hóa trước)
     *                 Ví dụ: "hashed_password_123...", "$2a$10$..."
     * 
     * Ví dụ: user.setPassword("$2a$10$new_hashed_password"); // Thiết lập mật khẩu đã mã hóa
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Lấy vai trò của người dùng
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính role
     * 2. Role xác định quyền hạn và chức năng mà người dùng có thể truy cập
     * 3. Có ba giá trị hợp lệ: 'ADMIN', 'TEACHER', 'STUDENT'
     * 
     * @return Vai trò của người dùng ('ADMIN', 'TEACHER', 'STUDENT' hoặc null nếu chưa gán)
     *         - 'ADMIN': Quản trị viên - có quyền cao nhất
     *         - 'TEACHER': Giáo viên - có quyền quản lý học sinh và điểm
     *         - 'STUDENT': Học sinh - có quyền xem điểm và thông tin cá nhân
     * 
     * Ví dụ: String role = user.getRole(); // Trả về vai trò, ví dụ: "ADMIN"
     */
    public String getRole() { return role; }
    /**
     * Thiết lập vai trò cho người dùng
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị role mới cho thuộc tính role của đối tượng
     * 2. Được sử dụng khi tạo mới người dùng hoặc cập nhật vai trò
     * 3. Role phải là một trong ba giá trị hợp lệ: 'ADMIN', 'TEACHER', 'STUDENT'
     * 
     * @param role Vai trò của người dùng ('ADMIN', 'TEACHER', 'STUDENT')
     *             - 'ADMIN': Quản trị viên - có quyền cao nhất
     *             - 'TEACHER': Giáo viên - có quyền quản lý học sinh và điểm
     *             - 'STUDENT': Học sinh - có quyền xem điểm và thông tin cá nhân
     * 
     * Ví dụ: user.setRole("TEACHER"); // Thiết lập vai trò thành giáo viên
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Lấy ID học sinh liên kết với người dùng
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính studentId
     * 2. Chỉ áp dụng cho người dùng có vai trò 'STUDENT'
     * 3. Dùng để liên kết người dùng với hồ sơ học sinh tương ứng
     * 
     * @return ID học sinh liên kết (số nguyên dương nếu là học sinh, 0 nếu không phải học sinh)
     *         Ví dụ: 1, 2, 3, ... (nếu là học sinh) hoặc 0 (nếu không phải học sinh)
     * 
     * Ví dụ: int studentId = user.getStudentId(); // Trả về ID học sinh liên kết, ví dụ: 456
     */
    public int getStudentId() { return studentId; }
    /**
     * Thiết lập ID học sinh liên kết với người dùng
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị studentId mới cho thuộc tính studentId của đối tượng
     * 2. Chỉ áp dụng cho người dùng có vai trò 'STUDENT'
     * 3. Dùng để liên kết người dùng với hồ sơ học sinh tương ứng
     * 
     * @param studentId ID học sinh liên kết (số nguyên dương nếu là học sinh, 0 nếu không phải học sinh)
     *                  Ví dụ: 1, 2, 3, ... (nếu là học sinh) hoặc 0 (nếu không phải học sinh)
     * 
     * Ví dụ: user.setStudentId(456); // Thiết lập ID học sinh liên kết thành 456
     */
    public void setStudentId(int studentId) { this.studentId = studentId; }

    /**
     * Lấy họ tên đầy đủ của người dùng
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính fullName
     * 2. Bao gồm cả họ và tên của người dùng
     * 3. Được hiển thị trong giao diện người dùng
     * 
     * @return Họ tên đầy đủ của người dùng (chuỗi có thể null nếu chưa gán)
     *         Ví dụ: "Nguyễn Văn An", "Trần Thị Bình"
     * 
     * Ví dụ: String fullName = user.getFullName(); // Trả về họ tên, ví dụ: "Nguyễn Văn An"
     */
    public String getFullName() { return fullName; }
    /**
     * Thiết lập họ tên đầy đủ cho người dùng
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị fullName mới cho thuộc tính fullName của đối tượng
     * 2. Được sử dụng khi tạo mới người dùng hoặc cập nhật họ tên
     * 3. Nên bao gồm cả họ và tên của người dùng
     * 
     * @param fullName Họ tên đầy đủ của người dùng (chuỗi có thể null)
     *                 Ví dụ: "Nguyễn Văn An", "Trần Thị Bình"
     * 
     * Ví dụ: user.setFullName("Nguyễn Văn Bảo"); // Thiết lập họ tên thành "Nguyễn Văn Bảo"
     */
    public void setFullName(String fullName) { this.fullName = fullName; }

    /**
     * Lấy ngày tạo tài khoản
     * 
     * Cách thức hoạt động:
     * 1. Trả về giá trị của thuộc tính createdDate
     * 2. Đại diện cho thời điểm tài khoản được tạo
     * 3. Được sử dụng để theo dõi lịch sử tài khoản
     * 
     * @return Ngày tạo tài khoản (chuỗi có thể null nếu chưa gán)
     *         Định dạng: "YYYY-MM-DD HH:MM:SS" hoặc null
     *         Ví dụ: "2023-10-15 14:30:00"
     * 
     * Ví dụ: String createdDate = user.getCreatedDate(); // Trả về ngày tạo, ví dụ: "2023-10-15 14:30:00"
     */
    public String getCreatedDate() { return createdDate; }
    /**
     * Thiết lập ngày tạo tài khoản
     * 
     * Cách thức hoạt động:
     * 1. Gán giá trị createdDate mới cho thuộc tính createdDate của đối tượng
     * 2. Được sử dụng khi tạo mới tài khoản hoặc cập nhật thời gian tạo
     * 3. Nên tuân theo định dạng chuẩn để dễ xử lý
     * 
     * @param createdDate Ngày tạo tài khoản (chuỗi có thể null)
     *                    Định dạng: "YYYY-MM-DD HH:MM:SS" hoặc null
     *                    Ví dụ: "2023-10-15 14:30:00"
     * 
     * Ví dụ: user.setCreatedDate("2023-10-15 14:30:00"); // Thiết lập ngày tạo thành "2023-10-15 14:30:00"
     */
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}