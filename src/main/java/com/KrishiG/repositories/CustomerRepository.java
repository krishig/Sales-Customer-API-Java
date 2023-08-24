package com.KrishiG.repositories;

import com.KrishiG.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Page<Customer> findByMobileNumberLike(String mobileNumber, Pageable pageable);

}
