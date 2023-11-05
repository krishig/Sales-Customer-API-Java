package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CartProductsRequestDto;

import com.KrishiG.services.CartProductService;
import com.KrishiG.services.CartService;
import com.KrishiG.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="CartController",description = "APIs for CartController!!")
public class CartController {

    Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/addProduct")
    @Operation(summary = "API for add product to cart ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Success !! | Ok"),
            @ApiResponse(responseCode = "401", description = "Not Authorized !!")
    })
    public ResponseEntity<Object> addProductToCart(@Valid @RequestBody CartProductsRequestDto cartProductsRequestDto,
                                                   @RequestHeader Map<String, String> header)
    {
        Long userId = jwtUtil.getUserIdFromToken(header);
        logger.info("POST CALL API for Add Product:>>>>/cart/addProduct");
        ResponseEntity<Object> responseEntity = cartProductService.addProductToCart(cartProductsRequestDto, userId);
        if(responseEntity != null) {
            logger.info("products added successfully !!");
        }else {
            logger.error("Something went wrong inside cartProductService");
        }
        return responseEntity;
    }

    @DeleteMapping("/{cartId}/{cartProductId}")
    @Operation(description = "API for delete product from cart")
    public ResponseEntity<Object> deleteProductFromCart(@PathVariable("cartId") Long cartId,
                                                        @PathVariable("cartProductId") Long cartProductId,
                                                        @RequestHeader Map<String, String> header)
    {
        logger.info("DELETE CALL API for delete product:>>>>/cart/{cartId}/{cartProductId}");

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
    @Operation(description = "API for getCartProduct from Cart")
    public ResponseEntity<Object> getCartProducts(@PathVariable("cartId") Long cartId, @RequestHeader Map<String, String> header)
    {
        logger.info("GET CALL API for get cart's product:>>>/cart/getCartProduct/{cartId}");

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
