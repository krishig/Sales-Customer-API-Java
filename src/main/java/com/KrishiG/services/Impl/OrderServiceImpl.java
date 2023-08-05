package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.response.OrderResponseDto;
import com.KrishiG.enitites.*;
import com.KrishiG.repositories.*;
import com.KrishiG.services.OrderService;
import com.KrishiG.util.PriceCalculation;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    public OrderResponseDto bookOrder(OrderRequestDto orderRequestDto) {
        PaymentMethod paymentMethod = savePaymentMethod(orderRequestDto);
        Orders orders = convertDtoToEntity(orderRequestDto, paymentMethod);
        Orders savedOrder = orderRepository.save(orders);
        if(savedOrder!=null) {
            Optional<CustomerCart> customerCart = cartRepository.findByCustomer(savedOrder.getCustomerId());
            if(customerCart.isPresent()) {
                List<CartProducts> cartProducts = cartProductRepository.findByCart(customerCart.get());
                if (!cartProducts.isEmpty()) {
                    for (CartProducts cartProducts1 : cartProducts) {
                        OrderItems orderItems = setOrderItems(savedOrder, cartProducts1);
                        orderItemsRepository.save(orderItems);
                    }
                }
            }
        }

        OrderResponseDto orderResponseDto = convertEntityToDto(savedOrder);
        return orderResponseDto;
    }

    @Override
    public void removeCartProduct(Customer customer) {
        Optional<CustomerCart> cart = cartRepository.findByCustomer(customer);
        List<CartProducts> cartProducts = cartProductRepository.findByCart(cart.get());
        if(!cartProducts.isEmpty()) {
            cartProducts.stream().forEach(a->{
                cartProductRepository.delete(a);
            });
        }
    }

    public PaymentMethod savePaymentMethod(OrderRequestDto orderRequestDto) {
        return paymentMethodRepository.save(orderRequestDto.getPaymentMethod());
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
        String s = "OR"+random.nextInt(10000);
        return s;
    }
}
