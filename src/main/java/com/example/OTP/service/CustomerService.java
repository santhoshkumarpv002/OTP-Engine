package com.example.OTP.service;

import com.example.OTP.entity.Customer;


public interface CustomerService {

    public Customer saveCustomer(Customer customer);

    public String sendOTP(String email);
    
    public String validateOTP(String email, String otp);
    
    public String admin(String email);
}