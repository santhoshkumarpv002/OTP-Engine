package com.example.OTP.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistException extends RuntimeException{
    public ResourceAlreadyExistException(String msg){
        super(msg);
    }
    
}
