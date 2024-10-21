package com.KrishiG.controllers;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.services.CustomerService;
import com.KrishiG.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/customer")
@CrossOrigin(origins={"*"}, maxAge = 4800)
@Tag(name="CustomerController",description = "APIs for CustomerController!!")
public class CustomerController {

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtUtil jwtUtil;

    //create
    @PostMapping("/addCustomer")
    @Operation(summary = "Create new customer ", description = "API for create new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Success !! | Ok"),
            @ApiResponse(responseCode = "401", description = "Not Authorized !!"),
            @ApiResponse(responseCode = "201", description = "Created !!")

    })
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto,
                                                 @RequestHeader Map<String, String> header) {
        logger.info("POST CALL API for add customer:>>>/customer/addCustomer");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = customerService.createCustomer(customerRequestDto, userId);
        logger.info("Customer Created Successfully!!");
        return responseEntity;
    }

    //update
    @PutMapping("/{customerId}")
    @Operation(description = "API for update customer")
    public ResponseEntity<Object> updateCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto, @PathVariable("customerId") Long customerId,
                                                 @RequestHeader Map<String, String> header) {
        logger.info("UPDATE CALL API for update customer and Address:>>>/customer/{customerId}");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = customerService.updateCustomer(customerId, customerRequestDto, userId);
        logger.info("Customer Updated Successfully!!");
        return responseEntity;
    }

    //delete
    @DeleteMapping("/{customerId}")
    @Operation(description = "API for delete customer")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("customerId") Long customerId) {
        logger.info("DELETE CALL API for delete customer:>>>/customer/{customerId}");
        ResponseEntity<Object> responseEntity = customerService.deleteCustomer(customerId);
        logger.info("Customer Deleted Successfully with the CustomerId {}",customerId);
        return responseEntity;
    }

    //getAll
    @GetMapping("/getAll")
    @Operation(description = "API for get all customers")
    public ResponseEntity<Object> getAllCustomers( @RequestParam(value = "pageNumber",defaultValue = "1",required = false) int pageNumber,
                                                                                  @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                                                  @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                                                                  @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir,
                                                                                  @RequestHeader Map<String, String> header)
    {
        logger.info("GET CALL API for get customer:>>>/customer/getAll");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> allCustomers = customerService.getAllCustomers(pageNumber, pageSize, sortBy, sortDir);
        if(allCustomers != null) {
            logger.info("Sent all customers");
        }else{
            logger.info("fail to fetch customers form the DB");
        }
        return allCustomers;
    }
    @GetMapping("/findByMobile")
    @Operation(description = "API for get customer by mobile number")
    public ResponseEntity<Object> getCustomerByMobile(@PathParam("mobileNumber") String mobileNumber,
                                                      @RequestParam(value = "pageNumber",defaultValue = "1",required = false) int pageNumber,
                                                      @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                                      @RequestParam(value = "sortBy",defaultValue = "mobileNumber",required = false) String sortBy,
                                                      @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir,
                                                      @RequestHeader Map<String, String> header) {
        logger.info("GET CALL API for get customer by mobile number:>>>/customer/findByMobile");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = customerService.getCustomerByMobile(pageNumber, pageSize, sortBy, sortDir,mobileNumber);
        if(responseEntity != null){
            logger.info("sent all customer corresponding to the given number");
        }else{
            logger.info("Customer not available with the given MobileNumber");
        }
        return responseEntity;
    }

    @GetMapping("/findById/{id}")
    @Operation(description = "API for get customer by Id")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long id, @RequestHeader Map<String, String> header) {
        logger.info("GET CALL API for get customer by customer id:>>>/customer/findById/{id}");
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
    @Operation(description = "API for add address of customer")
    public ResponseEntity<Object> addAddressOfCustomer(@Valid @RequestBody CustomerAddressRequestDto addressDto, @RequestHeader Map<String, String> header) {
        logger.info("POST CALL API for add/update Address:>>>/customer/address");
        Long userId = jwtUtil.getUserIdFromToken(header);
        ResponseEntity<Object> responseEntity = customerService.addCustomerAddress(addressDto, userId);
        if(responseEntity != null){
            logger.info("Address saved for the Customer!!");
        }else{
            logger.info("something went wrong, not able to save customer Address");
        }
        return responseEntity;
    }

    @DeleteMapping("/address/{addressId}")
    @Operation(description = "API for delete address of customer")
    public ResponseEntity<Object> deleteAddress(@PathVariable Long addressId, @RequestHeader Map<String, String> header) {
        logger.info("DELETE CALL API for Delete Address:>>>/customer/address/{addressId}");
        Long userId = jwtUtil.getUserIdFromToken(header);
        logger.info("delete Address for Customer");
        ResponseEntity<Object> responseEntity = customerService.deleteAddress(addressId);
        return responseEntity;
    }
}
