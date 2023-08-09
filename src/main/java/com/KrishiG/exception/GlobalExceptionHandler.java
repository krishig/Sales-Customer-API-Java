package com.KrishiG.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Handle Method Argument Not Valid Exception
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.stream().forEach(ObjectError -> {
            String message = ObjectError.getDefaultMessage();
            String field = ((FieldError) ObjectError).getField();
            response.put(field, message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Handler method for Resource Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseExceptionMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        ApiResponseExceptionMessage response = ApiResponseExceptionMessage
                .builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .success(false)
                .error(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Handler Method for SQLSyntaxErrorException
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<ApiResponseExceptionMessage> resourceNotFoundExceptionHandler(SQLSyntaxErrorException ex) {
        ApiResponseExceptionMessage response = ApiResponseExceptionMessage
                .builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .success(false)
                .error(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Handler method for HttpRequestMethodNotSupportedException
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        String message = "Method not supported";
        ApiResponseExceptionMessage errorResponse = new ApiResponseExceptionMessage(message,false,true,HttpStatus.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ApiResponseExceptionMessage> jwtTokenNotFountException(JwtTokenException ex) {
        ApiResponseExceptionMessage response = ApiResponseExceptionMessage
                .builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .success(false)
                .error(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
