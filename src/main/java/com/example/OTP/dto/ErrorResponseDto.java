package com.example.OTP.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Response object that encapsulates a message and HTTP status.")
public class ErrorResponseDto {

    @Schema(description = "API path invoked by client", example = "/api/customers")
    private String apiPath;

    @Schema(description = "Error code representing the error happened", example = "404 NOT_FOUND")
    private HttpStatus errorCode;

    @Schema(description = "Error message representing the error happened", example = "Customer not found with the given ID")
    private String errorMessage;

    @Schema(description = "Time representing when the error happened", example = "2025-04-07T10:40:00")
    private LocalDateTime errorTime;
}