<?php
// scores.php - Quản lý điểm với SQL Server
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
                $query = "SELECT * FROM scores WHERE id = ?";
                $params = array($_GET['id']);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false && sqlsrv_has_rows($stmt)) {
                    $score = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC);
                    // Convert datetime to string
                    if (isset($score['date_created'])) {
                        $score['date_created'] = $score['date_created']->format('Y-m-d H:i:s');
                    }
                    if (isset($score['created_date'])) {
                        $score['created_date'] = $score['created_date']->format('Y-m-d H:i:s');
                    }
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
            } catch (Exception $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else if (isset($_GET['student_id'])) {
            // Lấy danh sách điểm theo student_id
            try {
                $query = "SELECT * FROM scores WHERE student_id = ? ORDER BY date_created DESC";
                $params = array($_GET['student_id']);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                $scores = array();
                while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC)) {
                    // Convert datetime to string
                    if (isset($row['date_created'])) {
                        $row['date_created'] = $row['date_created']->format('Y-m-d H:i:s');
                    }
                    if (isset($row['created_date'])) {
                        $row['created_date'] = $row['created_date']->format('Y-m-d H:i:s');
                    }
                    $scores[] = $row;
                }
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $scores
                ));
            } catch (Exception $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else {
            // Lấy danh sách tất cả điểm
            try {
                $query = "SELECT * FROM scores ORDER BY date_created DESC";
                $stmt = sqlsrv_query($conn, $query);
                
                $scores = array();
                while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC)) {
                    // Convert datetime to string
                    if (isset($row['date_created'])) {
                        $row['date_created'] = $row['date_created']->format('Y-m-d H:i:s');
                    }
                    if (isset($row['created_date'])) {
                        $row['created_date'] = $row['created_date']->format('Y-m-d H:i:s');
                    }
                    $scores[] = $row;
                }
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $scores
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
        // Thêm điểm mới
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->student_id) && !empty($data->subject_id) && !empty($data->score_type) && !empty($data->score) && !empty($data->teacher_id)) {
            try {
                $query = "INSERT INTO scores (student_id, subject_id, score_type, score, teacher_id) VALUES (?, ?, ?, ?, ?)";
                $params = array($data->student_id, $data->subject_id, $data->score_type, $data->score, $data->teacher_id);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
                    // Lấy ID vừa được tạo
                    $getIdQuery = "SELECT SCOPE_IDENTITY() as id";
                    $getIdStmt = sqlsrv_query($conn, $getIdQuery);
                    $newIdRow = sqlsrv_fetch_array($getIdStmt, SQLSRV_FETCH_ASSOC);
                    $newId = $newIdRow['id'];
                    
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Thêm điểm thành công",
                        "data" => array("id" => $newId)
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể thêm điểm"
                    ));
                }
            } catch (Exception $e) {
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
                $query = "UPDATE scores SET student_id = ?, subject_id = ?, score_type = ?, score = ?, teacher_id = ? WHERE id = ?";
                $params = array($data->student_id, $data->subject_id, $data->score_type, $data->score, $data->teacher_id, $data->id);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
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
            } catch (Exception $e) {
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
                $query = "DELETE FROM scores WHERE id = ?";
                $params = array($_GET['id']);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
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
            } catch (Exception $e) {
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

// Đóng kết nối
sqlsrv_close($conn);
?>