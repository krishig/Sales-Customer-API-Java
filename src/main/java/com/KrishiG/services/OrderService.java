package com.KrishiG.services;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.response.OrderResponseDto;
import com.KrishiG.enitites.Customer;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    public ResponseEntity<Object> bookOrder(OrderRequestDto orderRequestDto);
    public ResponseEntity<Object> removeCartProduct(Customer customer);
}
