package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CartProductsRequestDto;

import com.KrishiG.services.CartProductService;
import com.KrishiG.services.CartService;
import com.KrishiG.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {

    Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/addProduct")
    public ResponseEntity<Object> addProductToCart(@Valid @RequestBody CartProductsRequestDto cartProductsRequestDto,
                                                   @RequestHeader Map<String, String> header)
    {
        Long userId = jwtUtil.getUserIdFromToken(header);
        logger.info("Inside CartController for addProduct ");
        ResponseEntity<Object> responseEntity = cartProductService.addProductToCart(cartProductsRequestDto, userId);
        if(responseEntity != null) {
            logger.info("products added successfully !!");
        }else {
            logger.error("Something went wrong inside cartProductService");
        }
        return responseEntity;
    }

    @DeleteMapping("/{cartId}/{cartProductId}")
    public ResponseEntity<Object> deleteProductFromCart(@PathVariable("cartId") Long cartId,
                                                        @PathVariable("cartProductId") Long cartProductId,
                                                        @RequestHeader Map<String, String> header)
    {
        logger.info("Inside DeleteController to delete product from cart");

        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = cartProductService.deleteProductFromCart(cartId, cartProductId);
        if(responseEntity != null) {
            logger.info("products deleted from cart successfully !!");
        }else {
            logger.error("Something went wrong inside cartProductService");
        }
        return responseEntity;
    }

    @GetMapping("getCartProduct/{cartId}")
    public ResponseEntity<Object> getCartProducts(@PathVariable("cartId") Long cartId, @RequestHeader Map<String, String> header)
    {
        logger.info("Inside getCartProduct from Cart");

        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> cartProducts = cartProductService.getCartProducts(cartId);
        if(cartProducts != null) {
            logger.info("Sent all products available in cart!!");
        }else {
            logger.error("Something went wrong inside cartProductService");
        }
        return cartProducts;
    }
}
