package com.KrishiG.services;

import com.KrishiG.dtos.request.CustomerCartRequestDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;
import org.springframework.http.ResponseEntity;

public interface CartService {

    public ResponseEntity<Object> addCart(CustomerCartRequestDto customerCartRequestDto);
}
