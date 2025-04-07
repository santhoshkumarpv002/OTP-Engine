package com.example.OTP.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.OTP.entity.OTP;

public interface OTPRepository extends JpaRepository<OTP, Integer> {

    OTP findByEmail(String email);

}
