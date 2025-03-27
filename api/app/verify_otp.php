<?php
session_start(); // Start session
include('db.php');
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!empty($_POST['otp'])) {
        $otp = $_POST['otp'];

        // Find the email associated with this OTP
        $stmt = $conn->prepare("SELECT email FROM signup WHERE otp = ?");
        $stmt->bind_param("i", $otp);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($row = $result->fetch_assoc()) {
            $_SESSION['email'] = $row['email']; // Store email in session

            // Clear OTP from database
            $clear_otp_stmt = $conn->prepare("UPDATE signup SET otp = NULL WHERE email = ?");
            $clear_otp_stmt->bind_param("s", $_SESSION['email']);
            $clear_otp_stmt->execute();
            $clear_otp_stmt->close();

            echo json_encode(["status" => "success", "message" => "OTP verified successfully. Proceed to reset password."]);
        } else {
            echo json_encode(["status" => "error", "message" => "Invalid OTP. Please try again."]);
        }

        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "OTP is required."]);
    }
}
?>