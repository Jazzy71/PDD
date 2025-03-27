<?php
session_start();
header("Content-Type: application/json");

include('db.php');

// ✅ Check all required fields
if (
    !isset($_POST['name'], $_POST['email'], $_POST['password'], $_POST['mobile'], $_POST['location'])
) {
    echo json_encode(["status" => "error", "message" => "Missing required fields"]);
    exit;
}

// ✅ Sanitize inputs
$name = $conn->real_escape_string($_POST['name']);
$email = $conn->real_escape_string($_POST['email']);
$password = $conn->real_escape_string($_POST['password']); // Optional: hash before storing
$mobile = $conn->real_escape_string($_POST['mobile']);
$location = $conn->real_escape_string($_POST['location']);

// ✅ Optional: Hash password (recommended for security)
// $password = password_hash($_POST['password'], PASSWORD_DEFAULT);

// ✅ Check if email already exists
$check_sql = "SELECT * FROM signup WHERE email = '$email'";
$result = $conn->query($check_sql);

if ($result && $result->num_rows > 0) {
    echo json_encode(["status" => "error", "message" => "Email already registered"]);
    exit;
}

// ✅ Insert user into signup table
$sql = "INSERT INTO signup (name, email, password, mobile, location)
        VALUES ('$name', '$email', '$password', '$mobile', '$location')";

if ($conn->query($sql) === TRUE) {
    $user_id = $conn->insert_id;

    // Start session
    $_SESSION['id'] = $user_id;
    $_SESSION['name'] = $name;
    $_SESSION['email'] = $email;
    $_SESSION['logged_in'] = true;

    echo json_encode([
        "status" => "success",
        "message" => "User registered successfully",
        "user" => [
            "id" => $_SESSION['id'],
            "name" => $_SESSION['name'],
            "email" => $_SESSION['email'],
            "logged_in" => $_SESSION['logged_in']
        ]
    ]);
} else {
    echo json_encode(["status" => "error", "message" => "Database Error: " . $conn->error]);
}

$conn->close();
?>
