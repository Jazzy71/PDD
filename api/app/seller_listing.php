<?php
session_start();
header("Content-Type: application/json");

include('db.php'); // Ensure database connection

// ✅ Ensure seller is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "Seller not logged in"]);
    exit;
}

$seller_id = $_SESSION['id']; // Fetch the logged-in seller's ID

$sql = "SELECT * FROM car_listings WHERE seller_id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $seller_id);
$stmt->execute();
$result = $stmt->get_result();

$listings = [];
while ($row = $result->fetch_assoc()) {
    $listings[] = $row;
}

echo json_encode(["status" => "success", "listings" => $listings]);

$stmt->close();
$conn->close();
?>
