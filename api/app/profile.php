<?php
session_start();
header("Content-Type: application/json");
include('db.php'); // Database connection file

// ✅ Ensure user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "Unauthorized access. Please log in."]);
    exit;
}

$user_id = $_SESSION['id'];
$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'POST') {
    // ✅ Validate required fields
    if (!isset($_POST['email'], $_POST['username'], $_POST['mobile'], $_POST['location'])) {
        echo json_encode(["status" => "error", "message" => "Missing required fields"]);
        exit;
    }

    $email = $conn->real_escape_string($_POST['email']);
    $username = $conn->real_escape_string($_POST['username']);
    $mobile = $conn->real_escape_string($_POST['mobile']);
    $location = $conn->real_escape_string($_POST['location']);

    // ✅ Check if profile already exists
    $check_profile = "SELECT id FROM profile WHERE id = ?";
    $stmt = $conn->prepare($check_profile);
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $profile_result = $stmt->get_result();

    if ($profile_result->num_rows > 0) {
        echo json_encode(["status" => "error", "message" => "Profile already exists"]);
        exit;
    }

    // ✅ Insert into profile table (no profile_pic)
    $sql = "INSERT INTO profile (id, username, email, mobile, location) VALUES (?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("issss", $user_id, $username, $email, $mobile, $location);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Profile created successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
    }

} elseif ($method == 'PUT') {
    // ✅ Read JSON input
    $data = json_decode(file_get_contents("php://input"), true);

    if (!isset($data['username'], $data['mobile'], $data['location'])) {
        echo json_encode(["status" => "error", "message" => "Missing required fields"]);
        exit;
    }

    $username = $conn->real_escape_string($data['username']);
    $mobile = $conn->real_escape_string($data['mobile']);
    $location = $conn->real_escape_string($data['location']);

    // ✅ Update profile table
    $sql = "UPDATE profile SET username = ?, mobile = ?, location = ? WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sssi", $username, $mobile, $location, $user_id);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Profile updated successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: " . $conn->error]);
    }

} else {
    echo json_encode(["status" => "error", "message" => "Method not allowed"]);
}

$conn->close();
?>
