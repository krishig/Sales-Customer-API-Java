package com.KrishiG.dtos.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj, boolean error, boolean success) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("error",error);
        map.put("success", success);
        map.put("data", responseObj);

        return new ResponseEntity<Object>(map,status);
    }

}
