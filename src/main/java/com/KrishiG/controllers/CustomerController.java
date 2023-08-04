package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.dtos.response.CustomerAddressResponseDto;
import com.KrishiG.dtos.response.CustomerResponseDto;
import com.KrishiG.dtos.response.PageableResponse;
import com.KrishiG.responsesApiMessages.ApiResponseMessage;
import com.KrishiG.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    //create
    @PostMapping("/addCustomer")
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        CustomerResponseDto createdCustomer = customerService.createCustomer(customerRequestDto);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto, @PathVariable("customerId") Long customerId) {
        CustomerResponseDto updatedCustomer = customerService.updateCustomer(customerId, customerRequestDto);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponseMessage> deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.deleteCustomer(customerId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("Customer Deleted Successfully")
                                                                 .status(HttpStatus.OK).success(true).build();

        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //getAll
    @GetMapping("/")
    public ResponseEntity<PageableResponse<CustomerResponseDto>> getAllCustomers( @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                                  @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                                                  @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                                                  @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir)
    {
        PageableResponse<CustomerResponseDto> response =  customerService.getAllCustomers(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/findByMobile/{mobileNumber}")
    public ResponseEntity<List<CustomerResponseDto>> getCustomerByMobile(@PathVariable String mobileNumber) {
        List<CustomerResponseDto> lstOfCustomer = customerService.getCustomerByMobile(mobileNumber);
        return new ResponseEntity<>(lstOfCustomer,HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        CustomerResponseDto customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }


    @PostMapping("/address")
    public ResponseEntity<CustomerAddressResponseDto> addAddressOfCustomer(@Valid @RequestBody CustomerAddressRequestDto addressDto) {
        CustomerAddressResponseDto addressDtos = customerService.addCustomerAddress(addressDto);
        return new ResponseEntity<>(addressDtos, HttpStatus.CREATED);
    }
}
