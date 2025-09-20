<?php
// subjects.php - Quản lý môn học
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type");

include_once 'config.php';

$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        // Lấy danh sách môn học hoặc thông tin môn học theo ID
        if (isset($_GET['id'])) {
            // Lấy thông tin môn học theo ID
            try {
                $query = "SELECT * FROM subjects WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $_GET['id']);
                $stmt->execute();
                
                if ($stmt->rowCount() > 0) {
                    $subject = $stmt->fetch(PDO::FETCH_ASSOC);
                    echo json_encode(array(
                        "status" => "success",
                        "data" => $subject
                    ));
                } else {
                    http_response_code(404);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không tìm thấy môn học"
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
            // Lấy danh sách tất cả môn học
            try {
                $query = "SELECT * FROM subjects";
                $stmt = $pdo->prepare($query);
                $stmt->execute();
                $subjects = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $subjects
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
        // Thêm môn học mới
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->subject_name) && !empty($data->subject_code)) {
            try {
                $query = "INSERT INTO subjects (subject_name, subject_code) VALUES (:subject_name, :subject_code)";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':subject_name', $data->subject_name);
                $stmt->bindParam(':subject_code', $data->subject_code);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Thêm môn học thành công",
                        "data" => array("id" => $pdo->lastInsertId())
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể thêm môn học"
                    ));
                }
            } catch (PDOException $e) {
                if ($e->getCode() == 23000) {
                    http_response_code(409);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Mã môn học đã tồn tại"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Lỗi khi thêm môn học: " . $e->getMessage()
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
        // Cập nhật thông tin môn học
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->id) && !empty($data->subject_name) && !empty($data->subject_code)) {
            try {
                $query = "UPDATE subjects SET subject_name = :subject_name, subject_code = :subject_code WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $data->id);
                $stmt->bindParam(':subject_name', $data->subject_name);
                $stmt->bindParam(':subject_code', $data->subject_code);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Cập nhật môn học thành công"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể cập nhật môn học"
                    ));
                }
            } catch (PDOException $e) {
                if ($e->getCode() == 23000) {
                    http_response_code(409);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Mã môn học đã tồn tại"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Lỗi khi cập nhật môn học: " . $e->getMessage()
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
        // Xóa môn học
        if (isset($_GET['id'])) {
            try {
                // Xóa môn học (sẽ tự động xóa các bản ghi liên quan do ràng buộc khóa ngoại)
                $query = "DELETE FROM subjects WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $_GET['id']);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Xóa môn học thành công"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể xóa môn học"
                    ));
                }
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi xóa môn học: " . $e->getMessage()
                ));
            }
        } else {
            http_response_code(400);
            echo json_encode(array(
                "status" => "error",
                "message" => "Vui lòng cung cấp ID môn học"
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