<?php
header("Content-Type: application/json");
include('db.php'); // Database connection

$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'GET') {
    // Search parameters
    $location = isset($_GET['location']) ? $_GET['location'] : '';
    $car_model = isset($_GET['car_model']) ? $_GET['car_model'] : '';
    $fuel_type = isset($_GET['fuel_type']) ? $_GET['fuel_type'] : '';
    $min_price = isset($_GET['min_price']) ? floatval($_GET['min_price']) : 0;
    $max_price = isset($_GET['max_price']) ? floatval($_GET['max_price']) : 99999999;

    // Base query
    $sql = "SELECT * FROM car_listings WHERE base_price BETWEEN ? AND ?";

    // Dynamic filtering
    $params = [$min_price, $max_price];
    $types = "dd";

    if (!empty($location)) {
        $sql .= " AND location LIKE ?";
        $params[] = "%$location%";
        $types .= "s";
    }

    if (!empty($car_model)) {
        $sql .= " AND car_model LIKE ?";
        $params[] = "%$car_model%";
        $types .= "s";
    }

    if (!empty($fuel_type)) {
        $sql .= " AND fuel_type = ?";
        $params[] = $fuel_type;
        $types .= "s";
    }

    // Prepare and execute query
    $stmt = $conn->prepare($sql);
    if ($stmt) {
        $stmt->bind_param($types, ...$params);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch results
        $cars = [];
        while ($row = $result->fetch_assoc()) {
            $cars[] = $row;
        }

        echo json_encode(["status" => "success", "cars" => $cars]);

        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Database query preparation failed: " . $conn->error]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method."]);
}

$conn->close();
?>
