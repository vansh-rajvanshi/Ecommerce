package com.example.E_commerce.Project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class OtpService {

    // Stores OTPs against email addresses
    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();
    // Stores OTP expiry times against email addresses
    private final ConcurrentHashMap<String, LocalDateTime> otpExpiryStore = new ConcurrentHashMap<>();
    // Expiry duration for OTP (in minutes)
    private static final int OTP_EXPIRY_DURATION = 5;

    @Autowired
    private EmailService emailService; // Injecting EmailService

    public boolean sendOtp(String email) {
        try {
            String otp = generateOtp(email);
            // Sending OTP to the user's email using EmailService
            String subject = "Your OTP Code";
            String message = "Your OTP is: " + otp + "\nThis OTP will expire in " + OTP_EXPIRY_DURATION + " minutes.";
            emailService.sendEmail(email, subject, message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        LocalDateTime expiryTime = otpExpiryStore.get(email);

        // Check if OTP exists, hasn't expired, and matches the provided OTP
        if (storedOtp == null || expiryTime == null || LocalDateTime.now().isAfter(expiryTime)) {
            return false; // OTP is invalid or expired
        }

        boolean isValid = storedOtp.equals(otp);

        if (isValid) {
            clearOtp(email); // Clear OTP after successful verification
        }

        return isValid;
    }

    private String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // Generate a 6-digit OTP
        otpStore.put(email, otp);
        otpExpiryStore.put(email, LocalDateTime.now().plusMinutes(OTP_EXPIRY_DURATION)); // Set expiry time
        return otp;
    }

    private void clearOtp(String email) {
        otpStore.remove(email);
        otpExpiryStore.remove(email);
    }
}
