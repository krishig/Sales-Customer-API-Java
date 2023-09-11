package com.KrishiG.services;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.request.StatusRequestDto;
import com.KrishiG.entities.Customer;
import com.KrishiG.util.Status;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface OrderService {
    public ResponseEntity<Object> bookOrder(OrderRequestDto orderRequestDto, Long userId);

    public ResponseEntity<Object> removeCartProduct(Customer customer);

    public ResponseEntity<Object> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    public ResponseEntity<Object> getOrderById(Long orderId);

    public ResponseEntity<Object> updateStatusByOrderId(Long orderId, StatusRequestDto status);

    public ResponseEntity<Object> getOrderByCustomerId(Long customerId);

    public ResponseEntity<Object> getAllOrdersDetails(LocalDateTime dateTime, Status status, int pageNumber, int pageSize,String sortBy, String sortDir);

    public ResponseEntity<Object> getOrderDetailsBySalesUserId(Long userId);
}
