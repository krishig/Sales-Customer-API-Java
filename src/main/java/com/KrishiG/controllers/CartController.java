package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CartProductsRequestDto;

import com.KrishiG.services.CartProductService;
import com.KrishiG.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/cart")
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
