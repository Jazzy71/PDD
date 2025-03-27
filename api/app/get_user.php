<?php
session_start();
header("Content-Type: application/json");

include('db.php');

if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "User not logged in"]);
    exit;
}

$id = $_SESSION['id'];

$sql = "SELECT name, email, mobile, location FROM signup WHERE id = $id";
$result = $conn->query($sql);

if ($result && $result->num_rows > 0) {
    $user = $result->fetch_assoc();
    echo json_encode(["status" => "success", "user" => $user]);
} else {
    echo json_encode(["status" => "error", "message" => "User not found"]);
}

$conn->close();
?>
