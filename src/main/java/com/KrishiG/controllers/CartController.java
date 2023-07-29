package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.request.CustomerCartRequestDto;
import com.KrishiG.dtos.response.CartProductResponseDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;
import com.KrishiG.services.CartProductService;
import com.KrishiG.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartProductService cartProductService;

    @PostMapping("/addCustomerCart")
    public ResponseEntity<CustomerCartResponseDto> addCart(@Valid @RequestBody CustomerCartRequestDto customerCartRequestDto)
    {
        CustomerCartResponseDto responseDto = cartService.addCart(customerCartRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/addProducts")
    public ResponseEntity<List<CartProductResponseDto>> addProductToCart(@Valid @RequestBody List<CartProductsRequestDto> cartProductsRequestDto)
    {
        List<CartProductResponseDto> cartProductResponseDto = cartProductService.addProductToCart(cartProductsRequestDto);
        return new ResponseEntity(cartProductResponseDto,HttpStatus.OK);
    }

}
