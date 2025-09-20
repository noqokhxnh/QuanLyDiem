<?php
// api/users.php
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
                $query = "SELECT * FROM users WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $_GET['id']);
                $stmt->execute();
                
                if ($stmt->rowCount() > 0) {
                    $user = $stmt->fetch(PDO::FETCH_ASSOC);
                    echo json_encode(array(
                        "status" => "success",
                        "data" => $user
                    ));
                } else {
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không tìm thấy người dùng"
                    ));
                }
            } catch (PDOException $e) {
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi truy vấn database: " . $e->getMessage()
                ));
            }
        } else {
            // Lấy danh sách tất cả người dùng
            try {
                $query = "SELECT * FROM users";
                $stmt = $pdo->prepare($query);
                $stmt->execute();
                $users = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                echo json_encode(array(
                    "status" => "success",
                    "data" => $users
                ));
            } catch (PDOException $e) {
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
                $query = "INSERT INTO users (username, password, role, full_name) VALUES (:username, :password, :role, :full_name)";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':username', $data->username);
                $stmt->bindParam(':password', $data->password);
                $stmt->bindParam(':role', $data->role);
                $stmt->bindParam(':full_name', $data->full_name);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Thêm người dùng thành công",
                        "data" => array("id" => $pdo->lastInsertId())
                    ));
                } else {
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể thêm người dùng"
                    ));
                }
            } catch (PDOException $e) {
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi thêm người dùng: " . $e->getMessage()
                ));
            }
        } else {
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
                $query = "UPDATE users SET username = :username, password = :password, role = :role, full_name = :full_name WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $data->id);
                $stmt->bindParam(':username', $data->username);
                $stmt->bindParam(':password', $data->password);
                $stmt->bindParam(':role', $data->role);
                $stmt->bindParam(':full_name', $data->full_name);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Cập nhật người dùng thành công"
                    ));
                } else {
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể cập nhật người dùng"
                    ));
                }
            } catch (PDOException $e) {
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi cập nhật người dùng: " . $e->getMessage()
                ));
            }
        } else {
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
                $query = "DELETE FROM users WHERE id = :id";
                $stmt = $pdo->prepare($query);
                $stmt->bindParam(':id', $_GET['id']);
                
                if ($stmt->execute()) {
                    echo json_encode(array(
                        "status" => "success",
                        "message" => "Xóa người dùng thành công"
                    ));
                } else {
                    echo json_encode(array(
                        "status" => "error",
                        "message" => "Không thể xóa người dùng"
                    ));
                }
            } catch (PDOException $e) {
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Lỗi khi xóa người dùng: " . $e->getMessage()
                ));
            }
        } else {
            echo json_encode(array(
                "status" => "error",
                "message" => "Vui lòng cung cấp ID người dùng"
            ));
        }
        break;
        
    default:
        echo json_encode(array(
            "status" => "error",
            "message" => "Phương thức không được hỗ trợ"
        ));
        break;
}
?>