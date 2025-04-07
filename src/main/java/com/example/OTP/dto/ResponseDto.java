package com.example.OTP.dto;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
@Data @AllArgsConstructor
public class ResponseDto {

    @Schema(
            description = "Status code in the response",
            example = "200 OK" // Example status code
    )
    private HttpStatus statusCode;

    @Schema(
            description = "Status message in the response",
            example = "Operation completed successfully" // Example message
    )
    private String message;

}