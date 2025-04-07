package com.example.OTP.service.Impl;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.OTP.entity.Customer;
import com.example.OTP.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public String sendOtp(String email, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("my.custome.projects.2025@gmail.com");
            helper.setTo(email);
            helper.setSubject("OTP Verification");

            String htmlContent;
            try (var inputStream = Objects.requireNonNull(
                    EmailService.class.getResourceAsStream("/templates/otp-template.html"))) {
                htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

            htmlContent = htmlContent.replace("{{OTP}}", otp);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            return "OTP sent successfully";
        } catch (Exception e) {
            return "Failed to send OTP email: " + e.getMessage();
        }
    }

    public String sendAccountCreationEmail(Customer customer) {
        log.info("Starting to send account creation email to {}", customer.getEmail());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("your.email@example.com");
            helper.setTo(customer.getEmail());
            helper.setSubject("Congratulations on Creating Your Account!");

            String htmlContent;
            try (var inputStream = Objects.requireNonNull(
                    EmailService.class.getResourceAsStream("/templates/account-creation-template.html"))) {
                htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                log.debug("HTML template loaded successfully");
            } catch (Exception e) {
                log.error("Failed to load HTML template", e);
                return "Failed to send account creation email: " + e.getMessage();
            }

            htmlContent = htmlContent.replace("{{id}}", String.valueOf(customer.getId()))
                    .replace("{{id}}", String.valueOf(customer.getId()))
                    .replace("{{email}}", customer.getEmail())
                    .replace("{{firstName}}", customer.getFirstName())
                    .replace("{{lastName}}", customer.getLastName())
                    .replace("{{age}}", String.valueOf(customer.getAge()))
                    .replace("{{address}}", customer.getAddress())
                    .replace("{{phone}}", customer.getPhone());
            log.debug("HTML content replaced with user details");

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Account creation email sent successfully to {}", customer.getEmail());
            return "Account creation email sent successfully!";
        } catch (Exception e) {
            log.error("Failed to send account creation email to {}", customer.getEmail(), e);
            return "Failed to send account creation email: " + e.getMessage();
        }

    }

}
