<?php
session_start();
header("Content-Type: application/json");

include('db.php'); // Database connection file

// ✅ Ensure user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "User not logged in"]);
    exit;
}

$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'POST') {
    // ✅ Validate Inputs
    if (!isset($_POST['listing_id'], $_POST['base_price'])) {
        echo json_encode(["status" => "error", "message" => "Missing required fields"]);
        exit;
    }

    $listing_id = intval($_POST['listing_id']);
    $base_price = floatval($_POST['base_price']);

    // ✅ Fetch Additional Car Details (registration_year, fuel_type, kilometer)
    $car_sql = "SELECT registration_year, fuel_type, kilometer FROM car_listings WHERE listing_id = ?";
    $car_stmt = $conn->prepare($car_sql);
    $car_stmt->bind_param("i", $listing_id);
    $car_stmt->execute();
    $car_result = $car_stmt->get_result();

    if ($car_result->num_rows === 0) {
        echo json_encode(["status" => "error", "message" => "Invalid listing ID"]);
        exit;
    }

    $car_data = $car_result->fetch_assoc();
    $car_stmt->close();

    $registration_year = intval($car_data['registration_year']);
    $fuel_type = strtolower($car_data['fuel_type']); // Normalize fuel type (lowercase)
    $kilometer = intval($car_data['kilometer']);

    // ✅ Apply Estimation Formula
    $current_year = date("Y");
    $age = $current_year - $registration_year;

    // 🔹 Depreciation Factors (Modify as per business logic)
    $depreciation_rate_per_year = 0.05; // 5% depreciation per year
    $kilometer_depreciation = 0.0002; // 0.02% per kilometer
    $fuel_adjustment = 1.0;

    // 🔹 Fuel Type Adjustment
    if ($fuel_type == "petrol") {
        $fuel_adjustment = 0.9; // Petrol cars depreciate more
    } elseif ($fuel_type == "diesel") {
        $fuel_adjustment = 0.95; // Diesel holds value better
    } elseif ($fuel_type == "electric") {
        $fuel_adjustment = 1.1; // Electric cars have higher resale
    }

    // 🔹 Calculate Estimated Price
    $age_depreciation = $base_price * ($age * $depreciation_rate_per_year);
    $kilometer_depreciation_amount = $base_price * ($kilometer * $kilometer_depreciation);

    $estimated_price = $base_price - $age_depreciation - $kilometer_depreciation_amount;
    $estimated_price *= $fuel_adjustment; // Apply fuel type adjustment

    // 🔹 Ensure minimum price (to avoid negative values)
    $estimated_price = max($estimated_price, $base_price * 0.4); // Min value is 40% of base price

    // ✅ Check if an estimate already exists for this listing
    $check_estimate_sql = "SELECT id FROM car_price_estimates WHERE listing_id = ?";
    $check_estimate_stmt = $conn->prepare($check_estimate_sql);
    $check_estimate_stmt->bind_param("i", $listing_id);
    $check_estimate_stmt->execute();
    $result = $check_estimate_stmt->get_result();
    
    if ($result->num_rows > 0) {
        // ✅ Update existing estimated price
        $sql = "UPDATE car_price_estimates SET base_price = ?, estimated_price = ?, calculated_at = NOW() WHERE listing_id = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ddi", $base_price, $estimated_price, $listing_id);
    } else {
        // ✅ Insert new estimated price
        $sql = "INSERT INTO car_price_estimates (listing_id, base_price, estimated_price, calculated_at) VALUES (?, ?, ?, NOW())";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("idd", $listing_id, $base_price, $estimated_price);
    }

    if ($stmt->execute()) {
        echo json_encode([
            "status" => "success",
            "message" => "Estimated price calculated and saved successfully",
            "base_price" => $base_price,
            "estimated_price" => $estimated_price
        ]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
    }

    $stmt->close();

} elseif ($method == 'GET') {
    // ✅ Retrieve Estimated Price for a listing
    if (!isset($_GET['listing_id'])) {
        echo json_encode(["status" => "error", "message" => "Missing listing ID"]);
        exit;
    }

    $listing_id = intval($_GET['listing_id']);

    $sql = "SELECT * FROM car_price_estimates WHERE listing_id = ? ORDER BY calculated_at DESC LIMIT 1";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $listing_id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($row = $result->fetch_assoc()) {
        echo json_encode(["status" => "success", "estimated_price" => $row]);
    } else {
        echo json_encode(["status" => "error", "message" => "No estimated price found for this listing"]);
    }

    $stmt->close();

} elseif ($method == 'DELETE') {
    // ✅ Delete Estimated Price
    parse_str(file_get_contents("php://input"), $_DELETE);

    if (!isset($_DELETE['listing_id'])) {
        echo json_encode(["status" => "error", "message" => "Missing listing ID"]);
        exit;
    }

    $listing_id = intval($_DELETE['listing_id']);

    $sql = "DELETE FROM car_price_estimates WHERE listing_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $listing_id);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Estimated price deleted"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}

$conn->close();
?>
