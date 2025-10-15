package com.medicconnect.services;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    private Random random = new Random();

    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    // TODO: Add sendEmail method to deliver OTP
}
