package com.KrishiG.services;

import com.KrishiG.dtos.request.CartProductsDto;
import com.KrishiG.dtos.response.CartProductResponseDto;

import java.util.List;

public interface CartProductService {

    public List<CartProductResponseDto> addProductToCart(List<CartProductsDto> cartProductsDto);
}
