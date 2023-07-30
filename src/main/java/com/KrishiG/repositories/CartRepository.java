package com.KrishiG.repositories;

import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.CustomerCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CustomerCart, Long> {
    public Optional<CustomerCart> findByCustomer(Customer customer);
}
