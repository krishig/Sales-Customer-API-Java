package com.KrishiG.services;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.response.CartProductResponseDto;
import com.KrishiG.dtos.response.TotalCartProductResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartProductService {

    public TotalCartProductResponseDto addProductToCart(CartProductsRequestDto cartProductsRequestDto);

    public ResponseEntity<Object> deleteProductFromCart(Long cartId, Long productCartId);

    public TotalCartProductResponseDto getCartProducts(Long cartId);
}
