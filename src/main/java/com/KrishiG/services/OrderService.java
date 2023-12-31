package com.KrishiG.services;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.request.StatusRequestDto;
import com.KrishiG.entities.Customer;
import com.KrishiG.util.Status;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface OrderService {
    public ResponseEntity<Object> bookOrder(OrderRequestDto orderRequestDto, Long userId);

    public ResponseEntity<Object> removeCartProduct(Customer customer);

    public ResponseEntity<Object> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    public ResponseEntity<Object> getOrderById(Long orderId);

    public ResponseEntity<Object> updateStatusByOrderId(Long orderId, StatusRequestDto status);

    public ResponseEntity<Object> getOrderByCustomerId(int pageNumber,
                                                       int pageSize, String sortBy,
                                                       String sortDir, Long customerId);

    public ResponseEntity<Object> getAllOrdersDetails(Date dateTime, Status status, int pageNumber, int pageSize, String sortBy, String sortDir);

    public ResponseEntity<Object> getOrderDetailsBySalesUserId(int pageNumber, int pageSize, String sortBy, String sortDir, Long userId, Long customerId);

    public ResponseEntity<Object> getOrderByOrderNumber(int pageNumber, int pageSize, String sortBy, String sortDir, String orderNo);

    public ResponseEntity<Object> getSearchOrderDetails(int pageNumber,
                                                        int pageSize, String sortBy,
                                                        String sortDir,
                                                        String orderId,
                                                        Date createdDate,
                                                        Date outOfDeliveryDate,
                                                        Date deliveredDate, String status);
}
