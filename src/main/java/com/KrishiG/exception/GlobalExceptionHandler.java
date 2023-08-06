package com.KrishiG.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
                .httpStatus(HttpStatus.NOT_FOUND)
                .success(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
