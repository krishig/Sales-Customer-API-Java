package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CustomerCartDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;
import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.CustomerCart;
import com.KrishiG.repositories.CartRepository;
import com.KrishiG.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CustomerCartResponseDto addCart(CustomerCartDto customerCartDto) {
        CustomerCart customerCart = convertDtoToEntity(customerCartDto);
        CustomerCart customerCartDB = cartRepository.save(customerCart);
        CustomerCartResponseDto response = convertEntityToDto(customerCartDto.getCustomerId(),customerCartDB);
        return response;
    }

    private CustomerCart convertDtoToEntity(CustomerCartDto customerCartDto) {
        CustomerCart customerCart = new CustomerCart();
        Customer customer = new Customer();
        customer.setId(customerCartDto.getCustomerId());
        customerCart.setCustomer(customer);
        customerCart.setCreatedBy(customerCartDto.getCreatedBy());
        customerCart.setModifiedBy(customerCartDto.getModifiedBy());
        return customerCart;
    }

    private CustomerCartResponseDto convertEntityToDto(Long customerId, CustomerCart customerCart) {
        CustomerCartResponseDto customerCartRes = new CustomerCartResponseDto();
        customerCartRes.setId(customerCart.getId());
        customerCartRes.setCustomerId(customerId);
        customerCartRes.setCreatedAt(customerCart.getCreatedDate());
        customerCartRes.setCreatedBy(customerCart.getCreatedBy());
        customerCartRes.setModifiedAt(customerCart.getModifiedDate());
        customerCartRes.setModifiedBy(customerCart.getModifiedBy());
        return customerCartRes;
    }

}
