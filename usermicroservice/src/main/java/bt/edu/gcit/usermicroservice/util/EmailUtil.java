package bt.edu.gcit.usermicroservice.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) throws MessagingException {
        String subject = "OTP for Password Reset";
        String content = "Dear user,\n\nYour OTP for resetting your password is: " + otp +
                "\n\nNote: This OTP is valid for 5 minutes.\n\nRegards,\nAlumni Platform Team";

        sendEmail(to, subject, content);
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false); // plain text

        mailSender.send(message);
    }
}
