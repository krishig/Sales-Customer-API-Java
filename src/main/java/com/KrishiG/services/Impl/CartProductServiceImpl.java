package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CartProductsRequestDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CartProductResponseDto> addProductToCart(List<CartProductsRequestDto> cartProductsRequestDto) {

        List<CartProducts> cartProducts = convertDtoToEntityList(cartProductsRequestDto);
        List<CartProducts> savedCartProduct = cartProductRepository.saveAll(cartProducts);
        List<CartProductResponseDto> savedCartProductResponseDto = convertEntityToDtoList(savedCartProduct);
        return savedCartProductResponseDto;
    }

    private List<CartProductResponseDto> convertEntityToDtoList(List<CartProducts> cartProducts) {

        List<CartProductResponseDto> cartProductResponseDtoList = new ArrayList<CartProductResponseDto>();
        for(CartProducts cartProducts1 : cartProducts) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            Optional<Product> product = productRepository.findById(cartProducts1.getProduct().getId());
            if (!product.isEmpty()) {
                productResponseDto.setId(product.get().getId());
                productResponseDto.setProductName(product.get().getProductName());
                productResponseDto.setProductDescription(product.get().getProductDescription());
            }

            CartProductResponseDto cartProductResponseDto = new CartProductResponseDto();
            cartProductResponseDto.setProductQuantity(cartProducts1.getProductQuantity());
            cartProductResponseDto.setActualPrice(cartProducts1.getActualPrice());
            cartProductResponseDto.setPurchasePrice(cartProducts1.getPurchasePrice());
            cartProductResponseDto.setCreatedBy(cartProducts1.getCreatedBy());
            cartProductResponseDto.setCreatedDate(cartProducts1.getCreatedDate());
            cartProductResponseDto.setModifiedDate(cartProducts1.getModifiedDate());
            cartProductResponseDto.setModifiedBy(cartProducts1.getModifiedBY());
            cartProductResponseDtoList.add(cartProductResponseDto);
        }
        return cartProductResponseDtoList;
    }

    private List<CartProducts> convertDtoToEntityList(List<CartProductsRequestDto> cartProductsRequestDto) {

        List<CartProducts> cartProductsList = new ArrayList<CartProducts>();
        for(CartProductsRequestDto cartProductsRequestDto1 : cartProductsRequestDto) {
            CustomerCart customerCart = new CustomerCart();
            customerCart.setId(cartProductsRequestDto1.getCartId());
            CartProducts cartProducts = new CartProducts();
            cartProducts.setCart(customerCart);
           // cartProducts.setProductId(cartProductsDto1.getProductId());
            cartProducts.setProductQuantity(cartProductsRequestDto1.getProductQuantity());
            cartProducts.setActualPrice(cartProductsRequestDto1.getActualPrice());
            cartProducts.setPurchasePrice(cartProductsRequestDto1.getPurchasePrice());
            cartProducts.setCreatedBy(cartProductsRequestDto1.getCreatedBy());
            cartProducts.setCreatedDate(cartProductsRequestDto1.getCreatedDate());
            cartProducts.setModifiedBY(cartProductsRequestDto1.getModifiedBY());
            cartProducts.setModifiedDate(cartProductsRequestDto1.getModifiedDate());
            cartProductsList.add(cartProducts);
        }
        return cartProductsList;
    }

}
