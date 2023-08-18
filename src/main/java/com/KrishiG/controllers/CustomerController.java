package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.services.CustomerService;
import com.KrishiG.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/customer")
@CrossOrigin(origins={"*"}, maxAge = 4800)
public class CustomerController {

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtUtil jwtUtil;

    //create
    @PostMapping("/addCustomer")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto,
                                                 @RequestHeader Map<String, String> header) {
        logger.info("Inside createCustomer Controller");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = customerService.createCustomer(customerRequestDto, userId);
        logger.info("Customer Created Successfully!!");
        return responseEntity;
    }

    //update
    @PutMapping("/{customerId}")
    public ResponseEntity<Object> updateCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto, @PathVariable("customerId") Long customerId,
                                                 @RequestHeader Map<String, String> header) {
        logger.info("Inside updateCustomer Controller");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = customerService.updateCustomer(customerId, customerRequestDto, userId);
        logger.info("Customer Updated Successfully!!");
        return responseEntity;
    }

    //delete
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("customerId") Long customerId) {
        logger.info("Inside DeleteCustomer Controller");
        ResponseEntity<Object> responseEntity = customerService.deleteCustomer(customerId);
        logger.info("Customer Deleted Successfully with the CustomerId {}",customerId);
        return responseEntity;
    }

    //getAll
    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllCustomers( @RequestParam(value = "pageNumber",defaultValue = "1",required = false) int pageNumber,
                                                                                  @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                                                  @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                                                  @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir,
                                                                                  @RequestHeader Map<String, String> header)
    {
        Long userId = jwtUtil.getUserIdFromToken(header);
        logger.info("Inside GetAll controller for customer");
        ResponseEntity<Object> allCustomers = customerService.getAllCustomers(pageNumber, pageSize, sortBy, sortDir);
        if(allCustomers != null) {
            logger.info("Sent all customers");
        }else{
            logger.info("fail to fetch customers form the DB");
        }
        return allCustomers;
    }
    @GetMapping("/findByMobile/{mobileNumber}")
    public ResponseEntity<Object> getCustomerByMobile(@PathVariable String mobileNumber,
                                                      @RequestHeader Map<String, String> header) {
        logger.info("Inside Customer Controller of FindByMobile Number");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = customerService.getCustomerByMobile(mobileNumber);
        if(responseEntity != null){
            logger.info("sent all customer corresponding to the given number");
        }else{
            logger.info("Customer not available with the given MobileNumber");
        }
        return responseEntity;
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long id, @RequestHeader Map<String, String> header) {
        logger.info("Inside Customer Controller of findById");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = customerService.getCustomerById(id);
        if(responseEntity != null){
            logger.info("Sent customer related to Given Id");
        }else {
            logger.info("Customer Not found with the given Id");
        }
        return responseEntity;
    }

    @PostMapping("/address")
    public ResponseEntity<Object> addAddressOfCustomer(@Valid @RequestBody CustomerAddressRequestDto addressDto, @RequestHeader Map<String, String> header) {
        Long userId = jwtUtil.getUserIdFromToken(header);
        logger.info("Inside Customer Controller of Add Address of Customer");
        ResponseEntity<Object> responseEntity = customerService.addCustomerAddress(addressDto, userId);
        if(responseEntity != null){
            logger.info("Address saved for the Customer!!");
        }else{
            logger.info("something went wrong, not able to save customer Address");
        }
        return responseEntity;
    }
}
