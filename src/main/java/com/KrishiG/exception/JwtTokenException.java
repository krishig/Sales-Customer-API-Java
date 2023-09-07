package com.KrishiG.exception;

import org.springframework.http.HttpStatus;

public class JwtTokenException extends RuntimeException{
    public JwtTokenException(String message) {
        super(message);
    }
}
