package com.KrishiG.services;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.response.CartProductResponseDto;
import com.KrishiG.dtos.response.TotalCartProductResponseDto;

import java.util.List;

public interface CartProductService {

    public TotalCartProductResponseDto addProductToCart(CartProductsRequestDto cartProductsRequestDto);

    public TotalCartProductResponseDto updateQuantityForProduct(Long cartId, Long cartProductId, int quantity, Double price);

    public TotalCartProductResponseDto getCartProducts(Long cartId);
}
