package com.KrishiG.repositories;

import com.KrishiG.enitites.CartProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<CartProducts,Long> {
}
