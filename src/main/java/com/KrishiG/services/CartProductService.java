package com.KrishiG.services;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.response.CartProductResponseDto;

import java.util.List;

public interface CartProductService {

    public List<CartProductResponseDto> addProductToCart(List<CartProductsRequestDto> cartProductsRequestDto);
}
