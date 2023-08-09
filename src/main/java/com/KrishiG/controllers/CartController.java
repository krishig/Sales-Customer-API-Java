package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.request.CustomerCartRequestDto;
import com.KrishiG.dtos.response.CartProductResponseDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;
import com.KrishiG.dtos.response.TotalCartProductResponseDto;
import com.KrishiG.services.CartProductService;
import com.KrishiG.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartProductService cartProductService;

    @PostMapping("/addProduct")
    public ResponseEntity<Object> addProductToCart(@Valid @RequestBody CartProductsRequestDto cartProductsRequestDto)
    {
        ResponseEntity<Object> responseEntity = cartProductService.addProductToCart(cartProductsRequestDto);
        return responseEntity;
    }

    @DeleteMapping("/{cartId}/{cartProductId}")
    public ResponseEntity<Object> deleteProductFromCart(@PathVariable("cartId") Long cartId,
                                                                                 @PathVariable("cartProductId") Long cartProductId)
    {
        ResponseEntity<Object> responseEntity = cartProductService.deleteProductFromCart(cartId, cartProductId);
        return responseEntity;
    }

    @GetMapping("getCartProduct/{cartId}")
    public ResponseEntity<Object> getCartProducts(@PathVariable("cartId") Long cartId)
    {
        ResponseEntity<Object> cartProducts = cartProductService.getCartProducts(cartId);
        return cartProducts;
    }

}
