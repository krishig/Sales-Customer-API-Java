package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CartProductsDto;
import com.KrishiG.dtos.response.CartProductResponseDto;
import com.KrishiG.dtos.response.ProductResponseDto;
import com.KrishiG.enitites.CartProducts;
import com.KrishiG.enitites.CustomerCart;
import com.KrishiG.enitites.Product;
import com.KrishiG.repositories.CartProductRepository;
import com.KrishiG.repositories.ProductRepository;
import com.KrishiG.services.CartProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartProductResponseDto addProductToCart(CartProductsDto cartProductsDto) {

        CartProducts cartProducts = convertDtoToEntity(cartProductsDto);
        CartProducts savedCartProduct = cartProductRepository.save(cartProducts);
        CartProductResponseDto savedCartProductResponseDto = convertEntityToDto(savedCartProduct);
        return savedCartProductResponseDto;
    }

    private CartProductResponseDto convertEntityToDto(CartProducts cartProducts) {

        ProductResponseDto productResponseDto = new ProductResponseDto();
        Optional<Product> product = productRepository.findById(cartProducts.getProductId());
        if(!product.isEmpty()) {
            productResponseDto.setId(product.get().getId());
            productResponseDto.setProductName(product.get().getProductName());
            productResponseDto.setProductDescription(product.get().getProductDescription());
        }

        CartProductResponseDto cartProductResponseDto = new CartProductResponseDto();
        cartProductResponseDto.setProductQuantity(cartProducts.getProductQuantity());
        cartProductResponseDto.setActualPrice(cartProducts.getActualPrice());
        cartProductResponseDto.setDiscount(cartProducts.getDiscount());
        cartProductResponseDto.setPurchasePrice(cartProducts.getPurchasePrice());
        cartProductResponseDto.setTotalAmount(cartProducts.getTotalAmount());
        cartProductResponseDto.setCreatedBy(cartProducts.getCreatedBy());
        cartProductResponseDto.setCreatedDate(cartProducts.getCreatedDate());
        cartProductResponseDto.setModifiedDate(cartProducts.getModifiedDate());
        cartProductResponseDto.setModifiedBy(cartProductResponseDto.getModifiedBy());
        return  cartProductResponseDto;
    }

    private CartProducts convertDtoToEntity(CartProductsDto cartProductsDto) {

        CustomerCart customerCart = new CustomerCart();
        customerCart.setId(cartProductsDto.getCartId());
        CartProducts cartProducts = new CartProducts();
        cartProducts.setCart(customerCart);
        cartProducts.setProductId(cartProductsDto.getProductId());
        cartProducts.setProductQuantity(cartProductsDto.getProductQuantity());
        cartProducts.setActualPrice(cartProductsDto.getActualPrice());
        cartProducts.setDiscount(cartProductsDto.getDiscount());
        cartProducts.setPurchasePrice(cartProductsDto.getPurchasePrice());
        cartProducts.setCreatedBy(cartProductsDto.getCreatedBy());
        cartProducts.setCreatedDate(cartProductsDto.getCreatedDate());
        cartProducts.setModifiedBY(cartProductsDto.getModifiedBY());
        cartProducts.setModifiedDate(cartProductsDto.getModifiedDate());
        return cartProducts;
    }

}
