package com.KrishiG.controllers;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.request.StatusRequestDto;
import com.KrishiG.services.OrderService;
import com.KrishiG.util.JwtUtil;
import com.KrishiG.util.Status;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
        logger.info("POST CALL API for Book ORDER:>>>/order/book");

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

        logger.info("POST CALL API for get all orders:>>>/order/allOrders");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return allOrders;
    }

    //get order By orderId
    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable("orderId") Long orderId,
                                               @RequestHeader Map<String, String> header) {
        logger.info("GET CALL API for get order by id:>>>/order/{orderId}");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> order = orderService.getOrderById(orderId);
        return order;
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getOrderById(@PathParam("orderNo") String orderId,
                                               @RequestParam(value = "pageNumber",defaultValue = "1",required = false) int pageNumber,
                                               @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                               @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                               @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir,
                                               @RequestHeader Map<String, String> header) {
        logger.info("GET CALL API for filter order by orderId:>>>/order/filter");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> order = orderService.getOrderByOrderNumber(pageNumber, pageSize, sortBy, sortDir, orderId);
        return order;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Object> getOrderByCustomerId(@PathVariable("customerId") Long customerId,
                                                       @RequestParam(value = "pageNumber",defaultValue = "1",required = false) int pageNumber,
                                                       @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                       @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                       @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir,
                                                       @RequestHeader Map<String, String> header) {
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> order = orderService.getOrderByCustomerId(pageNumber, pageSize, sortBy, sortDir, customerId);
        return order;
    }

    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<Object> updateStatusByOrderId(@RequestBody StatusRequestDto status, @PathVariable("orderId") Long orderId,
                                                        @RequestHeader Map<String, String> header) {
        logger.info("PUT CALL API for update status:>>>/order/updateStatus/{orderId}");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> order = orderService.updateStatusByOrderId(orderId, status);
        return order;
    }

    @GetMapping("/getDetails/all/{currentDate}")
    public ResponseEntity<Object> getOrderDetails(@PathVariable("currentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                  @RequestParam(value = "status", required = false, defaultValue = "OUT_OF_DELIVERED") Status status,
                                                  @RequestParam(value = "pageNumber",defaultValue = "1",required = false) int pageNumber,
                                                  @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                  @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                  @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir,
                                                  @RequestHeader Map<String, String> header) {
        logger.info("GET CALL API for get order details by current date:>>>/order/getDetails/all/{currentDate}");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> orders = orderService.getAllOrdersDetails(date, status, pageNumber, pageSize, sortBy, sortDir);
        return orders;
    }

    @GetMapping("/getAllOrder")
    public ResponseEntity<Object> getOrderDetails(@RequestParam(value = "customerId", required = false) Long customerId,
                                                  @RequestParam(value = "pageNumber",defaultValue = "1",required = false) int pageNumber,
                                                  @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                  @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                  @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir,
                                                  @RequestHeader Map<String, String> header) {
        logger.info("GET CALL API for get all order by user Id or customer Id :>>>/order/getAllOrder");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> response = orderService.getOrderDetailsBySalesUserId(pageNumber, pageSize, sortBy, sortDir, userId, customerId);
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearchOrderDetails(
                                                    @RequestParam(value = "pageNumber",defaultValue = "1",required = false) int pageNumber,
                                                    @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                    @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                    @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir,
                                                    @PathParam("orderId") String orderId,
                                                    @PathParam("createdDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date createdDate,
                                                    @PathParam("outOfDeliveryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date outOfDeliveryDate,
                                                    @PathParam("deliveredDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deliveredDate,
                                                    @PathParam("status") String status,
                                                    @RequestHeader Map<String, String> header) {
        logger.info("GET CALL API for search order by orderId, createdDate, outOfDeliveryDate, DeliveredDate, status :>>>/order/search");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> response = orderService.getSearchOrderDetails(pageNumber, pageSize, sortBy, sortDir, orderId, createdDate, outOfDeliveryDate, deliveredDate, status);
        return response;
    }
}
