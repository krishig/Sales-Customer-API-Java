package com.KrishiG.services;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.dtos.response.CustomerAddressResponseDto;
import com.KrishiG.dtos.response.CustomerResponseDto;
import com.KrishiG.dtos.response.PageableResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface CustomerService {

    //create
    ResponseEntity<Object> createCustomer(CustomerRequestDto customerRequestDto, Long userId);

    //update
    ResponseEntity<Object> updateCustomer(Long customerId, CustomerRequestDto customerRequestDto, Long userId);

    //getAll
    ResponseEntity<Object>  getAllCustomers(int pageNumber, int pageSize,String sortBy, String sortDir);

    //delete
    ResponseEntity<Object> deleteCustomer(Long customerId);

    ResponseEntity<Object> addCustomerAddress(CustomerAddressRequestDto addressDto, Long userId);

    ResponseEntity<Object> getCustomerByMobile(int pageNumber, int pageSize,String sortBy, String sortDir,String mobileNumber);

    ResponseEntity<Object> getCustomerById(Long id);
}
