package com.KrishiG.repositories;

import com.KrishiG.enitites.CartProducts;
import com.KrishiG.enitites.CustomerCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProducts,Long> {

    @Query(value = "select * from CART_PRODUCTS WHERE id=?1", nativeQuery = true)
    public List<CartProducts> getCartProductByCart(Long cartId);

    public List<CartProducts> findByCart(CustomerCart cart);

    public void deleteAllByCart(CustomerCart cart);
}
