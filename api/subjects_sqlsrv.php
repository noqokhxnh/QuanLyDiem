<?php
// subjects.php - Quản lý môn học với SQL Server
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
                $query = "SELECT * FROM subjects WHERE id = ?";
                $params = array($_GET['id']);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false && sqlsrv_has_rows($stmt)) {
                    $subject = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC);
                    // Convert datetime to string
                    if (isset($subject['created_date'])) {
                        $subject['created_date'] = $subject['created_date']->format('Y-m-d H:i:s');
                    }
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
            } catch (Exception $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else {
            // Lấy danh sách tất cả môn học
            try {
                $query = "SELECT * FROM subjects ORDER BY created_date DESC";
                $stmt = sqlsrv_query($conn, $query);
                
                $subjects = array();
                while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC)) {
                    // Convert datetime to string
                    if (isset($row['created_date'])) {
                        $row['created_date'] = $row['created_date']->format('Y-m-d H:i:s');
                    }
                    $subjects[] = $row;
                }
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $subjects
                ));
            } catch (Exception $e) {
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
                $query = "INSERT INTO subjects (subject_name, subject_code) VALUES (?, ?)";
                $params = array($data->subject_name, $data->subject_code);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
                    // Lấy ID vừa được tạo
                    $getIdQuery = "SELECT SCOPE_IDENTITY() as id";
                    $getIdStmt = sqlsrv_query($conn, $getIdQuery);
                    $newIdRow = sqlsrv_fetch_array($getIdStmt, SQLSRV_FETCH_ASSOC);
                    $newId = $newIdRow['id'];
                    
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Thêm môn học thành công",
                        "data" => array("id" => $newId)
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể thêm môn học"
                    ));
                }
            } catch (Exception $e) {
                if (strpos($e->getMessage(), 'duplicate key') !== false) {
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
                $query = "UPDATE subjects SET subject_name = ?, subject_code = ? WHERE id = ?";
                $params = array($data->subject_name, $data->subject_code, $data->id);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
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
            } catch (Exception $e) {
                if (strpos($e->getMessage(), 'duplicate key') !== false) {
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
                $query = "DELETE FROM subjects WHERE id = ?";
                $params = array($_GET['id']);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
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
            } catch (Exception $e) {
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

// Đóng kết nối
sqlsrv_close($conn);
?>