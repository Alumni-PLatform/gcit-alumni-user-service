package bt.edu.gcit.usermicroservice.controller;

import bt.edu.gcit.usermicroservice.dto.*;
import bt.edu.gcit.usermicroservice.service.ForgotPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        boolean sent = forgotPasswordService.sendOtpToEmail(request.getEmail());
        return ResponseEntity.ok("If your email is registered, an OTP has been sent.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody @Valid VerifyOtpRequest request) {
        boolean verified = forgotPasswordService.verifyOtp(request.getEmail(), request.getOtp());
        return verified
                ? ResponseEntity.ok("OTP verified successfully!")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP or OTP expired.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        boolean reset = forgotPasswordService.resetPassword(
                request.getEmail(),
                request.getNewPassword(),
                request.getConfirmPassword());
        return reset
                ? ResponseEntity.ok("Password has been reset successfully.")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match or email is invalid.");
    }
}
