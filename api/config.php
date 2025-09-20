<?php
// config.php - Cấu hình kết nối database
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type");

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
        "message" => "Không thể kết nối đến database: " . $e->getMessage()
    ));
    exit();
}
?>