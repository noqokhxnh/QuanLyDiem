<?php
// login.php - Xác thực người dùng
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

include_once 'config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    
    if (!empty($data->username) && !empty($data->password)) {
        try {
            $query = "SELECT * FROM users WHERE username = :username AND password = :password";
            $stmt = $pdo->prepare($query);
            $stmt->bindParam(':username', $data->username);
            $stmt->bindParam(':password', $data->password);
            $stmt->execute();
            
            if ($stmt->rowCount() > 0) {
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                echo json_encode(array(
                    "status" => "success",
                    "message" => "Đăng nhập thành công",
                    "data" => $user
                ));
            } else {
                echo json_encode(array(
                    "status" => "error",
                    "message" => "Tên đăng nhập hoặc mật khẩu không đúng"
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
        http_response_code(400);
        echo json_encode(array(
            "status" => "error",
            "message" => "Vui lòng cung cấp tên đăng nhập và mật khẩu"
        ));
    }
} else {
    http_response_code(405);
    echo json_encode(array(
        "status" => "error",
        "message" => "Phương thức không được hỗ trợ"
    ));
}
?>