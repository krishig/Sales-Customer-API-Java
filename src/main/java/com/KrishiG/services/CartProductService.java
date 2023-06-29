package com.KrishiG.services;

import com.KrishiG.dtos.request.CartProductsDto;
import com.KrishiG.dtos.response.CartProductResponseDto;

public interface CartProductService {

    public CartProductResponseDto addProductToCart(CartProductsDto cartProductsDto);
}
