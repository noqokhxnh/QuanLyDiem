<?php
// students.php - Quản lý học sinh
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type");

include_once 'config.php';

$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        // Lấy danh sách học sinh hoặc thông tin học sinh theo ID
        if (isset($_GET['id'])) {
            // Lấy thông tin học sinh theo ID
            try {
                $query = "SELECT * FROM students WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $_GET['id']);
                $stmt->execute();
                
                if ($stmt->rowCount() > 0) {
                    $student = $stmt->fetch(PDO::FETCH_ASSOC);
                    echo json_encode(array(
                        "status" => "success",
                        "data" => $student
                    ));
                } else {
                    http_response_code(404);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không tìm thấy học sinh"
                    ));
                }
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else if (isset($_GET['user_id'])) {
            // Lấy thông tin học sinh theo user_id
            try {
                $query = "SELECT * FROM students WHERE user_id = :user_id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':user_id', $_GET['user_id']);
                $stmt->execute();
                
                if ($stmt->rowCount() > 0) {
                    $student = $stmt->fetch(PDO::FETCH_ASSOC);
                    echo json_encode(array(
                        "status" => "success",
                        "data" => $student
                    ));
                } else {
                    echo json_encode(array(
                        "status" => "success",
                        "data" => []
                    ));
                }
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else {
            // Lấy danh sách tất cả học sinh
            try {
                $query = "SELECT * FROM students";
                $stmt = $pdo->prepare($query);
                $stmt->execute();
                $students = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $students
                ));
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        }
        break;
        
    case 'POST':
        // Thêm học sinh mới
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->user_id) && !empty($data->student_code) && !empty($data->class_name) && !empty($data->birth_date)) {
            try {
                $query = "INSERT INTO students (user_id, student_code, class_name, birth_date) VALUES (:user_id, :student_code, :class_name, :birth_date)";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':user_id', $data->user_id);
                $stmt->bindParam(':student_code', $data->student_code);
                $stmt->bindParam(':class_name', $data->class_name);
                $stmt->bindParam(':birth_date', $data->birth_date);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Thêm học sinh thành công",
                        "data" => array("id" => $pdo->lastInsertId())
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể thêm học sinh"
                    ));
                }
            } catch (PDOException $e) {
                if ($e->getCode() == 23000) {
                    http_response_code(409);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Mã học sinh đã tồn tại"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Lỗi khi thêm học sinh: " . $e->getMessage()
                    ));
                }
            }
        } else {
            http_response_code(400);
            echo json_encode(array(
                "status" => "error",
                "message" => "Vui lòng cung đầy đủ thông tin"
            ));
        }
        break;
        
    case 'PUT':
        // Cập nhật thông tin học sinh
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->id) && !empty($data->user_id) && !empty($data->student_code) && !empty($data->class_name) && !empty($data->birth_date)) {
            try {
                $query = "UPDATE students SET user_id = :user_id, student_code = :student_code, class_name = :class_name, birth_date = :birth_date WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $data->id);
                $stmt->bindParam(':user_id', $data->user_id);
                $stmt->bindParam(':student_code', $data->student_code);
                $stmt->bindParam(':class_name', $data->class_name);
                $stmt->bindParam(':birth_date', $data->birth_date);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Cập nhật học sinh thành công"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể cập nhật học sinh"
                    ));
                }
            } catch (PDOException $e) {
                if ($e->getCode() == 23000) {
                    http_response_code(409);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Mã học sinh đã tồn tại"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Lỗi khi cập nhật học sinh: " . $e->getMessage()
                    ));
                }
            }
        } else {
            http_response_code(400);
            echo json_encode(array(
                "status" => "error",
                "message" => "Vui lòng cung đầy đủ thông tin"
            ));
        }
        break;
        
    case 'DELETE':
        // Xóa học sinh
        if (isset($_GET['id'])) {
            try {
                // Xóa học sinh (sẽ tự động xóa các bản ghi liên quan do ràng buộc khóa ngoại)
                $query = "DELETE FROM students WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $_GET['id']);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Xóa học sinh thành công"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể xóa học sinh"
                    ));
                }
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi xóa học sinh: " . $e->getMessage()
                ));
            }
        } else {
            http_response_code(400);
            echo json_encode(array(
                "status" => "error",
                "message" => "Vui lòng cung cấp ID học sinh"
            ));
        }
        break;
        
    default:
        http_response_code(405);
        echo json_encode(array(
            "status" => "error",
            "message" => "Phương thức không được hỗ trợ"
        ));
        break;
}
?>