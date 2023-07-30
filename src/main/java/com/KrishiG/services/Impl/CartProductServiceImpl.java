package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.response.CartProductResponseDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;
import com.KrishiG.dtos.response.ProductResponseDto;
import com.KrishiG.dtos.response.TotalCartProductResponseDto;
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
import java.util.stream.Collectors;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public TotalCartProductResponseDto addProductToCart(CartProductsRequestDto cartProductsRequestDto) {

        CartProducts cartProducts = convertDtoToEntity(cartProductsRequestDto);
        cartProductRepository.save(cartProducts);
        List<CartProducts> lstCartProducts = cartProductRepository.findAll();
        TotalCartProductResponseDto totalCartProductResponseDto = convertEntityToDtoList(lstCartProducts);
        return totalCartProductResponseDto;
    }

    @Override
    public TotalCartProductResponseDto updateQuantityForProduct(Long cartId, Long cartProductId, int quantity, Double price) {
        List<CartProducts> cartProductByCart = cartProductRepository.getCartProductByCart(cartId);
            if (!cartProductByCart.isEmpty()) {
                for(CartProducts cartProducts : cartProductByCart) {
                    if(cartProducts.getId().equals(cartProductId)) {
                        cartProducts.setProductQuantity(quantity);
                        cartProducts.setPurchasePrice(price);
                        cartProductRepository.save(cartProducts);
                    }
                }

            }
        List<CartProducts> lstCartProducts = cartProductRepository.findAll();
        TotalCartProductResponseDto totalCartProductResponseDto = convertEntityToDtoList(lstCartProducts);
        return totalCartProductResponseDto;
    }

    @Override
    public TotalCartProductResponseDto getCartProducts(Long cartId) {
        List<CartProducts> lstCartProducts = cartProductRepository.findAll();
        TotalCartProductResponseDto totalCartProductResponseDto = convertEntityToDtoList(lstCartProducts);
        return totalCartProductResponseDto;
    }

    private TotalCartProductResponseDto convertEntityToDtoList(List<CartProducts> cartProducts) {
        TotalCartProductResponseDto totalCartProductResponseDto = new TotalCartProductResponseDto();
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
        Double totalPrice = cartProducts.stream().collect(Collectors.summingDouble(CartProducts::getPurchasePrice));
        totalCartProductResponseDto.setCartProductResponseDtoList(cartProductResponseDtoList);
        totalCartProductResponseDto.setTotalPrice(totalPrice);
        return totalCartProductResponseDto;
    }

    private CartProducts convertDtoToEntity(CartProductsRequestDto cartProductsRequestDto) {
            CustomerCart customerCart = new CustomerCart();
            customerCart.setId(cartProductsRequestDto.getCartId());
            CartProducts cartProducts = new CartProducts();
            cartProducts.setCart(customerCart);
            cartProducts.setProductQuantity(cartProductsRequestDto.getProductQuantity());
            cartProducts.setCreatedBy(cartProductsRequestDto.getCreatedBy());
            cartProducts.setCreatedDate(cartProductsRequestDto.getCreatedDate());
            cartProducts.setModifiedBY(cartProductsRequestDto.getModifiedBY());
            cartProducts.setModifiedDate(cartProductsRequestDto.getModifiedDate());
            return cartProducts;
    }

}
