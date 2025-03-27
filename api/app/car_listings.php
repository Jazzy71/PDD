<?php
session_start();
header("Content-Type: application/json");
include('db.php'); // Database connection file

// ✅ Ensure user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "User not logged in"]);
    exit;
}

$seller_id = isset($_POST['seller_id']) ? intval($_POST['seller_id']) : $_SESSION['id']; // Allow override in Postman
$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'POST') {
    // ✅ Use $_POST for Form Data instead of JSON input
    if (!isset($_POST['location'], $_POST['registration_year'], $_POST['car_name'], $_POST['car_model'], $_POST['kilometer'], $_POST['fuel_type'], $_POST['base_price'], $_POST['status'])) {
        echo json_encode(["status" => "error", "message" => "Missing required fields"]);
        exit;
    }

    $location = $conn->real_escape_string($_POST['location']);
    $registration_year = intval($_POST['registration_year']);
    $car_name = $conn->real_escape_string($_POST['car_name']);
    $car_model = $conn->real_escape_string($_POST['car_model']);
    $kilometer = intval($_POST['kilometer']);
    $fuel_type = $conn->real_escape_string($_POST['fuel_type']);
    $base_price = floatval($_POST['base_price']);
    $status = $conn->real_escape_string($_POST['status']);
    $created_at = date('Y-m-d H:i:s');

    // ✅ Check if seller_id exists
    $stmt_check = $conn->prepare("SELECT id FROM signup WHERE id = ?");
    $stmt_check->bind_param("i", $seller_id);
    $stmt_check->execute();
    $result_check = $stmt_check->get_result();
    if ($result_check->num_rows === 0) {
        echo json_encode(["status" => "error", "message" => "Invalid seller ID"]);
        exit;
    }
    $stmt_check->close();

    // ✅ Insert into car_listings
    $sql = "INSERT INTO car_listings (seller_id, location, registration_year, car_name, car_model, kilometer, fuel_type, base_price, status, created_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("isissisdss", $seller_id, $location, $registration_year, $car_name, $car_model, $kilometer, $fuel_type, $base_price, $status, $created_at);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Car listing added successfully", "listing_id" => $stmt->insert_id]);
    } else {
        echo json_encode(["status" => "error", "message" => "Database error: " . $conn->error]);
    }
    $stmt->close();

} elseif ($method == 'GET') {
    // ✅ Retrieve car listings
    $sql = "SELECT * FROM car_listings";
    if (isset($_GET['listing_id'])) {
        $listing_id = intval($_GET['listing_id']);
        $sql .= " WHERE listing_id = $listing_id";
    }

    $result = $conn->query($sql);
    $listings = [];
    while ($row = $result->fetch_assoc()) {
        $listings[] = $row;
    }

    echo json_encode(["status" => "success", "car_listings" => $listings]);

} elseif ($method == 'PUT') {
    // ✅ Read JSON Data for PUT
    $_PUT = json_decode(file_get_contents("php://input"), true);

    if (!isset($_PUT['listing_id'], $_PUT['location'], $_PUT['registration_year'], $_PUT['car_name'], $_PUT['car_model'], $_PUT['kilometer'], $_PUT['fuel_type'], $_PUT['base_price'], $_PUT['status'])) {
        echo json_encode(["status" => "error", "message" => "Missing required fields"]);
        exit;
    }

    $listing_id = intval($_PUT['listing_id']);
    $location = $conn->real_escape_string($_PUT['location']);
    $registration_year = intval($_PUT['registration_year']);
    $car_name = $conn->real_escape_string($_PUT['car_name']);
    $car_model = $conn->real_escape_string($_PUT['car_model']);
    $kilometer = intval($_PUT['kilometer']);
    $fuel_type = $conn->real_escape_string($_PUT['fuel_type']);
    $base_price = floatval($_PUT['base_price']);
    $status = $conn->real_escape_string($_PUT['status']);

    // ✅ Update only if the seller owns this listing
    $sql = "UPDATE car_listings SET location=?, registration_year=?, car_name=?, car_model=?, kilometer=?, fuel_type=?, base_price=?, status=? 
            WHERE listing_id=? AND seller_id=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sissisdsii", $location, $registration_year, $car_name, $car_model, $kilometer, $fuel_type, $base_price, $status, $listing_id, $seller_id);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Car listing updated successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Database error: " . $conn->error]);
    }
    $stmt->close();

} elseif ($method == 'DELETE') {
    // ✅ Read JSON Data for DELETE
    $_DELETE = json_decode(file_get_contents("php://input"), true);

    if (!isset($_DELETE['listing_id'])) {
        echo json_encode(["status" => "error", "message" => "Missing listing ID"]);
        exit;
    }

    $listing_id = intval($_DELETE['listing_id']);

    // ✅ Delete only if the seller owns this listing
    $sql = "DELETE FROM car_listings WHERE listing_id=? AND seller_id=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $listing_id, $seller_id);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Car listing deleted successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Database error: " . $conn->error]);
    }
    $stmt->close();

} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}

$conn->close();
?>
