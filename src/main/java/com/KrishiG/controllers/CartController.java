package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CartProductsDto;
import com.KrishiG.dtos.request.CustomerCartDto;
import com.KrishiG.dtos.request.ProductDto;
import com.KrishiG.dtos.response.CartProductResponseDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;
import com.KrishiG.services.CartProductService;
import com.KrishiG.services.CartService;
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
    public ResponseEntity<CustomerCartResponseDto> addCart(@RequestBody CustomerCartDto customerCartDto)
    {
        CustomerCartResponseDto responseDto = cartService.addCart(customerCartDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/addProducts")
    public ResponseEntity<List<CartProductResponseDto>> addProductToCart(@RequestBody List<CartProductsDto> cartProductsDto)
    {
        List<CartProductResponseDto> cartProductResponseDto = cartProductService.addProductToCart(cartProductsDto);
        return new ResponseEntity(cartProductResponseDto,HttpStatus.OK);
    }

}
