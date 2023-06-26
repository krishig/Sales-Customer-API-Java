package com.KrishiG.repositories;

import com.KrishiG.enitites.CustomerCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CustomerCart, Long> {
}
