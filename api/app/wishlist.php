<?php
session_start();
header("Content-Type: application/json");

include('db.php'); // Database connection file

// ✅ Ensure user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "User not logged in"]);
    exit;
}

$user_id = $_SESSION['id']; // Get logged-in user ID
$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'POST') {
    // ✅ Add to Wishlist
    if (!isset($_POST['listing_id'])) {
        echo json_encode(["status" => "error", "message" => "Missing listing ID"]);
        exit;
    }

    $listing_id = intval($_POST['listing_id']);

    // Check if the car is already in the wishlist
    $check_sql = "SELECT * FROM wishlist WHERE user_id = ? AND listing_id = ?";
    $check_stmt = $conn->prepare($check_sql);
    $check_stmt->bind_param("ii", $user_id, $listing_id);
    $check_stmt->execute();
    $result = $check_stmt->get_result();

    if ($result->num_rows > 0) {
        echo json_encode(["status" => "error", "message" => "Car already in wishlist"]);
    } else {
        // Add car to wishlist
        $sql = "INSERT INTO wishlist (user_id, listing_id) VALUES (?, ?)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ii", $user_id, $listing_id);

        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Car added to wishlist"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
        }
        $stmt->close();
    }

    $check_stmt->close();

} elseif ($method == 'GET') {
    // ✅ View Wishlist (Fetch saved cars)
    $sql = "SELECT w.listing_id, c.car_name, c.car_model, c.base_price, c.fuel_type, c.location, w.added_at 
            FROM wishlist w 
            JOIN car_listings c ON w.listing_id = c.listing_id 
            WHERE w.user_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $wishlist = [];
    while ($row = $result->fetch_assoc()) {
        $wishlist[] = $row;
    }

    echo json_encode(["status" => "success", "wishlist" => $wishlist]);

    $stmt->close();

} elseif ($method == 'DELETE') {
    // ✅ Remove from Wishlist
    parse_str(file_get_contents("php://input"), $_DELETE);

    if (!isset($_DELETE['listing_id'])) {
        echo json_encode(["status" => "error", "message" => "Missing listing ID"]);
        exit;
    }

    $listing_id = intval($_DELETE['listing_id']);

    $sql = "DELETE FROM wishlist WHERE user_id = ? AND listing_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $user_id, $listing_id);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Car removed from wishlist"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}

$conn->close();
?>
