package com.example.OTP.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO for sending OTP requests")
public class SendOTPDto {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address of the user where the OTP will be sent", 
            example = "user@example.com", 
            required = true)
    private String email;
}