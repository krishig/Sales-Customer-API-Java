package com.KrishiG.repositories;

import com.KrishiG.entities.OrderItems;
import com.KrishiG.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    List<OrderItems> findByOrders(Orders orders);
}
