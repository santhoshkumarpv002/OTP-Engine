package com.example.OTP.service;



import com.example.OTP.entity.Customer;


public  interface EmailService {

    public String sendOtp(String email, String otp) ;
    public String sendAccountCreationEmail(Customer customer);

}