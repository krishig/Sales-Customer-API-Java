package com.KrishiG.services;

import com.KrishiG.dtos.request.CustomerAddressDto;
import com.KrishiG.dtos.request.CustomerDto;
import com.KrishiG.dtos.response.CustomerAddressResponseDto;
import com.KrishiG.dtos.response.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    //create
    CustomerResponseDto createCustomer(CustomerDto customerDto);

    //update
    CustomerDto updateCustomer(Long customerId, CustomerDto customerDto);

    //getAll
    List<CustomerDto> getAllCustomers();

    //delete
    void deleteCustomer(Long customerId);

    public CustomerAddressResponseDto addCustomerAddress(CustomerAddressDto addressDto);
}
