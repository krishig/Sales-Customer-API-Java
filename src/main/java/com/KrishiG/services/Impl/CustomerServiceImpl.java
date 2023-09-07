package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.dtos.response.*;
import com.KrishiG.entities.Customer;
import com.KrishiG.entities.CustomerAddress;
import com.KrishiG.entities.CustomerCart;
import com.KrishiG.exception.ResourceNotFoundException;
import com.KrishiG.repositories.AddressRepository;
import com.KrishiG.repositories.CartRepository;
import com.KrishiG.repositories.CustomerRepository;
import com.KrishiG.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ResponseEntity<Object> createCustomer(CustomerRequestDto customerRequestDto, Long userId) {

        logger.info("Inside CreateCustomer ServiceImpl");

        Customer customer = mapper.map(customerRequestDto, Customer.class);
        customer.setCreatedBy(userId);
        List<CustomerAddress> lstOfAddresses = new ArrayList<>();
        Customer dbCustomer = customerRepository.save(customer);
        if (dbCustomer != null) {
            logger.info("Setting the address for the Customers");
            for (CustomerAddress address : dbCustomer.getAddress()) {
                address.setCustomer(dbCustomer);
                address.setCreatedBy(userId);
                CustomerAddress savedAddress = addressRepository.save(address);
                lstOfAddresses.add(address);
                logger.info("Set address successfully!! ");
            }
            CustomerCart customerCart = addInCustomerCart(dbCustomer);
            logger.info("Created CustomerCart !!");
            customerCart.setCreatedBy(userId);
            CustomerCart customerCartDB = cartRepository.save(customerCart);
            if (customerCartDB == null) {
                logger.info("Cart is not available for the customer ");
                throw new ResourceNotFoundException();
            }
            customer.setCustomerCart(customerCartDB);
            logger.info("Cart created Successfully for the Customer!!");
        }
        customer.setAddress(lstOfAddresses);
        logger.info("Address set for the customer");
        CustomerResponseDto customerResponseDto1 = convertEntityToDto(customer);
        String createMessage = "Customer is created Successfully";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(createMessage, HttpStatus.CREATED, customerResponseDto1, false, true);
        return responseEntity;
    }

    private CustomerCart addInCustomerCart(Customer customer) {
        CustomerCart customerCart = new CustomerCart();
        customerCart.setCustomer(customer);
        customerCart.setCreatedBy(customer.getCreatedBy());
        return customerCart;
    }

    @Override
    public ResponseEntity<Object> updateCustomer(Long customerId, CustomerRequestDto customerRequestDto, Long userId) {

        logger.info("Inside updateCustomer ServiceImpl");

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID"));
        customer.setFullName(customerRequestDto.getFullName());
        customer.setMobileNumber(customerRequestDto.getMobileNumber());
        customer.setGender(customerRequestDto.getGender());
        customer.setModifiedBy(userId);
        customer.setModifiedDate(customerRequestDto.getModifiedDate());
        Customer customerDB = customerRepository.save(customer);
        if (customerDB == null) {
            logger.info("Customer Not Found with the given ID {} ", customerId);
            throw new ResourceNotFoundException("Unable to Update Customer with the given Id " + customerId);
        }
        CustomerResponseDto customerResponseDto = convertEntityToDto(customerDB);
        String updateMessage = "Customer Updated Successfully";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(updateMessage, HttpStatus.CREATED, customerResponseDto, false, true);
        logger.info("Customer Updated Successfully!!");
        return responseEntity;

    }

    @Override
    public ResponseEntity<Object> getAllCustomers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        logger.info("Inside getAllCustomers ServiceImpl !!");

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        //pageNumber starts from 1
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Customer> page = customerRepository.findAll(pageable);
        if (page.isEmpty()) {
            logger.info("No Customer is available");
            throw new ResourceNotFoundException("No Customer is available");
        }
        List<Customer> customers = page.getContent();

        List<CustomerResponseDto> dtoList = customers.stream().map(customer -> convertEntityToDto(customer)).collect(Collectors.toList());

        PageableResponse<CustomerResponseDto> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, response, false, true);
        logger.info("Sent all the customer from getAllCustomer ServiceImpl");
        return responseEntity;

    }

    @Override
    public ResponseEntity<Object> deleteCustomer(Long customerId) {

        logger.info("Inside deleteCustomer ServiceImpl");

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID " + customerId));
        String deleteMessage = "Customer Deleted Successfully !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(deleteMessage, HttpStatus.OK, null, false, true);
        customerRepository.delete(customer);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> addCustomerAddress(CustomerAddressRequestDto addressDto, Long userId) {
        CustomerAddress customerAddresses = null;
        CustomerAddress customerAddress = mapper.map(addressDto, CustomerAddress.class);
        if (customerAddress.getId() != null) {
            customerAddress.setModifiedBy(userId);
        } else {
            customerAddress.setCreatedBy(userId);
        }
        customerAddresses = addressRepository.save(customerAddress);
        if (customerAddresses == null) {
            throw new ResourceNotFoundException("Could not able to add address " + addressDto.getCustomer().getAddress());
        }
        CustomerAddressResponseDto addressDto1 = convertEntityToDtoForAddress(customerAddresses);
        String message = "Customer Address Added !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, addressDto1, false, true);
        if(responseEntity != null) {
            logger.info("Sent response successfully for addCustomerAddress");
        }else {
            logger.info("Something went wrong in Service!!");
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getCustomerByMobile(int pageNumber, int pageSize, String sortBy, String sortDir,String mobileNumber) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        Page<Customer> page = customerRepository.findByMobileNumberLike("%" + mobileNumber + "%", pageable);
        if (page.isEmpty()) {
            logger.info("No Customer is available");
            throw new ResourceNotFoundException("No Customer is available");
        }
        List<Customer> customers = page.getContent();

        List<CustomerResponseDto> dtoList = customers.stream().map(customer -> convertEntityToDto(customer)).collect(Collectors.toList());

        PageableResponse<CustomerResponseDto> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.OK, response, false, true);
        logger.info("Sent all the customer from getAllCustomer ServiceImpl");
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getCustomerById(Long id) {
        logger.info("Inside getCustomerById serviceImpl");
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            customerResponseDto = convertEntityToDto(customer.get());
            String message = "Customer By Id !!";
            ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, customerResponseDto, false, true);
            return responseEntity;
        } else {
            logger.info("Customer Not Found with the Given ID");
            throw new ResourceNotFoundException("Customer Not Found with the Given ID " + id);
        }
    }

    @Override
    public ResponseEntity<Object> deleteAddress(Long id) {
        addressRepository.deleteById(id);
        String deleteMessage = "Customer Deleted Successfully !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(deleteMessage, HttpStatus.OK, null, false, true);
        return responseEntity;
    }

    private CustomerResponseDto convertEntityToDto(Customer customer) {
        logger.info("Inside convertEntityToDto");
        CustomerResponseDto customerResDto = new CustomerResponseDto();
        customerResDto.setId(customer.getId());
        customerResDto.setFullName(customer.getFullName());
        customerResDto.setGender(customer.getGender());
        List<CustomerAddressResponseDto> addressDtos = new ArrayList<>();
        for (CustomerAddress custAddress : customer.getAddress()) {
            CustomerAddressResponseDto addressDto = convertEntityToDtoForAddress(custAddress);
            addressDtos.add(addressDto);
        }

        customerResDto.setAddress(addressDtos);
        customerResDto.setMobileNumber(customer.getMobileNumber());
        customerResDto.setCreatedBy(customer.getCreatedBy());
        customerResDto.setCreatedDate(customer.getCreatedDate());
        customerResDto.setModifiedBy(customer.getModifiedBy());
        customerResDto.setModifiedDate(customer.getModifiedDate());
        if (customer.getCustomerCart() != null) {
            CustomerCartResponseDto customerCartResponseDto = new CustomerCartResponseDto();
            customerCartResponseDto.setId(customer.getCustomerCart().getId());
            customerCartResponseDto.setCustomerId(customer.getCustomerCart().getCustomer().getId());
            customerCartResponseDto.setCreatedBy(customer.getCustomerCart().getCreatedBy());
            customerCartResponseDto.setCreatedDate(customer.getCustomerCart().getCreatedDate());
            customerCartResponseDto.setModifiedBy(customer.getCustomerCart().getModifiedBy());
            customerCartResponseDto.setModifiedDate(customer.getCustomerCart().getModifiedDate());
            customerResDto.setCustomerCartResponseDto(customerCartResponseDto);
        }
        logger.info("Exiting from convertEntityToDto");
        return customerResDto;
    }

    public CustomerAddressResponseDto convertEntityToDtoForAddress(CustomerAddress customerAddress) {
        logger.info("Inside convertEntityToDtoForAddress");
        CustomerAddressResponseDto customerAddressResponseDto = new CustomerAddressResponseDto();
        customerAddressResponseDto.setHouseNumber(customerAddress.getHouseNumber());
        customerAddressResponseDto.setId(customerAddress.getId());
        customerAddressResponseDto.setStreetName(customerAddress.getStreetName());
        customerAddressResponseDto.setDistrict(customerAddress.getDistrict());
        customerAddressResponseDto.setVillageName(customerAddress.getVillageName());
        customerAddressResponseDto.setState(customerAddress.getState());
        customerAddressResponseDto.setPostalCode(customerAddress.getPostalCode());
        customerAddressResponseDto.setCustomer(customerAddress.getCustomer().getId());
        customerAddressResponseDto.setCreatedBy(customerAddress.getCreatedBy());
        customerAddressResponseDto.setCreatedDate(customerAddress.getCreatedDate());
        customerAddressResponseDto.setModifiedBy(customerAddress.getModifiedBy());
        customerAddressResponseDto.setModifiedDate(customerAddress.getModifiedDate());
        logger.info("Exiting from convertEntityToDtoForAddress");
        return customerAddressResponseDto;
    }
}
