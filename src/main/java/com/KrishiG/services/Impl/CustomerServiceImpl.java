package com.KrishiG.services.Impl;

import com.KrishiG.dtos.CustomerAddressDto;
import com.KrishiG.dtos.CustomerDto;
import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.CustomerAddress;
import com.KrishiG.exception.ResourceNotFoundException;
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
    private ModelMapper mapper;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        List<CustomerAddress> lstOfAddresses = new ArrayList<>();
        for(CustomerAddressDto temp : customerDto.getAddress())
        {
            CustomerAddress customerAddress = mapper.map(temp, CustomerAddress.class);
            lstOfAddresses.add(customerAddress);
        }
        customer.setAddressId(lstOfAddresses);
        customerRepository.save(customer);
        return mapper.map(customer, CustomerDto.class);
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
        customer.setAddressId(lstOfAddresses);
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
}
