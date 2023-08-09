package com.KrishiG.controllers;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.response.CustomerAddressResponseDto;
import com.KrishiG.dtos.response.OrderResponseDto;
import com.KrishiG.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/book")
    public ResponseEntity<Object> saveOrder(@RequestBody OrderRequestDto orderRequestDto) {
        ResponseEntity<Object> responseEntity = orderService.bookOrder(orderRequestDto);
        orderService.removeCartProduct(orderRequestDto.getCustomerId());
        return responseEntity;
    }
}
