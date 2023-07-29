package com.KrishiG.services;

import com.KrishiG.dtos.request.CustomerCartRequestDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;

public interface CartService {

    public CustomerCartResponseDto addCart(CustomerCartRequestDto customerCartRequestDto);
}
