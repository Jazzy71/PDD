<?php
session_start();
header("Content-Type: application/json");

include('db.php');

// Check if email & password are provided
if (!isset($_POST['email'], $_POST['password'])) {
    echo json_encode(["status" => "error", "message" => "Missing required fields"]);
    exit;
}

$email = $conn->real_escape_string($_POST['email']);
$password = $_POST['password'];

// Query to fetch user details from 'signup' table
$sql = "SELECT id, name, email, password FROM signup WHERE email = '$email'";
$result = $conn->query($sql);

if ($result->num_rows === 1) {
    $user = $result->fetch_assoc();

    // Directly compare passwords (NO HASHING)
    if ($password === $user['password']) {
        $_SESSION['id'] = $user['id'];
        $_SESSION['name'] = $user['name'];
        $_SESSION['email'] = $user['email'];
        $_SESSION['logged_in'] = true;

        echo json_encode([
            "status" => "success",
            "message" => "Login successful",
            "user" => [
                "id" => $_SESSION['id'],
                "name" => $_SESSION['name'],
                "email" => $_SESSION['email'],
                "logged_in" => $_SESSION['logged_in']
            ]
        ]);
    } else {
        echo json_encode(["status" => "error", "message" => "Invalid password"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "User not found"]);
}

$conn->close();
?>
