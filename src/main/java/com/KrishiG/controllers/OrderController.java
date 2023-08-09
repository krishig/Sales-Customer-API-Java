package com.KrishiG.controllers;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.services.OrderService;
import com.KrishiG.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/book")
    public ResponseEntity<Object> saveOrder(@RequestBody OrderRequestDto orderRequestDto,
                                            @RequestHeader Map<String, String> header) {
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = orderService.bookOrder(orderRequestDto, userId);
        orderService.removeCartProduct(orderRequestDto.getCustomerId());
        return responseEntity;
    }
}
