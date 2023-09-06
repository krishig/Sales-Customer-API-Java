package com.KrishiG.repositories;

import com.KrishiG.entities.Image;
import com.KrishiG.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProduct(Product product);
}
