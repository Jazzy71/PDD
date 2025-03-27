<?php
session_start();
header("Content-Type: application/json");
include('db.php'); // Database connection file

// Check if user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "Unauthorized access. Please log in."]);
    exit;
}

// Check request method
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

// Check if the listing exists
$check_listing = $conn->prepare("SELECT listing_id FROM car_listings WHERE listing_id = ?");
$check_listing->bind_param("i", $listing_id);
$check_listing->execute();
$result = $check_listing->get_result();

if ($result->num_rows === 0) {
    echo json_encode(["status" => "error", "message" => "Listing not found."]);
    exit;
}

// Check if files are uploaded
if (!isset($_FILES["image_path"]) || empty($_FILES["image_path"]["name"])) {
    echo json_encode(["status" => "error", "message" => "No images uploaded."]);
    exit;
}

// File upload configuration
$upload_dir = "uploads/cars/";
if (!file_exists($upload_dir)) {
    mkdir($upload_dir, 0777, true); // Create directory if it doesn't exist
}

$allowed_extensions = ["jpg", "jpeg", "png", "gif"];
$image_paths = [];

// Ensure $_FILES["image_path"]["tmp_name"] is an array
$files = is_array($_FILES["image_path"]["name"]) ? $_FILES["image_path"]["name"] : [$_FILES["image_path"]["name"]];
$tmp_files = is_array($_FILES["image_path"]["tmp_name"]) ? $_FILES["image_path"]["tmp_name"] : [$_FILES["image_path"]["tmp_name"]];
$errors = is_array($_FILES["image_path"]["error"]) ? $_FILES["image_path"]["error"] : [$_FILES["image_path"]["error"]];

foreach ($files as $key => $image_name) {
    $tmp_name = $tmp_files[$key];

    if ($errors[$key] !== UPLOAD_ERR_OK) {
        echo json_encode(["status" => "error", "message" => "File upload error: " . $errors[$key]]);
        exit;
    }

    $image_ext = strtolower(pathinfo($image_name, PATHINFO_EXTENSION));

    if (!in_array($image_ext, $allowed_extensions)) {
        echo json_encode(["status" => "error", "message" => "Invalid image format. Only JPG, JPEG, PNG, GIF allowed."]);
        exit;
    }

    // Generate unique image name
    $unique_name = uniqid("car_", true) . "." . $image_ext;
    $image_path = $upload_dir . $unique_name;

    // Move uploaded file
    if (move_uploaded_file($tmp_name, $image_path)) {
        $image_paths[] = $image_path;
    } else {
        echo json_encode(["status" => "error", "message" => "Failed to upload one or more images."]);
        exit;
    }
}

// Insert image paths into database
$stmt = $conn->prepare("INSERT INTO car_images (listing_id, image_path) VALUES (?, ?)");

foreach ($image_paths as $path) {
    $stmt->bind_param("is", $listing_id, $path);
    if (!$stmt->execute()) {
        echo json_encode(["status" => "error", "message" => "Database insert failed: " . $stmt->error]);
        exit;
    }
}

$stmt->close();
$conn->close();

echo json_encode([
    "status" => "success",
    "message" => "Images uploaded successfully",
    "uploaded_images" => $image_paths
]);
?>
