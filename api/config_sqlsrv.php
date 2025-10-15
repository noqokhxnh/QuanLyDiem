<?php
// config.php - SQL Server configuration

// SQL Server configuration
$serverName = "localhost";
$database = "student_manager";
$username = "qld_user";
$password = "StrongPassword123!";

try {
    // Sử dụng SQL Server Driver for PHP
    $connectionOptions = array(
        "Database" => $database,
        "UID" => $username,
        "PWD" => $password,
        "CharacterSet" => "UTF-8"
    );
    
    $conn = sqlsrv_connect($serverName, $connectionOptions);
    
    if ($conn === false) {
        http_response_code(500);
        echo json_encode(array(
            "status" => "error",
            "message" => "Lỗi kết nối database: " . print_r(sqlsrv_errors(), true)
        ));
        exit;
    }
} catch(Exception $e) {
    http_response_code(500);
    echo json_encode(array(
        "status" => "error",
        "message" => "Lỗi kết nối database: " . $e->getMessage()
    ));
    exit;
}
?>