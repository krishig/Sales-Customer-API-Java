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
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartProductService cartProductService;

    @PostMapping("/addProduct")
    public ResponseEntity<TotalCartProductResponseDto> addProductToCart(@Valid @RequestBody CartProductsRequestDto cartProductsRequestDto)
    {
        TotalCartProductResponseDto totalCartProductResponseDto = cartProductService.addProductToCart(cartProductsRequestDto);
        return new ResponseEntity(totalCartProductResponseDto,HttpStatus.OK);
    }

    @PostMapping("/{cartId}/{cartProductId}/quantity")
    public ResponseEntity<TotalCartProductResponseDto> updateQuantityForProduct(@PathVariable("cartId") Long cartId,
                                                                                 @PathVariable("cartProductId") Long cartProductId,
                                                                                 @PathVariable("quantity") int quantity,
                                                                                 @PathVariable("price") Double price)
    {
        TotalCartProductResponseDto totalCartProductResponseDto = cartProductService.updateQuantityForProduct(cartId, cartProductId, quantity, price);
        return new ResponseEntity(totalCartProductResponseDto,HttpStatus.OK);
    }

    @GetMapping("getCartProduct/{cartId}")
    public ResponseEntity<TotalCartProductResponseDto> getCartProducts(@PathVariable("cartId") Long cartId)
    {
        TotalCartProductResponseDto totalCartProductResponseDto = cartProductService.getCartProducts(cartId);
        return new ResponseEntity(totalCartProductResponseDto,HttpStatus.OK);
    }

}
