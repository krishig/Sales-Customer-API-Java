package com.KrishiG.services;

import com.KrishiG.dtos.request.OrderRequestDto;
import com.KrishiG.dtos.response.OrderResponseDto;
import com.KrishiG.enitites.Customer;

public interface OrderService {
    public OrderResponseDto bookOrder(OrderRequestDto orderRequestDto);
    public void removeCartProduct(Customer customer);
}
