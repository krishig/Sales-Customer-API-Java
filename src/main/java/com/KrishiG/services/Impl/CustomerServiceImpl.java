package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.dtos.response.*;
import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.CustomerAddress;
import com.KrishiG.enitites.CustomerCart;
import com.KrishiG.exception.ResourceNotFoundException;
import com.KrishiG.repositories.AddressRepository;
import com.KrishiG.repositories.CartRepository;
import com.KrishiG.repositories.CustomerRepository;
import com.KrishiG.services.CustomerService;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ResponseEntity<Object> createCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer = mapper.map(customerRequestDto, Customer.class);
        List<CustomerAddress> lstOfAddresses = new ArrayList<>();
        Customer dbCustomer = customerRepository.save(customer);
        if (dbCustomer != null) {
            for (CustomerAddress address : dbCustomer.getAddress()) {
                address.setCustomer(dbCustomer);
                CustomerAddress savedAddress = addressRepository.save(address);
                lstOfAddresses.add(address);
            }
            CustomerCart customerCart = addInCustomerCart(dbCustomer);
            CustomerCart customerCartDB = cartRepository.save(customerCart);
            customer.setCustomerCart(customerCartDB);
        }
        customer.setAddress(lstOfAddresses);
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
    public ResponseEntity<Object> updateCustomer(Long customerId, CustomerRequestDto customerRequestDto) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID"));
        customer.setFullName(customerRequestDto.getFullName());
        customer.setMobileNumber(customerRequestDto.getMobileNumber());
        customer.setGender(customerRequestDto.getGender());
        customer.setModifiedBy(customerRequestDto.getModifiedBy());
        customer.setModifiedDate(customerRequestDto.getModifiedDate());
        Customer customerDB = customerRepository.save(customer);
        CustomerResponseDto customerResponseDto = convertEntityToDto(customerDB);
        String updateMessage = "Customer Updated Successfully";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(updateMessage, HttpStatus.CREATED, customerResponseDto, false, true);
        return responseEntity;

    }

    @Override
    public ResponseEntity<Object> getAllCustomers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        //pageNumber default starts from 0
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Customer> page = customerRepository.findAll(pageable);
        if(page.isEmpty())
        {
            throw new ResourceNotFoundException("No Customer is available");
        }
        List<Customer> customers = page.getContent();

        List<CustomerResponseDto> dtoList = customers.stream().map(customer -> convertEntityToDto(customer)).collect(Collectors.toList());

        PageableResponse<CustomerResponseDto> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(null, HttpStatus.CREATED, response, false, true);
        return responseEntity;

    }

    @Override
    public ResponseEntity<Object> deleteCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with the given ID"));
        String deleteMessage = "Customer Deleted Successfully !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(deleteMessage, HttpStatus.OK, null, false, true);
        customerRepository.delete(customer);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> addCustomerAddress(CustomerAddressRequestDto addressDto) {
        CustomerAddress customerAddress = mapper.map(addressDto, CustomerAddress.class);
        CustomerAddress customerAddresses = addressRepository.save(customerAddress);
        CustomerAddressResponseDto addressDto1 = convertEntityToDtoForAddress(customerAddresses);
        String message = "Customer Address Added !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, addressDto1, false, true);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getCustomerByMobile(String mobileNumber) {
        List<Customer> lstCustomer = customerRepository.findByMobileNumberLike("%" + mobileNumber + "%");
        List<CustomerResponseDto> lstCustomerResponseDtos = new ArrayList<>();
        if (!lstCustomer.isEmpty()) {
            for (Customer customer1 : lstCustomer) {
                CustomerResponseDto customerResponseDto = convertEntityToDto(customer1);
                lstCustomerResponseDtos.add(customerResponseDto);
            }
        }
        String message = "Customer Details By Mobile Number !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, lstCustomerResponseDtos, false, true);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Object> getCustomerById(Long id) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            customerResponseDto = convertEntityToDto(customer.get());
        }
        String message = "Customer By Id !!";
        ResponseEntity<Object> responseEntity = ApiResponse.generateResponse(message, HttpStatus.OK, customerResponseDto, false, true);
        return responseEntity;
    }

    private CustomerResponseDto convertEntityToDto(Customer customer) {
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
        CustomerCartResponseDto customerCartResponseDto = new CustomerCartResponseDto();
        customerCartResponseDto.setId(customer.getCustomerCart().getId());
        customerCartResponseDto.setCustomerId(customer.getCustomerCart().getCustomer().getId());
        customerCartResponseDto.setCreatedBy(customer.getCustomerCart().getCreatedBy());
        customerCartResponseDto.setCreatedDate(customer.getCustomerCart().getCreatedDate());
        customerCartResponseDto.setModifiedBy(customer.getCustomerCart().getModifiedBy());
        customerCartResponseDto.setModifiedDate(customer.getCustomerCart().getModifiedDate());
        customerResDto.setCustomerCartResponseDto(customerCartResponseDto);
        return customerResDto;
    }

    public CustomerAddressResponseDto convertEntityToDtoForAddress(CustomerAddress customerAddress) {
        CustomerAddressResponseDto customerAddressResponseDto = new CustomerAddressResponseDto();
        customerAddressResponseDto.setHouseNumber(customerAddress.getHouseNumber());
        customerAddressResponseDto.setId(customerAddress.getId());
        customerAddressResponseDto.setStreetName(customerAddress.getStreetName());
        customerAddressResponseDto.setDistrict(customerAddress.getDistrict());
        customerAddressResponseDto.setVillageName(customerAddress.getVillageName());
        customerAddressResponseDto.setState(customerAddress.getState());
        customerAddressResponseDto.setCustomer(customerAddress.getCustomer().getId());
        customerAddressResponseDto.setCreatedBy(customerAddress.getCreatedBy());
        customerAddressResponseDto.setCreatedDate(customerAddress.getCreatedDate());
        customerAddressResponseDto.setModifiedBy(customerAddress.getModifiedBy());
        customerAddressResponseDto.setModifiedDate(customerAddress.getModifiedDate());
        return customerAddressResponseDto;
    }
}
