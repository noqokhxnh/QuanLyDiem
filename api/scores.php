<?php
// scores.php - Quản lý điểm số
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type");

include_once 'config.php';

$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        // Lấy danh sách điểm hoặc thông tin điểm theo ID
        if (isset($_GET['id'])) {
            // Lấy thông tin điểm theo ID
            try {
                $query = "SELECT * FROM scores WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $_GET['id']);
                $stmt->execute();
                
                if ($stmt->rowCount() > 0) {
                    $score = $stmt->fetch(PDO::FETCH_ASSOC);
                    echo json_encode(array(
                        "status" => "success",
                        "data" => $score
                    ));
                } else {
                    http_response_code(404);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không tìm thấy điểm"
                    ));
                }
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else if (isset($_GET['student_id'])) {
            // Lấy danh sách điểm theo student_id
            try {
                $query = "SELECT * FROM scores WHERE student_id = :student_id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':student_id', $_GET['student_id']);
                $stmt->execute();
                $scores = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $scores
                ));
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else {
            // Lấy danh sách tất cả điểm
            try {
                $query = "SELECT * FROM scores";
                $stmt = $pdo->prepare($query);
                $stmt->execute();
                $scores = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $scores
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
        // Thêm điểm mới
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->student_id) && !empty($data->subject_id) && !empty($data->score_type) && !empty($data->score) && !empty($data->teacher_id)) {
            try {
                $query = "INSERT INTO scores (student_id, subject_id, score_type, score, teacher_id) VALUES (:student_id, :subject_id, :score_type, :score, :teacher_id)";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':student_id', $data->student_id);
                $stmt->bindParam(':subject_id', $data->subject_id);
                $stmt->bindParam(':score_type', $data->score_type);
                $stmt->bindParam(':score', $data->score);
                $stmt->bindParam(':teacher_id', $data->teacher_id);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Thêm điểm thành công",
                        "data" => array("id" => $pdo->lastInsertId())
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể thêm điểm"
                    ));
                }
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi thêm điểm: " . $e->getMessage()
                ));
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
        // Cập nhật thông tin điểm
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->id) && !empty($data->student_id) && !empty($data->subject_id) && !empty($data->score_type) && !empty($data->score) && !empty($data->teacher_id)) {
            try {
                $query = "UPDATE scores SET student_id = :student_id, subject_id = :subject_id, score_type = :score_type, score = :score, teacher_id = :teacher_id WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $data->id);
                $stmt->bindParam(':student_id', $data->student_id);
                $stmt->bindParam(':subject_id', $data->subject_id);
                $stmt->bindParam(':score_type', $data->score_type);
                $stmt->bindParam(':score', $data->score);
                $stmt->bindParam(':teacher_id', $data->teacher_id);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Cập nhật điểm thành công"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể cập nhật điểm"
                    ));
                }
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi cập nhật điểm: " . $e->getMessage()
                ));
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
        // Xóa điểm
        if (isset($_GET['id'])) {
            try {
                $query = "DELETE FROM scores WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $_GET['id']);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Xóa điểm thành công"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể xóa điểm"
                    ));
                }
            } catch (PDOException $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi xóa điểm: " . $e->getMessage()
                ));
            }
        } else {
            http_response_code(400);
            echo json_encode(array(
                "status" => "error",
                "message" => "Vui lòng cung cấp ID điểm"
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