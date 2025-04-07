package com.example.OTP.service.Impl;

import java.util.Base64;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.OTP.entity.Customer;
import com.example.OTP.entity.OTP;
import com.example.OTP.exception.ResourceAlreadyExistException;
import com.example.OTP.exception.ResourceNotExistException;
import com.example.OTP.repository.CustomerRepository;
import com.example.OTP.repository.OTPRepository;
import com.example.OTP.service.CustomerService;
import com.example.OTP.service.EmailService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    private final OTPRepository otpRepository;

    @Override
    public Customer saveCustomer(Customer customer) throws ResourceAlreadyExistException {
        // Check if email already exists
        log.info("Attempting to save customer with email: {}", customer.getEmail());
        Optional.ofNullable(customerRepository.findByEmail(customer.getEmail())).ifPresent(existingCustomer -> {
            log.warn("Email already exists: {}", existingCustomer.getEmail());
            throw new ResourceAlreadyExistException("Email already exists");
        });
        // Save to database
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer saved successfully with ID: {}", savedCustomer.toString());
        // Send a welcome email to customer
        emailService.sendAccountCreationEmail(savedCustomer);
        return savedCustomer;
    }

    @Override
    public String sendOTP(String email) {

        return Optional.ofNullable(customerRepository.findByEmail(email))
                .map(existingCustomer -> {
                    log.info("Email exists. Sending OTP to {}", existingCustomer.getEmail());

                    // Generate OTP
                    String generatedOTP = generateOTP();
                    // encrypt oto
                    String encodedOtp = encodeOTP(generatedOTP);
                    log.info("OTP encoded");

                    // check if otp already exist,may be user multiple time give validatni request
                    Optional.ofNullable(otpRepository.findByEmail(email)).ifPresentOrElse((existingOtp -> {
                        log.info("OTP record found with email: {} , so i will update new recored on exist record",
                                email);
                        existingOtp.setOtp(encodedOtp);
                        otpRepository.save(existingOtp);
                    }), () -> {
                        OTP savedOTPData = otpRepository.save(
                                new OTP(0, existingCustomer.getEmail(), encodedOtp, 0, false));
                        log.info("OTP saved successfully in DB with ID: {}", savedOTPData.getId());

                    });

                    // Send OTP via email
                    emailService.sendOtp(email, generatedOTP);
                    log.info("OTP sent to {}", existingCustomer.getEmail());
                    return "OTP sent successfully";
                })
                .orElseThrow(() -> {
                    log.info("User does not exist with the given email -> {}", email);
                    return new ResourceNotExistException("User does not exist with the given email -> " + email);
                });
    }

    public String validateOTP(String email, String otp) {
        log.info("Validating OTP for email: {}", email);

        return Optional.ofNullable(otpRepository.findByEmail(email))
                .map(existingOtp -> {
                    log.info("OTP record found with email: {}", email);

                    if (existingOtp.getRetryCount() < 3) {
                
                        //  decode and validate OTP
                        if (!otp.equals(decodeOTP(existingOtp.getOtp()))) {
                            existingOtp.setRetryCount(existingOtp.getRetryCount() + 1);
                            otpRepository.save(existingOtp);
                            log.warn("Incorrect OTP for email: {}. Retry count updated to: {}", email,
                                    existingOtp.getRetryCount());
                            return "OTP is wrong. Please try again.";
                        }
                        // if OTP is valid
                        else {
                            otpRepository.deleteById(existingOtp.getId());
                            log.info("OTP validated successfully for email: {}", email);
                            return "OTP is valid";
                        }

                    } else {
                        existingOtp.setLocked(true);
                        otpRepository.save(existingOtp);
                        log.error("Max attempts reached for email: {}", email);
                        return "You tried max attempts, contact admin to unlock account!";
                    }
                })
                .orElseThrow(() -> {
                    log.error("User does not exist with the given email -> {}", email);
                    return new ResourceNotExistException("User does not exist with the given email -> " + email);
                });
    }

    @Override
    public String admin(String email) {

        return Optional.ofNullable(otpRepository.findByEmail(email))
                .map(existingOtp -> {
                    log.info("OTP record found with email: {}", email);
                    if (existingOtp.isLocked()) {
                        existingOtp.setLocked(false);
                        existingOtp.setRetryCount(0);
                        otpRepository.save(existingOtp);
                        log.info("OTP unlocked successfully for email: {}", email);
                        return "OTP unlocked successfully";
                    } else {
                        log.error("OTP is not locked for email: {}", email);
                        return "OTP is not locked";
                    }

                }).orElseThrow(
                        () -> new ResourceNotExistException("User does not exist with the given email -> " + email));
    }

    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a random 6-digit number
        return String.valueOf(otp);
    }

    public String decodeOTP(String otp) {
        return new String(Base64.getDecoder().decode(otp.getBytes()));
    }

    public String encodeOTP(String otp) {
        return Base64.getEncoder().encodeToString(otp.getBytes());
    }

}