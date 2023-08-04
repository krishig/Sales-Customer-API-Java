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
import java.util.Optional;

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
            for(CustomerAddress address : dbCustomer.getAddress()) {
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
        return customerResponseDto1;
    }

    private CustomerCart addInCustomerCart(Customer customer) {
        CustomerCart customerCart = new CustomerCart();
        customerCart.setCustomer(customer);
        customerCart.setCreatedBy(customer.getCreatedBy());
        return customerCart;
    }

    @Override
    public CustomerResponseDto updateCustomer(Long customerId, CustomerRequestDto customerRequestDto) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found with the given ID"));
        customer.setFullName(customerRequestDto.getFullName());
        customer.setMobileNumber(customerRequestDto.getMobileNumber());
        customer.setGender(customerRequestDto.getGender());
        customer.setModifiedBy(customer.getModifiedBy());
        customer.setModifiedDate(customer.getModifiedDate());

        Customer customerDB = customerRepository.save(customer);

        CustomerResponseDto customerResponseDto = convertEntityToDto(customerDB);

        return customerResponseDto;

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
    public CustomerAddressResponseDto addCustomerAddress(CustomerAddressRequestDto addressDto) {
        CustomerAddress customerAddress = mapper.map(addressDto, CustomerAddress.class);
        CustomerAddress customerAddresses = addressRepository.save(customerAddress);
        CustomerAddressResponseDto addressDto1 = convertEntityToDtoForAddress(customerAddresses);
        return addressDto1;
    }

    @Override
    public List<CustomerResponseDto> getCustomerByMobile(String mobileNumber) {
        List<Customer> lstCustomer = customerRepository.findByMobileNumberLike("%"+mobileNumber+"%");
        List<CustomerResponseDto> lstCustomerResponse = new ArrayList<>();
        if(!lstCustomer.isEmpty()) {
            for (Customer customer1 : lstCustomer) {
                CustomerResponseDto customerResponseDto = convertEntityToDto(customer1);
                lstCustomerResponse.add(customerResponseDto);
            }
        }
        return lstCustomerResponse;
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()) {
            customerResponseDto = convertEntityToDto(customer.get());
        }
        return customerResponseDto;
    }

    private CustomerResponseDto convertEntityToDto(Customer customer) {
        CustomerResponseDto customerResDto = new CustomerResponseDto();
        customerResDto.setId(customer.getId());
        customerResDto.setFullName(customer.getFullName());
        customerResDto.setGender(customer.getGender());
        List<CustomerAddressResponseDto> addressDtos = new ArrayList<>();
        for(CustomerAddress custAddress : customer.getAddress()) {
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
        customerCartResponseDto.setCreatedAt(customer.getCustomerCart().getCreatedDate());
        customerCartResponseDto.setModifiedBy(customer.getCustomerCart().getModifiedBy());
        customerCartResponseDto.setModifiedAt(customer.getCustomerCart().getModifiedDate());
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
        customerAddressResponseDto.setCreatedAt(customerAddress.getCreatedAt());
        customerAddressResponseDto.setModifiedBy(customerAddress.getModifiedBy());
        customerAddressResponseDto.setModifiedAt(customerAddress.getModifiedAt());
        return customerAddressResponseDto;
    }
}
