<?php
session_start();
header("Content-Type: application/json");
include('db.php'); // Database connection file

// Check if user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "Unauthorized access. Please log in."]);
    exit;
}

// Ensure request is POST
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    echo json_encode(["status" => "error", "message" => "Invalid request method. Use POST."]);
    exit;
}

// Validate listing_id
if (!isset($_POST["listing_id"]) || empty($_POST["listing_id"])) {
    echo json_encode(["status" => "error", "message" => "Missing listing_id."]);
    exit;
}

$listing_id = intval($_POST["listing_id"]);
$previous_owners = isset($_POST["previous_owners"]) ? intval($_POST["previous_owners"]) : 1;
$accident_history = isset($_POST["accident_history"]) ? $_POST["accident_history"] : "No";
$service_records = isset($_POST["service_records"]) ? $_POST["service_records"] : null;
$major_repairs = isset($_POST["major_repairs"]) ? $_POST["major_repairs"] : null;
$insurance_claims = isset($_POST["insurance_claims"]) ? $_POST["insurance_claims"] : "No";

// Check if the listing exists
$check_listing = $conn->prepare("SELECT listing_id FROM car_listings WHERE listing_id = ?");
$check_listing->bind_param("i", $listing_id);
$check_listing->execute();
$result = $check_listing->get_result();

if ($result->num_rows === 0) {
    echo json_encode(["status" => "error", "message" => "Listing not found."]);
    exit;
}

// Check if history already exists for this listing
$check_history = $conn->prepare("SELECT history_id FROM car_history WHERE listing_id = ?");
$check_history->bind_param("i", $listing_id);
$check_history->execute();
$result = $check_history->get_result();

if ($result->num_rows > 0) {
    // Update existing history
    $stmt = $conn->prepare("UPDATE car_history SET 
        previous_owners = ?, accident_history = ?, service_records = ?, major_repairs = ?, insurance_claims = ? 
        WHERE listing_id = ?");
    $stmt->bind_param("issssi", $previous_owners, $accident_history, $service_records, $major_repairs, $insurance_claims, $listing_id);
} else {
    // Insert new history
    $stmt = $conn->prepare("INSERT INTO car_history (listing_id, previous_owners, accident_history, service_records, major_repairs, insurance_claims) 
        VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("iissss", $listing_id, $previous_owners, $accident_history, $service_records, $major_repairs, $insurance_claims);
}

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Car history saved successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to save car history"]);
}

$stmt->close();
$conn->close();
?>
