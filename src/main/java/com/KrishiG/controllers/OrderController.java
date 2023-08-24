package com.KrishiG.controllers;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.request.StatusRequestDto;
import com.KrishiG.services.OrderService;
import com.KrishiG.util.JwtUtil;
import com.KrishiG.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/book")
    public ResponseEntity<Object> saveOrder(@RequestBody OrderRequestDto orderRequestDto,
                                            @RequestHeader Map<String, String> header) {
        logger.info("Inside OrderController ");

        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = orderService.bookOrder(orderRequestDto, userId);
        orderService.removeCartProduct(orderRequestDto.getCustomerId());

        logger.info("Exiting from OrderController ");

        return responseEntity;
    }

    //getAll orders
    @GetMapping("/allOrders")
    public ResponseEntity<Object> getAllOrders(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
                                               @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                               @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                               @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
                                               @RequestHeader Map<String, String> header) {

        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return allOrders;
    }

    //get order By orderId
    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable("orderId") Long orderId,
                                               @RequestHeader Map<String, String> header) {
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> order = orderService.getOrderById(orderId);
        return order;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Object> getOrderByCustomerId(@PathVariable("customerId") Long customerId,
                                               @RequestHeader Map<String, String> header) {
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> order = orderService.getOrderByCustomerId(customerId);
        return order;
    }

    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<Object> updateStatusByOrderId(@RequestBody StatusRequestDto status, @PathVariable("orderId") Long orderId,
                                                        @RequestHeader Map<String, String> header) {
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> order = orderService.updateStatusByOrderId(orderId, status);
        return order;
    }
}
