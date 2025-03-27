<?php
include('db.php');
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// Load PHPMailer
require __DIR__ . '/PHPMailer/src/Exception.php';
require __DIR__ . '/PHPMailer/src/PHPMailer.php';
require __DIR__ . '/PHPMailer/src/SMTP.php';

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!empty($_POST['email'])) {
        $email = $_POST['email'];

        // ✅ Check if email exists in the database
        $stmt = $conn->prepare("SELECT id FROM signup WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($row = $result->fetch_assoc()) {
            // ✅ Generate a 4-digit OTP
            $otp = rand(1000, 9999);

            // ✅ Store OTP in the database
            $update_stmt = $conn->prepare("UPDATE signup SET otp = ? WHERE email = ?");
            $update_stmt->bind_param("is", $otp, $email);

            if ($update_stmt->execute()) {
                // ✅ Initialize PHPMailer
                $mail = new PHPMailer(true);
                try {
                    $mail->isSMTP();
                    $mail->Host = 'smtp.gmail.com'; // SMTP Server
                    $mail->SMTPAuth = true;
                    $mail->Username = 'jazimj77@gmail.com'; // Your Gmail Address
                    $mail->Password = 'lzfk pjvt dpix tcii'; // 🔴 SECURITY RISK (use env variable)
                    $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
                    $mail->Port = 587;

                    // ✅ Email setup
                    $mail->setFrom('jazimj77@gmail.com', 'carnest'); // Sender's Email & Name
                    $mail->addAddress($email); // Recipient's Email

                    // ✅ Email content
                    $mail->Subject = 'Your OTP Code';
                    $mail->Body    = "Your OTP for password reset is: $otp\n\nPlease do not share this OTP with anyone.";

                    // ✅ Send email
                    if ($mail->send()) {
                        echo json_encode(["status" => "success", "message" => "OTP sent to your email."]);
                    } else {
                        echo json_encode(["status" => "error", "message" => "Failed to send OTP email."]);
                    }
                } catch (Exception $e) {
                    echo json_encode(["status" => "error", "message" => "Mailer Error: " . $mail->ErrorInfo]);
                }
            } else {
                echo json_encode(["status" => "error", "message" => "Error updating OTP in database."]);
            }
            $update_stmt->close();
        } else {
            echo json_encode(["status" => "error", "message" => "Email not found."]);
        }

        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "Email address is required."]);
    }
}

$conn->close();
?>
