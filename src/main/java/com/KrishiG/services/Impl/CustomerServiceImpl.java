package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.CustomerAddressDto;
import com.KrishiG.dtos.request.CustomerDto;
import com.KrishiG.dtos.response.CustomerAddressResponseDto;
import com.KrishiG.dtos.response.CustomerResponseDto;
import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.CustomerAddress;
import com.KrishiG.exception.ResourceNotFoundException;
import com.KrishiG.repositories.AddressRepository;
import com.KrishiG.repositories.CustomerRepository;
import com.KrishiG.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CustomerResponseDto createCustomer(CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        List<CustomerAddress> lstOfAddresses = new ArrayList<>();
        Customer dbCustomer = customerRepository.save(customer);
        for(CustomerAddressDto temp : customerDto.getAddress())
        {
            CustomerAddress customerAddress = mapper.map(temp, CustomerAddress.class);
            customerAddress.setCustomer(dbCustomer);
            CustomerAddress address = addressRepository.save(customerAddress);
            lstOfAddresses.add(address);
        }
        customer.setAddress(lstOfAddresses);
        CustomerResponseDto customerDto1 = convertEntityToDto(customer);
        return customerDto1;
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found with the given ID"));
        customer.setFullName(customerDto.getFullName());
        customer.setMobileNo(customerDto.getMobileNo());
        customer.setGender(customerDto.getGender());
        List<CustomerAddress> lstOfAddresses = new ArrayList<>();
        for(CustomerAddressDto temp : customerDto.getAddress())
        {
            CustomerAddress customerAddress = mapper.map(temp, CustomerAddress.class);
            lstOfAddresses.add(customerAddress);
        }
        customer.setAddress(lstOfAddresses);
        customer.setCreatedBy(customerDto.getCreatedBy());
        customer.setCreatedDate(customer.getCreatedDate());
        customer.setModifiedBy(customer.getModifiedBy());
        customer.setModifiedDate(customer.getModifiedDate());

        customerRepository.save(customer);

        return mapper.map(customer,CustomerDto.class);

    }

    @Override
    public List<CustomerDto> getAllCustomers() {

        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDtos = customers.stream().map(customer -> mapper.map(customer,CustomerDto.class)).collect(Collectors.toList());
        return customerDtos;
    }

    @Override
    public void deleteCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found with the given ID"));
        customerRepository.delete(customer);
    }

    @Override
    public CustomerAddressResponseDto addCustomerAddress(CustomerAddressDto addressDto) {
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
        customerResDto.setMobileNo(customer.getMobileNo());
        customerResDto.setCreatedBy(customer.getCreatedBy());
        customerResDto.setCreatedAt(customer.getCreatedDate());
        customerResDto.setModifiedBy(customer.getModifiedBy());
        customerResDto.setModifiedAt(customer.getModifiedDate());
        return  customerResDto;
    }

    public CustomerAddressResponseDto convertEntityToDtoForAddress(Long customerId, CustomerAddress customerAddress) {
        CustomerAddressResponseDto customerAddressDto = new CustomerAddressResponseDto();
        customerAddressDto.setAddress(customerAddress.getAddress());
        customerAddressDto.setHouseNumber(customerAddress.getHouseNumber());
        customerAddressDto.setId(customerAddress.getId());
        customerAddressDto.setStreetName(customerAddress.getStreetName());
        customerAddressDto.setDistrict(customerAddress.getDistrict());
        customerAddressDto.setVillageName(customerAddress.getVillageName());
        customerAddressDto.setState(customerAddress.getState());
        customerAddressDto.setCustId(customerId);
        return customerAddressDto;
    }
}
