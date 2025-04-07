package com.example.OTP.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OTP.dto.ErrorResponseDto;
import com.example.OTP.dto.ResponseDto;
import com.example.OTP.dto.SendOTPDto;
import com.example.OTP.dto.ValidateOTPDto;
import com.example.OTP.entity.Customer;
import com.example.OTP.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/customer")

public class MainController {

        private final CustomerService customerService;

        public MainController(CustomerService customerService) {
                this.customerService = customerService;
        }

        @PostMapping("/new")
        @Operation(summary = "Create a new customer", description = "This endpoint allows the creation of a new customer. The customer information is provided in the request body.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Customer created successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))
                        }),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)

                        )),

        })

        public ResponseEntity<Customer> newCustomer(
                        @Valid @RequestBody Customer customer) {
                log.info("Received HTTP POST request to create a new customer.");
                Customer savedCustomer = customerService.saveCustomer(customer);
                log.info("HTTP POST request successful. Customer created with ID: {}", savedCustomer.getId());
                return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        }

        @PostMapping("/sendOtp")
        @Operation(summary = "Send OTP to a customer", description = "This endpoint sends an OTP to the customer's email address provided in the request body.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "OTP sent successfully", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
                        }),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        public ResponseEntity<ResponseDto> sendOtp(
                        @RequestBody @Valid SendOTPDto req) {
                log.info("Received HTTP POST request to send OTP from {}", req.getEmail());
                String message = customerService.sendOTP(req.getEmail());
                log.info("OTP sent to {}", req.getEmail());
                return ResponseEntity.ok(new ResponseDto(HttpStatus.OK, message));
        }

        @PostMapping("/validateOtp")
        @Operation(summary = "Validate a given OTP", description = "This endpoint validates the OTP provided by the user against the database.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "OTP validated successfully"),
                        @ApiResponse(responseCode = "400", description = "OTP not valid or expired", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        public ResponseEntity<ResponseDto> validateOtp(@Valid @RequestBody ValidateOTPDto data) {
                log.info("Received HTTP POST request to validate OTP.");
                // Extract OTP from request
                String  message = customerService.validateOTP(data.getEmail(), data.getOtp());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK, message));

        }

        @GetMapping("/admin/{email}")
        @Operation(
            summary = "Admin Actions by Email",
            description = "This endpoint allows an admin to perform actions or retrieve information based on the email provided in the URL path."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request processed successfully", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User does not exist with the provided email", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        public ResponseEntity<ResponseDto> admin(@PathVariable String email) {
            String message = customerService.admin(email);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK, message));
        }

}
