package bt.edu.gcit.usermicroservice.service.impl;

import bt.edu.gcit.usermicroservice.entity.User;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;
import bt.edu.gcit.usermicroservice.repository.UserRepository;
import bt.edu.gcit.usermicroservice.service.ForgotPasswordService;
import bt.edu.gcit.usermicroservice.util.EmailUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final PasswordEncoder passwordEncoder;

    // OTP config
    private static final long OTP_VALID_DURATION_SECONDS = 300; // 5 minutes
    private final Map<String, OtpInfo> otpStorage = new ConcurrentHashMap<>();

    @Override
    public boolean sendOtpToEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user found with this email."));

        String otp = generateOtp();
        otpStorage.put(email, new OtpInfo(otp, Instant.now()));

        try {
            emailUtil.sendOtpEmail(email, otp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean verifyOtp(String email, String enteredOtp) {
        OtpInfo otpInfo = otpStorage.get(email);

        if (otpInfo != null) {
            boolean notExpired = Instant.now().isBefore(otpInfo.generatedAt.plusSeconds(OTP_VALID_DURATION_SECONDS));
            boolean matches = otpInfo.otp.equals(enteredOtp);

            if (notExpired && matches) {
                otpStorage.remove(email);
                return true;
            }
        }

        otpStorage.remove(email); // cleanup expired or incorrect OTP
        return false;
    }

    @Transactional
    @Override
    public boolean resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return false;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user found with this email."));

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        otpStorage.remove(email);
        return true;
    }

    // === Private Helpers ===

    private String generateOtp() {
        Random rand = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(rand.nextInt(10));
        }
        return otp.toString();
    }

    private static class OtpInfo {
        String otp;
        Instant generatedAt;

        OtpInfo(String otp, Instant generatedAt) {
            this.otp = otp;
            this.generatedAt = generatedAt;
        }
    }
}
