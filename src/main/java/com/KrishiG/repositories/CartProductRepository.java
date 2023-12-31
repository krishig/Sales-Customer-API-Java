package com.KrishiG.repositories;

import com.KrishiG.entities.CartProducts;
import com.KrishiG.entities.CustomerCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProducts,Long> {

    public Optional<CartProducts> findCartProductsByIdAndCart(Long id, CustomerCart cart);

    public List<CartProducts> findByCart(CustomerCart cart);

    public void deleteAllByCart(CustomerCart cart);
}
