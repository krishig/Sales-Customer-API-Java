package com.KrishiG.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseExceptionMessage {
    private String message;
    private boolean success;
    private boolean error;
    private HttpStatus httpStatus;

    public ApiResponseExceptionMessage(String message) {
    }
}
