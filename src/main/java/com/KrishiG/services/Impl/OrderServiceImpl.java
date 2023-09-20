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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final DecimalFormat disFormat = new DecimalFormat("#.##");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @Override
    public ResponseEntity<Object> bookOrder(OrderRequestDto orderRequestDto, Long userId) {

        logger.info("Inside BookOrder");
        Optional<CustomerAddress> address = addressRepository.findById(orderRequestDto.getAddressId());
        if(address.isPresent()) {
            DeliveryAddress deliveryAddress = saveDeliveryAddress(address.get());
            orderRequestDto.setAddressId(deliveryAddress.getId());
        }
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
               case OPEN : order.setStatus(Status.PACKAGING);
                           break;
               case PACKAGING: order.setStatus(Status.READY_TO_DISPATCH);
                               break;
               case READY_TO_DISPATCH: order.setStatus(Status.OUT_OF_DELIVERED);
                                       order.setOutOfDeliveryDate(new Date());
                                       break;
               case OUT_OF_DELIVERED :  order.setStatus(Status.DELIVERED);
                                        order.setClosedDate(new Date());
                                        break;
           }

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
    public ResponseEntity<Object> getAllOrdersDetails(Date date, Status status, int pageNumber, int pageSize, String sortBy, String sortDir) {
        OrderDetailsAndCountResponseDto orderDetailsAndCountResponseDto = new OrderDetailsAndCountResponseDto();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(date);
        Page<Orders> page = null;
        int outOfDeliveryCount = 0;
        int deliveredCount = 0;
        int orderCount = 0;
        int pendingDeliveryCount = 0;

        outOfDeliveryCount = getCountForOutOfDelivery(currentDate);
        deliveredCount = getCountForDelivered(currentDate);
        orderCount = getCountForOrder(currentDate);
        pendingDeliveryCount = getCountForPendingDelivery(currentDate);
        Double totalPrice = orderRepository.getTotalPrice(currentDate, Status.DELIVERED.toString());
        if(totalPrice==null) {
            totalPrice = 0.0;
        }
        orderDetailsAndCountResponseDto.setOutOfDeliveryCount(outOfDeliveryCount);
        orderDetailsAndCountResponseDto.setDeliveredCount(deliveredCount);
        orderDetailsAndCountResponseDto.setTotalOrderCount(orderCount);
        orderDetailsAndCountResponseDto.setPendingDeliveryCount(pendingDeliveryCount);
        orderDetailsAndCountResponseDto.setTotalCash(Double.valueOf(disFormat.format(totalPrice)));

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        switch (status) {
            case OUT_OF_DELIVERED : page = orderRepository.findByOutOfDeliveryDateAndStatus(currentDate, status.toString(), pageable);
                break;
            case DELIVERED: page = orderRepository.findByClosedDateAndStatus(currentDate, status.toString(), pageable);
                break;
            case OPEN: page = orderRepository.findByCreatedDateAndStatus(currentDate, status.toString(), pageable);
            break;
            default: page = orderRepository.findByPendingDeliveredByDate(currentDate, Status.OUT_OF_DELIVERED.toString(), pageable);
        }
        //pageNumber starts from 1

        List<Orders> ordersList = page.getContent();
        List<OrderResponseDto> dtoList = ordersList.stream().map(orders1 -> convertEntityToDto(orders1)).collect(Collectors.toList());
        PageableResponse<OrderResponseDto> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        orderDetailsAndCountResponseDto.setOrderResponseDto(response);
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, orderDetailsAndCountResponseDto, false, true);
        logger.info("Sent all the customer from getAllCustomer ServiceImpl");
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getOrderDetailsBySalesUserId(int pageNumber, int pageSize, String sortBy, String sortDir, Long userId) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Orders> page =  orderRepository.findByCreatedBy(userId, pageable);
        if (page.isEmpty()) {
            logger.info("Order is not available for userId");
            throw new ResourceNotFoundException("Order is not available for userId");
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
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getOrderByOrderNumber(int pageNumber, int pageSize, String sortBy, String sortDir,String orderId) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Orders> page = orderRepository.findByOrderIdLike("%" + orderId + "%", pageable);
        if (page.isEmpty()) {
            logger.info("Order is not available with the given Order No");
            throw new ResourceNotFoundException("Order is not available with the given Order No");
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
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getSearchOrderDetails(int pageNumber,
                                                        int pageSize, String sortBy,
                                                        String sortDir,
                                                        String orderId,
                                                        Date createdDate,
                                                        Date outOfDeliveryDate,
                                                        Date deliveredDate, String status) {
        String orderId1 = orderId != "" ? "%" + orderId+ "%" : null;
        String status1 = status != "" ? status : null;
        String createdDate1 = createdDate != null ? "%" + simpleDateFormat.format(createdDate) + "%" : null;
        String outOfDeliveryDate1 = outOfDeliveryDate != null ? "%" + simpleDateFormat.format(outOfDeliveryDate) + "%" : null;
        String deliveredDate1 = deliveredDate != null ? "%" + simpleDateFormat.format(deliveredDate) + "%" : null;
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Orders> page = orderRepository.getOrderDetailsByKeyword(orderId1, createdDate1, outOfDeliveryDate1, deliveredDate1, status1, pageable);
        if (page.isEmpty()) {
            logger.info("Order is not available with the given Order No");
            throw new ResourceNotFoundException("Order is not available with the given Order No");
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
        return responseEntity;
    }

    private int getCountForOutOfDelivery(String date) {
        return orderRepository.getCountForOutOfDeliveryByDateAndStatus(date, Status.OUT_OF_DELIVERED.toString());
    }
    private int getCountForDelivered(String date) {
        return orderRepository.getCountForDeliveredByDateAndStatus(date, Status.DELIVERED.toString());
    }

    private int getCountForOrder(String date) {
        return orderRepository.getCountForOrderByDateAndStatus(date, Status.OPEN.toString());
    }

    private int getCountForPendingDelivery(String  date) {
        return orderRepository.getCountForPendingDeliveredByDate(date, Status.OUT_OF_DELIVERED.toString());
    }
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
        orderItems.setDiscount(cartProducts.getProduct().getDiscount());
        orderItems.setActualPrice(cartProducts.getProduct().getActualPrice());
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
        Optional<DeliveryAddress> address = deliveryAddressRepository.findById(orders.getAddressId());
        if(address.isPresent()) {
            CustomerAddressResponseDto customerAddressResponseDto = convertEntityToDtoForAddress(address.get());
            orderResponseDto.setAddressResponseDto(customerAddressResponseDto);
        }

        orderResponseDto.setCreatedBy(orders.getCreatedBy());
        orderResponseDto.setCreatedDate(orders.getCreatedDate());
        orderResponseDto.setClosedDate(orders.getCreatedDate());
        orderResponseDto.setClosedDate(orders.getClosedDate());
        orderResponseDto.setModifiedBy(orders.getModifiedBy());
        orderResponseDto.setModifiedDate(orders.getModifiedDate());
        List<OrderItems> orderItems = orderItemsRepository.findByOrders(orders);
        if(!orderItems.isEmpty()) {
            //List<OrderItemsRes> dtoList = orderItems.stream().map(orderItem -> convertEntityToDTOForOrderItems(orderItem)).collect(Collectors.toList());
            List<ProductResponseDto> productResponseDtos = new ArrayList<>();
            for(OrderItems ordersItem : orderItems) {
                ProductResponseDto productResponseDto = convertEntityToDTOForProduct(ordersItem);
                productResponseDtos.add(productResponseDto);
            }
            orderResponseDto.setProductResponseDtos(productResponseDtos);
        }
        logger.info("Exiting from convertDtoToEntity");
        return orderResponseDto;
    }

    public DeliveryAddress saveDeliveryAddress(CustomerAddress address) {
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setHouseNumber(address.getHouseNumber());
        deliveryAddress.setStreetName(address.getStreetName());
        deliveryAddress.setVillageName(address.getVillageName());
        deliveryAddress.setDistrict(address.getDistrict());
        deliveryAddress.setState(address.getState());
        deliveryAddress.setPostalCode(address.getPostalCode());
        DeliveryAddress deliveryAddressDB = deliveryAddressRepository.save(deliveryAddress);
        return deliveryAddressDB;
    }

    private ProductResponseDto convertEntityToDTOForProduct(OrderItems orderItems) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(orderItems.getProduct().getId());
        productResponseDto.setProductName(orderItems.getProduct().getProductName());
        productResponseDto.setProductDescription(orderItems.getProduct().getProductDescription());
        productResponseDto.setSubCategory(orderItems.getProduct().getSubCategory().getId());
        productResponseDto.setBrandId(orderItems.getProduct().getBrand().getId());
        ProductImageResponse productImageResponse = getImageForProduct(orderItems.getProduct().getImages());
        productResponseDto.setProductImageResponse(productImageResponse);
        productResponseDto.setQuantity(orderItems.getQuantity());
        productResponseDto.setPrice(orderItems.getActualPrice());
        productResponseDto.setDiscount(orderItems.getDiscount());
        Double tempTotalDiscountPrice= Double.valueOf(disFormat.format(orderItems.getTotalDiscountPrice()));
        productResponseDto.setTotalPrice(tempTotalDiscountPrice);
        Double tempPriceAfterDiscount= Double.valueOf(disFormat.format(orderItems.getTotalDiscountPrice()));
        productResponseDto.setDiscountPrice(tempPriceAfterDiscount);
        return productResponseDto;
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

    private CustomerAddressResponseDto convertEntityToDtoForAddress(DeliveryAddress address) {
        logger.info("Inside convertEntityToDtoForAddress");
        CustomerAddressResponseDto customerAddressResponseDto = new CustomerAddressResponseDto();
        customerAddressResponseDto.setHouseNumber(address.getHouseNumber());
        customerAddressResponseDto.setId(address.getId());
        customerAddressResponseDto.setStreetName(address.getStreetName());
        customerAddressResponseDto.setDistrict(address.getDistrict());
        customerAddressResponseDto.setVillageName(address.getVillageName());
        customerAddressResponseDto.setState(address.getState());
        customerAddressResponseDto.setPostalCode(address.getPostalCode());
        customerAddressResponseDto.setCreatedBy(address.getCreatedBy());
        customerAddressResponseDto.setCreatedDate(address.getCreatedDate());
        customerAddressResponseDto.setModifiedBy(address.getModifiedBy());
        customerAddressResponseDto.setModifiedDate(address.getModifiedDate());
        logger.info("Exiting from convertEntityToDtoForAddress");
        return customerAddressResponseDto;
    }

    private ProductImageResponse getImageForProduct(List<Image> images){
        ProductImageResponse productImage = new ProductImageResponse();
        if(!images.isEmpty()) {
            productImage.setImageName(images.get(0).getImageName());
            productImage.setImageUrl(images.get(0).getImageUrl());
        }
        return productImage;
    }
}
