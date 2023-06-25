package com.KrishiG.services;

import com.KrishiG.dtos.CustomerDto;
import lombok.Lombok;

import java.util.List;

public interface CustomerService {

    //create
    CustomerDto createCustomer(CustomerDto customerDto);

    //update
    CustomerDto updateCustomer(Long customerId, CustomerDto customerDto);

    //getAll
    List<CustomerDto> getAllCustomers();

    //delete
    void deleteCustomer(Long customerId);
}
