<?php
session_start(); // Start session
include('db.php');
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!empty($_POST['new_password']) && !empty($_POST['confirm_password'])) {
        if (!isset($_SESSION['email'])) {
            echo json_encode(["status" => "error", "message" => "Session expired. Please verify OTP again."]);
            exit();
        }

        $email = $_SESSION['email'];
        $new_password = $_POST['new_password'];
        $confirm_password = $_POST['confirm_password'];

        // Check if passwords match
        if ($new_password !== $confirm_password) {
            echo json_encode(["status" => "error", "message" => "Passwords do not match."]);
            exit();
        }

        // Update password in database (WITHOUT HASHING)
        $update_stmt = $conn->prepare("UPDATE signup SET password = ? WHERE email = ?");
        $update_stmt->bind_param("ss", $new_password, $email);

        if ($update_stmt->execute()) {
            session_destroy(); // Clear session after password reset
            echo json_encode(["status" => "success", "message" => "Password reset successful. You can now log in."]);
        } else {
            echo json_encode(["status" => "error", "message" => "Error resetting password. Please try again."]);
        }

        $update_stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "New password and confirm password are required."]);
    }
}
?>