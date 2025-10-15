<?php
// config.php - Database configuration

// Database configuration
$host = 'localhost';
$db_name = 'student_manager';
$username = 'root';
$password = '';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db_name;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    http_response_code(500);
    echo json_encode(array(
        "status" => "error",
        "message" => "Lỗi kết nối database: " . $e->getMessage()
    ));
    exit;
}
?>