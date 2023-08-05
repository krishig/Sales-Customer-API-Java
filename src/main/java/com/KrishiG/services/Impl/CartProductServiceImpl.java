package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.response.*;
import com.KrishiG.enitites.CartProducts;
import com.KrishiG.enitites.CustomerCart;
import com.KrishiG.enitites.Product;
import com.KrishiG.repositories.CartProductRepository;
import com.KrishiG.repositories.ProductRepository;
import com.KrishiG.services.CartProductService;
import com.KrishiG.util.PriceCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        CartProducts SavedCartProduct=null;
        if(cartProducts.getId()!=null && cartProducts.getCart()!=null) {
            Optional<CartProducts> getCartProduct = cartProductRepository.findCartProductsByIdAndCart(cartProducts.getId(), cartProducts.getCart());
            CartProducts cartProducts1 = getCartProduct.get();
            cartProducts1.setProductQuantity(cartProductsRequestDto.getProductQuantity());
            SavedCartProduct = cartProductRepository.save(cartProducts1);
        } else {
            SavedCartProduct = cartProductRepository.save(cartProducts);
        }
        List<CartProducts> lstCartProducts = cartProductRepository.findByCart(SavedCartProduct.getCart());
        TotalCartProductResponseDto totalCartProductResponseDto = convertEntityToDtoList(lstCartProducts);
        return totalCartProductResponseDto;
    }

    @Override
    public ResponseEntity<Object> deleteProductFromCart(Long cartId, Long cartProductId) {
        cartProductRepository.deleteById(cartProductId);
        String deleteMessage = "product is deleted successfully from cart!";
        CustomerCart customerCart = new CustomerCart();
        customerCart.setId(cartId);
        List<CartProducts> lstCartProducts = cartProductRepository.findByCart(customerCart);
        TotalCartProductResponseDto totalCartProductResponseDto = convertEntityToDtoList(lstCartProducts);
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(deleteMessage, HttpStatus.OK, totalCartProductResponseDto, false, true);
        return responseEntity;

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
                cartProductResponseDto.setDiscount(product.get().getDiscount());
                double discountPrice = PriceCalculation.calculationDiscountPrice(product.get().getActualPrice());
                cartProductResponseDto.setDiscountPrice(discountPrice);
                double productsPrice = discountPrice*cartProducts1.getProductQuantity();
                cartProductResponseDto.setTotalProductDiscountPrice(productsPrice);
                totalPrice = totalPrice + productsPrice;
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

    private CartProducts convertDtoToEntity(CartProductsRequestDto cartProductsRequestDto) {
            CustomerCart customerCart = new CustomerCart();
            customerCart.setId(cartProductsRequestDto.getCartId());
            CartProducts cartProducts = new CartProducts();
            cartProducts.setCart(customerCart);
            Optional<Product> product = productRepository.findById(cartProductsRequestDto.getProduct().getId());
            if(product.isPresent()) {
                cartProducts.setProduct(product.get());
            }
            cartProducts.setId(cartProductsRequestDto.getId());
            cartProducts.setProductQuantity(cartProductsRequestDto.getProductQuantity());
            cartProducts.setCreatedBy(cartProductsRequestDto.getCreatedBy());
            cartProducts.setCreatedDate(cartProductsRequestDto.getCreatedDate());
            cartProducts.setModifiedBY(cartProductsRequestDto.getModifiedBY());
            cartProducts.setModifiedDate(cartProductsRequestDto.getModifiedDate());
            return cartProducts;
    }

}
