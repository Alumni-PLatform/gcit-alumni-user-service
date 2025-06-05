package bt.edu.gcit.usermicroservice.service;

public interface ForgotPasswordService {
    boolean sendOtpToEmail(String email);

    boolean verifyOtp(String email, String enteredOtp);

    boolean resetPassword(String email, String newPassword, String confirmPassword);
}
