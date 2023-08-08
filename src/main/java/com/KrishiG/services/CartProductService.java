package com.KrishiG.services;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.response.CartProductResponseDto;
import com.KrishiG.dtos.response.TotalCartProductResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartProductService {

    public ResponseEntity<Object> addProductToCart(CartProductsRequestDto cartProductsRequestDto);

    public ResponseEntity<Object> deleteProductFromCart(Long cartId, Long productCartId);

    public ResponseEntity<Object> getCartProducts(Long cartId);
}
