package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.dtos.response.CustomerAddressResponseDto;
import com.KrishiG.dtos.response.CustomerResponseDto;
import com.KrishiG.dtos.response.PageableResponse;
import com.KrishiG.responsesApiMessages.ApiResponseMessage;
import com.KrishiG.services.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    //create
    @PostMapping("/addCustomer")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        ResponseEntity<Object> responseEntity = customerService.createCustomer(customerRequestDto);
        return responseEntity;
    }

    //update
    @PutMapping("/{customerId}")
    public ResponseEntity<Object> updateCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto, @PathVariable("customerId") Long customerId) {
        ResponseEntity<Object> responseEntity = customerService.updateCustomer(customerId, customerRequestDto);
        return responseEntity;
    }

    //delete
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("customerId") Long customerId) {
        ResponseEntity<Object> responseEntity = customerService.deleteCustomer(customerId);
        return responseEntity;
    }

    //getAll
    @GetMapping("/")
    public ResponseEntity<Object> getAllCustomers( @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                                  @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                                                  @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                                                  @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir)
    {
        ResponseEntity<Object> allCustomers = customerService.getAllCustomers(pageNumber, pageSize, sortBy, sortDir);
        return allCustomers;
    }

    @GetMapping("/findByMobile/{mobileNumber}")
    public ResponseEntity<Object> getCustomerByMobile(@PathVariable String mobileNumber) {
        ResponseEntity<Object> responseEntity = customerService.getCustomerByMobile(mobileNumber);
        return responseEntity;
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long id) {
        ResponseEntity<Object> responseEntity = customerService.getCustomerById(id);
        return responseEntity;
    }

    @PostMapping("/address")
    public ResponseEntity<Object> addAddressOfCustomer(@Valid @RequestBody CustomerAddressRequestDto addressDto) {
        ResponseEntity<Object> responseEntity = customerService.addCustomerAddress(addressDto);
        return responseEntity;
    }
}
