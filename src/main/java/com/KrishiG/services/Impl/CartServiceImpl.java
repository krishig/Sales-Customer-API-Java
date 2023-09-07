package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CustomerCartRequestDto;
import com.KrishiG.dtos.response.ApiResponse;
import com.KrishiG.dtos.response.CustomerCartResponseDto;
import com.KrishiG.entities.Customer;
import com.KrishiG.entities.CustomerCart;
import com.KrishiG.repositories.CartRepository;
import com.KrishiG.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Override
    public ResponseEntity<Object> addCart(CustomerCartRequestDto customerCartRequestDto) {

        logger.info("Inside addCart service !!");

        CustomerCart customerCart = convertDtoToEntity(customerCartRequestDto);
        CustomerCart customerCartDB = cartRepository.save(customerCart);
        CustomerCartResponseDto response = convertEntityToDto(customerCartRequestDto.getCustomerId(),customerCartDB);
        String message = "Cart Added !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, response, false, true);
        if(responseEntity != null) {
            logger.info("cartAdded successfully !!");
        }else {
            logger.error("Something went wrong");
        }
        return responseEntity;
    }

    private CustomerCart convertDtoToEntity(CustomerCartRequestDto customerCartRequestDto) {

        logger.info("Inside convertDtoToEntity!!");

        CustomerCart customerCart = new CustomerCart();
        Customer customer = new Customer();
        customer.setId(customerCartRequestDto.getCustomerId());
        customerCart.setCustomer(customer);
        customerCart.setCreatedBy(customerCartRequestDto.getCreatedBy());
        customerCart.setModifiedBy(customerCartRequestDto.getModifiedBy());

        logger.info("Exiting from convertDtoToEntity");

        return customerCart;
    }

    private CustomerCartResponseDto convertEntityToDto(Long customerId, CustomerCart customerCart) {

        logger.info("Inside convertEntityToDto!!");

        CustomerCartResponseDto customerCartRes = new CustomerCartResponseDto();
        customerCartRes.setId(customerCart.getId());
        customerCartRes.setCustomerId(customerId);
        customerCartRes.setCreatedDate(customerCart.getCreatedDate());
        customerCartRes.setCreatedBy(customerCart.getCreatedBy());
        customerCartRes.setModifiedDate(customerCart.getModifiedDate());
        customerCartRes.setModifiedBy(customerCart.getModifiedBy());

        logger.info("Exiting from convertEntityToDto");

        return customerCartRes;
    }

}
