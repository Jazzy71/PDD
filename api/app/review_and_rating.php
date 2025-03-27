<?php
session_start();
header("Content-Type: application/json");

include('db.php'); // Ensure database connection is included

// ✅ Ensure user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "User not logged in"]);
    exit;
}

$id = $_SESSION['id']; // The logged-in user submitting the review
$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'POST') {
    // ✅ Add a new review
    if (!isset($_POST['listing_id'], $_POST['rating'], $_POST['review_text'])) {
        echo json_encode(["status" => "error", "message" => "Missing required fields"]);
        exit;
    }

    $listing_id = intval($_POST['listing_id']);
    $rating = intval($_POST['rating']);
    $review_text = $conn->real_escape_string($_POST['review_text']);

    // ✅ Validate if listing_id exists in car_listings and fetch the seller_id
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
    $seller_id = $row['seller_id']; // Get seller ID from car_listings
    $stmt->close();

    // ✅ Validate if seller_id exists in signup table
    $check_seller = "SELECT id FROM signup WHERE id = ?";
    $stmt_seller = $conn->prepare($check_seller);
    $stmt_seller->bind_param("i", $seller_id);
    $stmt_seller->execute();
    $result_seller = $stmt_seller->get_result();

    if ($result_seller->num_rows === 0) {
        echo json_encode(["status" => "error", "message" => "Invalid seller ID. Seller does not exist."]);
        exit;
    }
    $stmt_seller->close();

    // ✅ Insert the review with validated `seller_id` and `listing_id`
    $sql = "INSERT INTO review_and_rating (buyer_id, seller_id, listing_id, rating, review_text) 
            VALUES (?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("iiiis", $id, $seller_id, $listing_id, $rating, $review_text);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Review submitted successfully", "review_id" => $stmt->insert_id]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
    }

    $stmt->close();

} elseif ($method == 'GET') {
    // ✅ Retrieve reviews (specific listing or all)
    $sql = "SELECT * FROM review_and_rating";
    
    if (isset($_GET['listing_id'])) {
        $listing_id = intval($_GET['listing_id']);
        $sql .= " WHERE listing_id = $listing_id";
    }

    $result = $conn->query($sql);
    $reviews = [];

    while ($row = $result->fetch_assoc()) {
        $reviews[] = $row;
    }

    echo json_encode(["status" => "success", "reviews" => $reviews]);

} elseif ($method == 'DELETE') {
    // ✅ Delete a review (only the buyer who wrote it can delete)
    parse_str(file_get_contents("php://input"), $_DELETE);

    if (!isset($_DELETE['review_id'])) {
        echo json_encode(["status" => "error", "message" => "Missing review ID"]);
        exit;
    }

    $review_id = intval($_DELETE['review_id']);

    // ✅ Ensure only the buyer who wrote the review can delete it
    $sql = "DELETE FROM review_and_rating WHERE review_id = ? AND buyer_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $review_id, $id);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Review deleted successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
    }

    $stmt->close();

} else {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
}

$conn->close();
?>
