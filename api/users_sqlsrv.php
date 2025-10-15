<?php
// users.php - Quản lý người dùng với SQL Server
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type");

include_once 'config.php';

$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        // Lấy danh sách người dùng hoặc thông tin người dùng theo ID
        if (isset($_GET['id'])) {
            // Lấy thông tin người dùng theo ID
            try {
                $query = "SELECT * FROM users WHERE id = ?";
                $params = array($_GET['id']);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false && sqlsrv_has_rows($stmt)) {
                    $user = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC);
                    // Convert datetime to string
                    if (isset($user['created_date'])) {
                        $user['created_date'] = $user['created_date']->format('Y-m-d H:i:s');
                    }
                    echo json_encode(array(
                        "status" => "success",
                        "data" => $user
                    ));
                } else {
                    http_response_code(404);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không tìm thấy người dùng"
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
            // Lấy danh sách tất cả người dùng
            try {
                $query = "SELECT * FROM users ORDER BY created_date DESC";
                $stmt = sqlsrv_query($conn, $query);
                
                $users = array();
                while ($row = sqlsrv_fetch_array($stmt, SQLSRV_FETCH_ASSOC)) {
                    // Convert datetime to string
                    if (isset($row['created_date'])) {
                        $row['created_date'] = $row['created_date']->format('Y-m-d H:i:s');
                    }
                    $users[] = $row;
                }
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $users
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
        // Thêm người dùng mới
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->username) && !empty($data->password) && !empty($data->role) && !empty($data->full_name)) {
            try {
                $query = "INSERT INTO users (username, password, role, full_name) VALUES (?, ?, ?, ?)";
                $params = array($data->username, $data->password, $data->role, $data->full_name);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
                    // Lấy ID vừa được tạo
                    $getIdQuery = "SELECT SCOPE_IDENTITY() as id";
                    $getIdStmt = sqlsrv_query($conn, $getIdQuery);
                    $newIdRow = sqlsrv_fetch_array($getIdStmt, SQLSRV_FETCH_ASSOC);
                    $newId = $newIdRow['id'];
                    
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Thêm người dùng thành công",
                        "data" => array("id" => $newId)
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể thêm người dùng"
                    ));
                }
            } catch (Exception $e) {
                if (strpos($e->getMessage(), 'duplicate key') !== false) {
                    http_response_code(409);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Tên đăng nhập đã tồn tại"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Lỗi khi thêm người dùng: " . $e->getMessage()
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
        // Cập nhật thông tin người dùng
        $data = json_decode(file_get_contents("php://input"));
        
        if (!empty($data->id) && !empty($data->username) && !empty($data->password) && !empty($data->role) && !empty($data->full_name)) {
            try {
                $query = "UPDATE users SET username = ?, password = ?, role = ?, full_name = ? WHERE id = ?";
                $params = array($data->username, $data->password, $data->role, $data->full_name, $data->id);
                $stmt = sqlsrv_query($conn, $query, $params);
                
                if ($stmt !== false) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Cập nhật người dùng thành công"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể cập nhật người dùng"
                    ));
                }
            } catch (Exception $e) {
                if (strpos($e->getMessage(), 'duplicate key') !== false) {
                    http_response_code(409);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Tên đăng nhập đã tồn tại"
                    ));
                } else {
                    http_response_code(500);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Lỗi khi cập nhật người dùng: " . $e->getMessage()
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
        // Xóa người dùng
        if (isset($_GET['id'])) {
            try {
                // Kiểm tra xem người dùng có phải là giáo viên không
                $checkQuery = "SELECT role FROM users WHERE id = ?";
                $checkParams = array($_GET['id']);
                $checkStmt = sqlsrv_query($conn, $checkQuery, $checkParams);
                
                if ($checkStmt !== false && sqlsrv_has_rows($checkStmt)) {
                    $user = sqlsrv_fetch_array($checkStmt, SQLSRV_FETCH_ASSOC);
                    
                    // Nếu là giáo viên, không cho phép xóa
                    if ($user['role'] == 1) {
                        http_response_code(403);
                        echo json_encode(array(
                            "status" => "error",
                            "message" => "Không thể xóa tài khoản giáo viên"
                        ));
                        exit();
                    }
                    
                    // Xóa học sinh và các bản ghi liên quan
                    $query = "DELETE FROM users WHERE id = ?";
                    $params = array($_GET['id']);
                    $stmt = sqlsrv_query($conn, $query, $params);
                    
                    if ($stmt !== false) {
                        echo json_encode(array(
                            "status" => "success",
                            "message" => "Xóa người dùng thành công"
                        ));
                    } else {
                        http_response_code(500);
                        echo json_encode(array(
                            "status" => "error",
                            "message" => "Không thể xóa người dùng"
                        ));
                    }
                } else {
                    http_response_code(404);
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không tìm thấy người dùng"
                    ));
                }
            } catch (Exception $e) {
                http_response_code(500);
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi xóa người dùng: " . $e->getMessage()
                ));
            }
        } else {
            http_response_code(400);
            echo json_encode(array(
                "status" => "error",
                "message" => "Vui lòng cung cấp ID người dùng"
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