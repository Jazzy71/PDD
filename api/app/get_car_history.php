<?php
header("Content-Type: application/json");
include('db.php'); // Database connection file

// Check if listing_id is present in POST data
if (!isset($_POST["listing_id"]) || empty($_POST["listing_id"])) {
    echo json_encode(["status" => "error", "message" => "Missing listing_id."]);
    exit;
}

$listing_id = intval($_POST["listing_id"]);

$stmt = $conn->prepare("SELECT * FROM car_history WHERE listing_id = ?");
$stmt->bind_param("i", $listing_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $history = $result->fetch_assoc();
    echo json_encode(["status" => "success", "car_history" => $history]);
} else {
    echo json_encode(["status" => "error", "message" => "No history found for this car."]);
}

$stmt->close();
$conn->close();
?>
