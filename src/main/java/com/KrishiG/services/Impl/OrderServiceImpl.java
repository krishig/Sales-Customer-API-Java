package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.request.StatusRequestDto;
import com.KrishiG.dtos.response.*;
import com.KrishiG.entities.*;
import com.KrishiG.exception.ResourceNotFoundException;
import com.KrishiG.repositories.*;
import com.KrishiG.services.OrderService;
import com.KrishiG.util.PriceCalculation;
import com.KrishiG.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public ResponseEntity<Object> bookOrder(OrderRequestDto orderRequestDto, Long userId) {

        logger.info("Inside BookOrder");

        Orders orders = convertDtoToEntity(orderRequestDto, userId);
        Orders savedOrder = orderRepository.save(orders);
        if (savedOrder != null) {
            Optional<CustomerCart> customerCart = cartRepository.findByCustomer(savedOrder.getCustomerId());
            if (customerCart.isPresent()) {
                List<CartProducts> cartProducts = cartProductRepository.findByCart(customerCart.get());
                if (!cartProducts.isEmpty()) {
                    for (CartProducts cartProducts1 : cartProducts) {
                        OrderItems orderItems = setOrderItems(savedOrder, cartProducts1, userId);
                        orderItemsRepository.save(orderItems);
                    }
                }
            }
        } else {
            logger.info("Some thing went wrong!!");
            throw new ResourceNotFoundException("Order Booking Fail !!");
        }
        OrderResponseDto orderResponseDto = convertEntityToDto(savedOrder);
        String message = "Order Booked !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, orderResponseDto, false, true);
        logger.info("Exiting from BookOrder");
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> removeCartProduct(Customer customer) {
        logger.info("Inside removeCartProduct");
        Optional<CustomerCart> cart = cartRepository.findByCustomer(customer);
        List<CartProducts> cartProducts = cartProductRepository.findByCart(cart.get());
        if (!cartProducts.isEmpty()) {
            cartProducts.stream().forEach(a -> {
                cartProductRepository.delete(a);
            });
        } else {
            logger.info("Something went wrong");
            throw new ResourceNotFoundException("Can't Remove Product from cart");
        }
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, null, false, true);
        logger.info("Exiting from removeCartProduct");
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        //pageNumber starts from 1
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Orders> page = orderRepository.findAll(pageable);
        if (page.isEmpty()) {
            logger.info("Order is not available");
            throw new ResourceNotFoundException("Order is not available");
        }
        List<Orders> orders = page.getContent();
        List<OrderResponseDto> dtoList = orders.stream().map(orders1 -> convertEntityToDto(orders1)).collect(Collectors.toList());

        PageableResponse<OrderResponseDto> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, response, false, true);
        logger.info("Sent all the Orders");
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getOrderById(Long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order is not available with the given Id"));
        OrderResponseDto orderResponseDto = convertEntityToDto(orders);
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, orderResponseDto, false, true);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> updateStatusByOrderId(Long orderId, StatusRequestDto status) {
        ResponseEntity<Object> responseEntity = null;
        Optional<Orders> dbOrders = orderRepository.findById(orderId);
        if(dbOrders.isPresent()) {
           Orders order = dbOrders.get();
           switch (status.getStatus()) {
               case OUT_OF_DELIVERED : order.setOutOfDeliveryDate(new Date());
                                        break;
               case DELIVERED: order.setClosedDate(new Date());
                                break;
           }
           order.setStatus(status.getStatus());
           Orders updatedOrder = orderRepository.save(order);
           OrderResponseDto orderResponseDto = convertEntityToDto(updatedOrder);
            responseEntity = ApiResponse.generateResponse(null, HttpStatus.CREATED, orderResponseDto, false, true);
        }

        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getOrderByCustomerId(Long customerId) {
        List<Orders> orders = new ArrayList<>();
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()) {
            orders = orderRepository.findByCustomerId(customer.get());
        }
        List<OrderResponseDto> dtoList = orders.stream().map(orders1 -> convertEntityToDto(orders1)).collect(Collectors.toList());
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, dtoList, false, true);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getAllOrdersDetails(LocalDateTime dateTime, Status status, int pageNumber, int pageSize,String sortBy, String sortDir) {
        OrderDetailsAndCountResponseDto orderDetailsAndCountResponseDto = new OrderDetailsAndCountResponseDto();
        /*Page<Orders> page = null;
        int outOfDeliveryCount = 0;
        int deliveredCount = 0;
        int orderCount = 0;
        int pendingDeliveryCount = 0;
        outOfDeliveryCount = getCountForOutOfDelivery(dateTime);
        deliveredCount = getCountForDelivered(dateTime);
        orderCount = getCountForOrder(dateTime);
        pendingDeliveryCount = getCountForPendingDelivery(dateTime);
        orderDetailsAndCountResponseDto.setOutOfDeliveryCount(outOfDeliveryCount);
        orderDetailsAndCountResponseDto.setDeliveredCount(deliveredCount);
        orderDetailsAndCountResponseDto.setTotalOrderCount(orderCount);

        switch (status) {
            case OUT_OF_DELIVERED : page = orderRepository.findByOutOfDeliveryDateAndStatus(dateTime, status);
                break;
            case DELIVERED: page = orderRepository.findByClosedDateAndStatus(dateTime, status);
                break;
            case OPEN: page = orderRepository.findByCreatedDateAndStatus(dateTime, status);
            default: page = orderRepository.findByPendingDeliveredByDate(dateTime);
        }
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        //pageNumber starts from 1
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        List<Orders> ordersList = page.getContent();
        List<OrderResponseDto> dtoList = ordersList.stream().map(orders1 -> convertEntityToDto(orders1)).collect(Collectors.toList());
        PageableResponse<OrderResponseDto> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        orderDetailsAndCountResponseDto.setOrderResponseDto(response);*/
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, orderDetailsAndCountResponseDto, false, true);
        logger.info("Sent all the customer from getAllCustomer ServiceImpl");
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getOrderDetailsBySalesUserId(Long userId) {
        List<Orders> lstOfOrders =  orderRepository.findByCreatedBy(userId);
        List<OrderResponseDto> dtoList = lstOfOrders.stream().map(orders1 -> convertEntityToDto(orders1)).collect(Collectors.toList());
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, dtoList, false, true);
        return responseEntity;
    }

    /*private int getCountForOutOfDelivery(LocalDateTime dateTime) {
        return orderRepository.getCountForOutOfDeliveryByDateAndStatus(dateTime, Status.OUT_OF_DELIVERED);
    }
    private int getCountForDelivered(LocalDateTime dateTime) {
        return orderRepository.getCountForDeliveredByDateAndStatus(dateTime, Status.DELIVERED);
    }

    private int getCountForOrder(LocalDateTime dateTime) {
        return orderRepository.getCountForDeliveredByDateAndStatus(dateTime, Status.OPEN);
    }

    private int getCountForPendingDelivery(LocalDateTime dateTime) {
        return orderRepository.getCountForPendingDeliveredByDate(dateTime);
    }*/
   /* public PaymentMethod savePaymentMethod(OrderRequestDto orderRequestDto) {
        logger.info("Inside savePaymentMethod");
        PaymentMethod save = paymentMethodRepository.save(orderRequestDto.getPaymentMethod());
        if (save == null) {
            logger.info("Something went wrong");
            throw new ResourceNotFoundException("Payment Not Saved ");
        } else {
            logger.info("Exiting from savePaymentMethod");
            return save;
        }
    }*/

    private OrderItems setOrderItems(Orders orders, CartProducts cartProducts, Long userId) {
        logger.info("Inside setOrderItems");
        OrderItems orderItems = new OrderItems();
        orderItems.setOrders(orders);
        List<Product> lstProducts = new ArrayList<>(1);
        lstProducts.add(cartProducts.getProduct());
        orderItems.setProduct(cartProducts.getProduct());
        Double discountPrice = PriceCalculation.calculationDiscountPrice(cartProducts.getProduct().getActualPrice(), cartProducts.getProduct().getDiscount());
        Double totalProductDiscountPrice = discountPrice * cartProducts.getProductQuantity();
        orderItems.setTotalDiscountPrice(totalProductDiscountPrice);
        orderItems.setPriceAfterDiscount(discountPrice);
        orderItems.setQuantity(cartProducts.getProductQuantity());
        orderItems.setCreatedBy(userId);
        logger.info("Exiting from setOrderItems");
        return orderItems;
    }

    private Orders convertDtoToEntity(OrderRequestDto orderRequestDto, Long userId) {
        logger.info("Inside convertDtoToEntity");
        Orders orders = new Orders();
        orders.setOrderId(generateOrderNumber(userId));
        orders.setCustomerId(orderRequestDto.getCustomerId());
        orders.setPaymentMethod(orderRequestDto.getPaymentMethod());
        orders.setTotalPrice(orderRequestDto.getTotalPrice());
        orders.setStatus(orderRequestDto.getStatus());
        orders.setContactNumber(orderRequestDto.getContactNumber());
        orders.setAddressId(orderRequestDto.getAddressId());
        orders.setCreatedBy(userId);
        logger.info("Exiting from convertDtoToEntity");
        return orders;
    }

    private OrderResponseDto convertEntityToDto(Orders orders) {
        logger.info("Inside convertEntityToDto");
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(orders.getId());
        orderResponseDto.setOrderId(orders.getOrderId());
        orderResponseDto.setCustomerId(orders.getCustomerId());
        orderResponseDto.setPaymentMethod(orders.getPaymentMethod());
        orderResponseDto.setStatus(orders.getStatus());
        orderResponseDto.setTotalPrice(orders.getTotalPrice());
        orderResponseDto.setContactNumber(orders.getContactNumber());
        orderResponseDto.setAddressId(orders.getAddressId());
        orderResponseDto.setCreatedBy(orders.getCreatedBy());
        orderResponseDto.setClosedDate(orders.getCreatedDate());
        orderResponseDto.setClosedDate(orders.getClosedDate());
        orderResponseDto.setModifiedBy(orders.getModifiedBy());
        orderResponseDto.setModifiedDate(orders.getModifiedDate());
        logger.info("Exiting from convertDtoToEntity");
        return orderResponseDto;
    }

    private String generateOrderNumber(Long userId) {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(dtf);
        String currentDateAndTime = getExtractNumberFromDate(date);
        String s = "ORD" + random.nextInt(1000) + currentDateAndTime + userId;
        return s;
    }

    public static String getExtractNumberFromDate(String date) {
        String dateStr = date.replaceAll("[/:\\W]","");
        return dateStr;
    }
}
