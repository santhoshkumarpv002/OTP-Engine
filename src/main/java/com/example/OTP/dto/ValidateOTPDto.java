package com.example.OTP.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO for validating OTP")
public class ValidateOTPDto {

    @Pattern(regexp = "\\d{6}", message = "OTP must be a 6-digit numeric value")
    @Schema(description = "The 6-digit numeric OTP sent to the user's email", 
            example = "123456", 
            required = true)
    private String otp;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    @Schema(description = "The user's email address", 
            example = "user@example.com", 
            required = true)
    private String email;
}