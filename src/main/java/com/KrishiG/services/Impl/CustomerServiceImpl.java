package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CustomerAddressRequestDto;
import com.KrishiG.dtos.request.CustomerRequestDto;
import com.KrishiG.dtos.response.CustomerAddressResponseDto;
import com.KrishiG.dtos.response.CustomerCartResponseDto;
import com.KrishiG.dtos.response.CustomerResponseDto;
import com.KrishiG.dtos.response.PageableResponse;
import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.CustomerAddress;
import com.KrishiG.enitites.CustomerCart;
import com.KrishiG.exception.ResourceNotFoundException;
import com.KrishiG.helper.Helper;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer = mapper.map(customerRequestDto, Customer.class);
        List<CustomerAddress> lstOfAddresses = new ArrayList<>();
        Customer dbCustomer = customerRepository.save(customer);
        if(dbCustomer!=null){
            for(CustomerAddressRequestDto temp : customerRequestDto.getAddress())
            {
                CustomerAddress customerAddress = mapper.map(temp, CustomerAddress.class);
                customerAddress.setCustomer(dbCustomer);
                CustomerAddress address = addressRepository.save(customerAddress);
                lstOfAddresses.add(address);
            }

            CustomerCart customerCart = new CustomerCart();
            customerCart.setCustomer(dbCustomer);
            CustomerCart customerCartDB = cartRepository.save(customerCart);
            customer.setCustomerCart(customerCartDB);
        }
        customer.setAddress(lstOfAddresses);
        CustomerResponseDto customerResponseDto1 = convertEntityToDto(customer);
        return customerResponseDto1;
    }

    @Override
    public CustomerRequestDto updateCustomer(Long customerId, CustomerRequestDto customerRequestDto) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found with the given ID"));
        customer.setFullName(customerRequestDto.getFullName());
        customer.setMobileNumber(customerRequestDto.getMobileNumber());
        customer.setGender(customerRequestDto.getGender());
        List<CustomerAddress> lstOfAddresses = new ArrayList<>();
        for(CustomerAddressRequestDto temp : customerRequestDto.getAddress())
        {
            CustomerAddress customerAddress = mapper.map(temp, CustomerAddress.class);
            lstOfAddresses.add(customerAddress);
        }
        customer.setAddress(lstOfAddresses);
        customer.setCreatedBy(customerRequestDto.getCreatedBy());
        customer.setCreatedDate(customer.getCreatedDate());
        customer.setModifiedBy(customer.getModifiedBy());
        customer.setModifiedDate(customer.getModifiedDate());

        customerRepository.save(customer);

        return mapper.map(customer, CustomerRequestDto.class);

    }

    @Override
    public PageableResponse<CustomerResponseDto> getAllCustomers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        //pageNumber default starts from 0
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Customer> page = customerRepository.findAll(pageable);
        PageableResponse<CustomerResponseDto> response = Helper.getPageableResponse(page,CustomerResponseDto.class);
        return response;

    }

    @Override
    public void deleteCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found with the given ID"));
        customerRepository.delete(customer);
    }

    @Override
    public CustomerAddressResponseDto addCustomerAddress(CustomerAddressRequestDto addressDto, Long addressId) {
        if(addressId!=null) {
            addressDto.setId(addressId);
        }
        CustomerAddress customerAddress = mapper.map(addressDto, CustomerAddress.class);
        CustomerAddress customerAddresses = addressRepository.save(customerAddress);
        CustomerAddressResponseDto addressDto1 = convertEntityToDtoForAddress(addressDto.getCustomer().getId(),customerAddresses);
        return addressDto1;
    }

    private CustomerResponseDto convertEntityToDto(Customer customer) {
        CustomerResponseDto customerResDto = new CustomerResponseDto();
        customerResDto.setId(customer.getId());
        customerResDto.setFullName(customer.getFullName());
        customerResDto.setGender(customer.getGender());
        List<CustomerAddressResponseDto> addressDtos = new ArrayList<>();
        for(CustomerAddress custAddress : customer.getAddress()) {
            CustomerAddressResponseDto addressDto = mapper.map(custAddress, CustomerAddressResponseDto.class);
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
        customerResDto.setCustomerCartResponseDto(customerCartResponseDto);
        return customerResDto;
    }

    public CustomerAddressResponseDto convertEntityToDtoForAddress(Long customerId, CustomerAddress customerAddress) {
        CustomerAddressResponseDto customerAddressResponseDto = new CustomerAddressResponseDto();
        customerAddressResponseDto.setHouseNumber(customerAddress.getHouseNumber());
        customerAddressResponseDto.setId(customerAddress.getId());
        customerAddressResponseDto.setStreetName(customerAddress.getStreetName());
        customerAddressResponseDto.setDistrict(customerAddress.getDistrict());
        customerAddressResponseDto.setVillageName(customerAddress.getVillageName());
        customerAddressResponseDto.setState(customerAddress.getState());
        customerAddressResponseDto.setCustId(customerId);
        return customerAddressResponseDto;
    }
}
