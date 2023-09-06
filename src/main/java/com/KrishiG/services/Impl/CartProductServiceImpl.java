package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CartProductsRequestDto;
import com.KrishiG.dtos.response.*;
import com.KrishiG.entities.CartProducts;
import com.KrishiG.entities.CustomerCart;
import com.KrishiG.entities.Image;
import com.KrishiG.entities.Product;
import com.KrishiG.exception.ResourceNotFoundException;
import com.KrishiG.repositories.CartProductRepository;
import com.KrishiG.repositories.ProductImageRepository;
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

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public ResponseEntity<Object> addProductToCart(CartProductsRequestDto cartProductsRequestDto, Long userId) {
        CartProducts cartProducts = convertDtoToEntity(cartProductsRequestDto);
        CartProducts SavedCartProduct = null;
        if (cartProducts.getId() != null && cartProducts.getCart() != null) {
            Optional<CartProducts> getCartProduct = cartProductRepository.findCartProductsByIdAndCart(cartProducts.getId(), cartProducts.getCart());
            if (getCartProduct == null) {
                throw new ResourceNotFoundException("Cart product not found ");
            }
            CartProducts cartProducts1 = getCartProduct.get();
            cartProducts1.setProductQuantity(cartProductsRequestDto.getProductQuantity());
            cartProducts1.setModifiedBY(userId);
            SavedCartProduct = cartProductRepository.save(cartProducts1);
        } else {
            cartProducts.setCreatedBy(userId);
            SavedCartProduct = cartProductRepository.save(cartProducts);
        }
        List<CartProducts> lstCartProducts = cartProductRepository.findByCart(SavedCartProduct.getCart());
        TotalCartProductResponseDto totalCartProductResponseDto = convertEntityToDtoList(lstCartProducts);
        String message = "Product added to cart !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, totalCartProductResponseDto, false, true);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> deleteProductFromCart(Long cartId, Long cartProductId) {
        Optional<CartProducts> cartProducts = cartProductRepository.findById(cartProductId);
        if (cartProducts.isPresent()) {
            cartProductRepository.deleteById(cartProductId);
            String deleteMessage = "product is deleted successfully from cart!";
            CustomerCart customerCart = new CustomerCart();
            customerCart.setId(cartId);
            List<CartProducts> lstCartProducts = cartProductRepository.findByCart(customerCart);
            TotalCartProductResponseDto totalCartProductResponseDto = convertEntityToDtoList(lstCartProducts);
            ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(deleteMessage, HttpStatus.OK, totalCartProductResponseDto, false, true);
            return responseEntity;
        } else {
            throw new ResourceNotFoundException("Can't able to delete product from cart");
        }
    }

    @Override
    public ResponseEntity<Object> getCartProducts(Long cartId) {
        CustomerCart cart = new CustomerCart();
        cart.setId(cartId);
        List<CartProducts> lstCartProducts = cartProductRepository.findByCart(cart);
        TotalCartProductResponseDto totalCartProductResponseDto = convertEntityToDtoList(lstCartProducts);
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, totalCartProductResponseDto, false, true);
        return responseEntity;
    }

    private TotalCartProductResponseDto convertEntityToDtoList(List<CartProducts> cartProducts) {
        TotalCartProductResponseDto totalCartProductResponseDto = new TotalCartProductResponseDto();
        List<CartProductResponseDto> cartProductResponseDtoList = new ArrayList<CartProductResponseDto>();
        Double totalPrice = 0.0;
        for (CartProducts cartProducts1 : cartProducts) {
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
                double discountPrice = PriceCalculation.calculationDiscountPrice(product.get().getActualPrice(), product.get().getDiscount());
                cartProductResponseDto.setDiscountPrice(discountPrice);
                double productsPrice = discountPrice * cartProducts1.getProductQuantity();
                cartProductResponseDto.setTotalProductDiscountPrice(productsPrice);
                totalPrice = totalPrice + productsPrice;
                ProductImageResponse productImageResponse = getImageForProduct(product.get());
                productResponseDto.setProductImageResponse(productImageResponse);
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
        if (product.isPresent()) {
            cartProducts.setProduct(product.get());
        }
        cartProducts.setId(cartProductsRequestDto.getId());
        cartProducts.setProductQuantity(cartProductsRequestDto.getProductQuantity());
        cartProducts.setCreatedBy(cartProductsRequestDto.getCreatedBy());
        cartProducts.setCreatedDate(cartProductsRequestDto.getCreatedDate());
        cartProducts.setModifiedBY(cartProductsRequestDto.getModifiedBy());
        cartProducts.setModifiedDate(cartProductsRequestDto.getModifiedDate());
        return cartProducts;
    }

    public ProductImageResponse getImageForProduct(Product product){
        List<Image> image = productImageRepository.findByProduct(product);
        ProductImageResponse productImage = new ProductImageResponse();
        productImage.setImageName(image.get(0).getImageName());
        productImage.setImageUrl(image.get(0).getImageUrl());
        return productImage;
    }

}
