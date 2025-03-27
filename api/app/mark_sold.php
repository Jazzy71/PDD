<?php
session_start();
header("Content-Type: application/json");

include('db.php'); // Ensure database connection

// ✅ Ensure seller is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "Seller not logged in"]);
    exit;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    if (!isset($_POST['listing_id'])) {
        echo json_encode(["status" => "error", "message" => "Missing listing ID"]);
        exit;
    }

    $listing_id = intval($_POST['listing_id']);
    $seller_id = $_SESSION['id']; // Ensure seller is the owner

    // ✅ Check if the seller owns the listing
    $sql = "SELECT seller_id FROM car_listings WHERE listing_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $listing_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows == 0) {
        echo json_encode(["status" => "error", "message" => "Invalid listing ID"]);
        exit;
    }

    $row = $result->fetch_assoc();
    if ($row['seller_id'] != $seller_id) {
        echo json_encode(["status" => "error", "message" => "Unauthorized action"]);
        exit;
    }
    
    // ✅ Mark the listing as sold
    $sql = "UPDATE car_listings SET status = 'Sold' WHERE listing_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $listing_id);
    
    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Car listing marked as sold."]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error updating record: " . $conn->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}

$conn->close();
?>
