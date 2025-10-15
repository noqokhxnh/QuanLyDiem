<?php
// students.php - Quản lý học sinh với SQL Server
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
                $query = "SELECT * FROM students WHERE id = ?";
                $params = array($_GET['id']);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false && sqlsrv_has_rows($stmt)) {
                    $student = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC);
                    // Convert datetime to string
                    if (isset($student['birth_date']) && $student['birth_date'] instanceof DateTime) {
                        $student['birth_date'] = $student['birth_date']->format('Y-m-d');
                    }
                    if (isset($student['created_date'])) {
                        $student['created_date'] = $student['created_date']->format('Y-m-d H:i:s');
                    }
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
            } catch (Exception $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else {
            // Lấy danh sách tất cả học sinh
            try {
                $query = "SELECT * FROM students ORDER BY created_date DESC";
                $stmt = sqlsrv_query($conn, $query);
                
                $students = array();
                while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC)) {
                    // Convert datetime to string
                    if (isset($row['birth_date']) && $row['birth_date'] instanceof DateTime) {
                        $row['birth_date'] = $row['birth_date']->format('Y-m-d');
                    }
                    if (isset($row['created_date'])) {
                        $row['created_date'] = $row['created_date']->format('Y-m-d H:i:s');
                    }
                    $students[] = $row;
                }
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $students
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
        // Thêm học sinh mới
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->user_id) && !empty($data->student_code) && !empty($data->class_name)) {
            try {
                $query = "INSERT INTO students (user_id, student_code, class_name, birth_date) VALUES (?, ?, ?, ?)";
                $params = array($data->user_id, $data->student_code, $data->class_name, $data->birth_date);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
                    // Lấy ID vừa được tạo
                    $getIdQuery = "SELECT SCOPE_IDENTITY() as id";
                    $getIdStmt = sqlsrv_query($conn, $getIdQuery);
                    $newIdRow = sqlsrv_fetch_array($getIdStmt, SQLSRV_FETCH_ASSOC);
                    $newId = $newIdRow['id'];
                    
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Thêm học sinh thành công",
                        "data" => array("id" => $newId)
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể thêm học sinh"
                    ));
                }
            } catch (Exception $e) {
                if (strpos($e->getMessage(), 'duplicate key') !== false) {
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
        
        if (!empty($data->id) && !empty($data->user_id) && !empty($data->student_code) && !empty($data->class_name)) {
            try {
                $query = "UPDATE students SET user_id = ?, student_code = ?, class_name = ?, birth_date = ? WHERE id = ?";
                $params = array($data->user_id, $data->student_code, $data->class_name, $data->birth_date, $data->id);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
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
            } catch (Exception $e) {
                if (strpos($e->getMessage(), 'duplicate key') !== false) {
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
                $query = "DELETE FROM students WHERE id = ?";
                $params = array($_GET['id']);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
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
            } catch (Exception $e) {
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

// Đóng kết nối
sqlsrv_close($conn);
?>