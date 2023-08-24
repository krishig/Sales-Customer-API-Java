package com.KrishiG.repositories;

import com.KrishiG.entities.Customer;
import com.KrishiG.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCustomerId(Customer customer);

}
