package com.example.OTP.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Ensures 'id' is only serialized (output in response)
    @Schema(description = "Unique identifier for the customer. This field is generated and included only in responses.", example = "1")
    private int id;

    @NotBlank(message = "First name must not be blank")
    @Schema(description = "First name of the customer.", example = "John")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Schema(description = "Last name of the customer.", example = "Doe")
    private String lastName;

    @Email(message = "Email must be a valid format")
    @NotBlank(message = "Email must not be blank")
    @Schema(description = "Email address of the customer.", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Age of the customer.", example = "30")
    @Min(value = 18, message = "Age must be at least 18")
    private int age;

    @NotBlank(message = "Address must not be blank")
    @Schema(description = "Address of the customer.", example = "123 Main Street, Springfield")
    private String address;

    @Schema(description = "Phone number of the customer.", example = "+1234567890")
    private String phone;
}