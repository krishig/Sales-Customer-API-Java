package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.response.ApiResponse;
import com.KrishiG.dtos.response.OrderResponseDto;
import com.KrishiG.entities.*;
import com.KrishiG.exception.ResourceNotFoundException;
import com.KrishiG.repositories.*;
import com.KrishiG.services.OrderService;
import com.KrishiG.util.PriceCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

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


    @Override
    public ResponseEntity<Object> bookOrder(OrderRequestDto orderRequestDto) {
        PaymentMethod paymentMethod = savePaymentMethod(orderRequestDto);
        Orders orders = convertDtoToEntity(orderRequestDto, paymentMethod);
        Orders savedOrder = orderRepository.save(orders);
        if (savedOrder != null) {
            Optional<CustomerCart> customerCart = cartRepository.findByCustomer(savedOrder.getCustomerId());
            if (customerCart.isPresent()) {
                List<CartProducts> cartProducts = cartProductRepository.findByCart(customerCart.get());
                if (!cartProducts.isEmpty()) {
                    for (CartProducts cartProducts1 : cartProducts) {
                        OrderItems orderItems = setOrderItems(savedOrder, cartProducts1);
                        orderItemsRepository.save(orderItems);
                    }
                }
            }
        }
        else{
            throw new ResourceNotFoundException("Order Booking Fail !!");
        }
        OrderResponseDto orderResponseDto = convertEntityToDto(savedOrder);
        String message = "Order Booked !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, orderResponseDto, false, true);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> removeCartProduct(Customer customer) {
        Optional<CustomerCart> cart = cartRepository.findByCustomer(customer);
        List<CartProducts> cartProducts = cartProductRepository.findByCart(cart.get());
        if (!cartProducts.isEmpty()) {
            cartProducts.stream().forEach(a -> {
                cartProductRepository.delete(a);
            });
        }
        else{
            throw new ResourceNotFoundException("Can't Remove Product from cart");
        }
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, null, false, true);
        return responseEntity;
    }

    public PaymentMethod savePaymentMethod(OrderRequestDto orderRequestDto) {
        PaymentMethod save = paymentMethodRepository.save(orderRequestDto.getPaymentMethod());
        if(save == null){
            throw new ResourceNotFoundException("Payment Not Saved ");
        }else {
            return save;
        }
    }
    private OrderItems setOrderItems(Orders orders, CartProducts cartProducts) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrders(orders);
        orderItems.setProduct(cartProducts.getProduct());
        Double discountPrice = PriceCalculation.calculationDiscountPrice(cartProducts.getProduct().getActualPrice());
        Double totalProductDiscountPrice = discountPrice * cartProducts.getProductQuantity();
        orderItems.setTotalDiscountPrice(totalProductDiscountPrice);
        orderItems.setPriceAfterDiscount(discountPrice);
        orderItems.setQuantity(cartProducts.getProductQuantity());

        return orderItems;
    }

    private Orders convertDtoToEntity(OrderRequestDto orderRequestDto, PaymentMethod paymentMethod) {
        Orders orders = new Orders();
        orders.setOrderId(generateOrderNumber());
        orders.setCustomerId(orderRequestDto.getCustomerId());
        orders.setPaymentMethod(paymentMethod);
        orders.setTotalPrice(orderRequestDto.getTotalPrice());
        orders.setStatus(orderRequestDto.getStatus());
        orders.setContactNumber(orderRequestDto.getContactNumber());
        orders.setAddressId(orderRequestDto.getAddressId());
        return orders;
    }

    private OrderResponseDto convertEntityToDto(Orders orders) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(orders.getId());
        orderResponseDto.setOrderId(orders.getOrderId());
        return orderResponseDto;
    }

    private String generateOrderNumber() {
        Random random = new Random();
        String s = "OR" + random.nextInt(10000);
        return s;
    }
}
