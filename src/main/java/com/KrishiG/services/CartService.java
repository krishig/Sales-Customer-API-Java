package com.KrishiG.services;

import com.KrishiG.dtos.request.CustomerCartDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;

public interface CartService {

    public CustomerCartResponseDto addCart(CustomerCartDto customerCartDto);
}
