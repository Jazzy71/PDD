<?php
session_start();
header("Content-Type: application/json");

include('db.php'); // Database connection file

// Check if email is provided in the request
if (!isset($_POST['email'])) {
    echo json_encode(["status" => "error", "message" => "Email is required for logout"]);
    exit;
}

$email = $conn->real_escape_string($_POST['email']);

// Check if the email exists in the `signup` table
$sql = "SELECT id FROM signup WHERE email = '$email'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // ✅ User exists, destroy the session
    session_unset();  // Unset session variables
    session_destroy(); // Destroy the session

    echo json_encode(["status" => "success", "message" => "User logged out successfully"]);
} else {
    // ❌ User does not exist
    echo json_encode(["status" => "error", "message" => "User not found"]);
}

$conn->close();
?>
