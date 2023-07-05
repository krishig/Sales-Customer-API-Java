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
    public List<CartProductResponseDto> addProductToCart(List<CartProductsDto> cartProductsDto) {

        List<CartProducts> cartProducts = convertDtoToEntityList(cartProductsDto);
        List<CartProducts> savedCartProduct = cartProductRepository.saveAll(cartProducts);
        List<CartProductResponseDto> savedCartProductResponseDto = convertEntityToDtoList(savedCartProduct);
        return savedCartProductResponseDto;
    }

    private List<CartProductResponseDto> convertEntityToDtoList(List<CartProducts> cartProducts) {

        List<CartProductResponseDto> cartProductResponseDtoList = new ArrayList<CartProductResponseDto>();
        for(CartProducts cartProducts1 : cartProducts) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            Optional<Product> product = productRepository.findById(cartProducts1.getProductId());
            if (!product.isEmpty()) {
                productResponseDto.setId(product.get().getId());
                productResponseDto.setProductName(product.get().getProductName());
                productResponseDto.setProductDescription(product.get().getProductDescription());
            }

            CartProductResponseDto cartProductResponseDto = new CartProductResponseDto();
            cartProductResponseDto.setProductQuantity(cartProducts1.getProductQuantity());
            cartProductResponseDto.setActualPrice(cartProducts1.getActualPrice());
            cartProductResponseDto.setDiscount(cartProducts1.getDiscount());
            cartProductResponseDto.setPurchasePrice(cartProducts1.getPurchasePrice());
            cartProductResponseDto.setTotalAmount(cartProducts1.getTotalAmount());
            cartProductResponseDto.setCreatedBy(cartProducts1.getCreatedBy());
            cartProductResponseDto.setCreatedDate(cartProducts1.getCreatedDate());
            cartProductResponseDto.setModifiedDate(cartProducts1.getModifiedDate());
            cartProductResponseDto.setModifiedBy(cartProducts1.getModifiedBY());
            cartProductResponseDtoList.add(cartProductResponseDto);
        }
        return  cartProductResponseDtoList;
    }

    private List<CartProducts> convertDtoToEntityList(List<CartProductsDto> cartProductsDto) {

        List<CartProducts> cartProductsList = new ArrayList<CartProducts>();
        for(CartProductsDto cartProductsDto1 : cartProductsDto) {
            CustomerCart customerCart = new CustomerCart();
            customerCart.setId(cartProductsDto1.getCartId());
            CartProducts cartProducts = new CartProducts();
            cartProducts.setCart(customerCart);
            cartProducts.setProductId(cartProductsDto1.getProductId());
            cartProducts.setProductQuantity(cartProductsDto1.getProductQuantity());
            cartProducts.setActualPrice(cartProductsDto1.getActualPrice());
            cartProducts.setDiscount(cartProductsDto1.getDiscount());
            cartProducts.setPurchasePrice(cartProductsDto1.getPurchasePrice());
            cartProducts.setCreatedBy(cartProductsDto1.getCreatedBy());
            cartProducts.setCreatedDate(cartProductsDto1.getCreatedDate());
            cartProducts.setModifiedBY(cartProductsDto1.getModifiedBY());
            cartProducts.setModifiedDate(cartProductsDto1.getModifiedDate());
            cartProductsList.add(cartProducts);
        }
        return cartProductsList;
    }

}
