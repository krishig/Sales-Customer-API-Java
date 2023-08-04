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
        Double totalPrice = 0.0;
        for(CartProducts cartProducts1 : cartProducts) {
            CartProductResponseDto cartProductResponseDto = new CartProductResponseDto();
            ProductResponseDto productResponseDto = new ProductResponseDto();
            Optional<Product> product = productRepository.findById(cartProducts1.getProduct().getId());
            if (!product.isEmpty()) {
                productResponseDto.setId(product.get().getId());
                productResponseDto.setProductName(product.get().getProductName());
                productResponseDto.setProductDescription(product.get().getProductDescription());
                productResponseDto.setSubCategory(product.get().getSubCategory().getId());
                productResponseDto.setBrandId(product.get().getBrand().getId());
                productResponseDto.setPrice(product.get().getActualPrice());
                productResponseDto.setDiscount(product.get().getDiscount());
                double discountPrice = calculationDiscountPrice(product.get().getActualPrice());
                double productsPrice = discountPrice*cartProducts1.getProductQuantity();
                cartProductResponseDto.setDiscountPrice(productsPrice);
                totalPrice = totalPrice + discountPrice;
            }
            cartProductResponseDto.setId(cartProducts1.getId());
            cartProductResponseDto.setCartId(cartProducts1.getCart().getId());
            cartProductResponseDto.setProductResponseDto(productResponseDto);
            cartProductResponseDto.setProductQuantity(cartProducts1.getProductQuantity());
            cartProductResponseDto.setCreatedBy(cartProducts1.getCreatedBy());
            cartProductResponseDto.setCreatedDate(cartProducts1.getCreatedDate());
            cartProductResponseDto.setModifiedDate(cartProducts1.getModifiedDate());
            cartProductResponseDto.setModifiedBy(cartProducts1.getModifiedBY());
            cartProductResponseDtoList.add(cartProductResponseDto);
        }
        totalCartProductResponseDto.setCartProductResponseDtoList(cartProductResponseDtoList);
        totalCartProductResponseDto.setTotalPrice(totalPrice);
        return totalCartProductResponseDto;
    }


    private double calculationDiscountPrice(double actualPrice) {
        double discountPrice = actualPrice - (actualPrice*10)/100;
        return discountPrice;
    }

    private CartProducts convertDtoToEntity(CartProductsRequestDto cartProductsRequestDto) {
            CustomerCart customerCart = new CustomerCart();
            customerCart.setId(cartProductsRequestDto.getCartId());
            CartProducts cartProducts = new CartProducts();
            cartProducts.setCart(customerCart);
            Product product = new Product();
            product.setId(cartProductsRequestDto.getProduct().getId());
            cartProducts.setProduct(product);
            cartProducts.setProductQuantity(cartProductsRequestDto.getProductQuantity());
            cartProducts.setCreatedBy(cartProductsRequestDto.getCreatedBy());
            cartProducts.setCreatedDate(cartProductsRequestDto.getCreatedDate());
            cartProducts.setModifiedBY(cartProductsRequestDto.getModifiedBY());
            cartProducts.setModifiedDate(cartProductsRequestDto.getModifiedDate());
            return cartProducts;
    }

}
