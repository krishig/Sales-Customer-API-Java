package com.KrishiG.services;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.dtos.response.CustomerAddressResponseDto;
import com.KrishiG.dtos.response.CustomerResponseDto;
import com.KrishiG.dtos.response.PageableResponse;

import java.util.List;


public interface CustomerService {

    //create
    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);

    //update
    CustomerRequestDto updateCustomer(Long customerId, CustomerRequestDto customerRequestDto);

    //getAll
    PageableResponse<CustomerResponseDto> getAllCustomers(int pageNumber, int pageSize,String sortBy, String sortDir);

    //delete
    void deleteCustomer(Long customerId);

    public CustomerAddressResponseDto addCustomerAddress(CustomerAddressRequestDto addressDto, Long addressId);
}
