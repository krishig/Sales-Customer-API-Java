package com.KrishiG.services;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.entities.Customer;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    public ResponseEntity<Object> bookOrder(OrderRequestDto orderRequestDto, Long userId);
    public ResponseEntity<Object> removeCartProduct(Customer customer);
}
