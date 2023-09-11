package com.KrishiG.repositories;

import com.KrishiG.entities.Customer;
import com.KrishiG.entities.Orders;
import com.KrishiG.util.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCustomerId(Customer customer);

    List<Orders> findByCreatedBy(Long userId);

    /*@Query("select count(*) from Orders o where o.out_of_delivered_at = ?1 and o.status i= ?2")
    public int getCountForOutOfDeliveryByDateAndStatus(LocalDateTime date, Status status);

    @Query("select count(*) from Orders o where o.closed_at = ?1 and o.status i= ?2")
    public int getCountForDeliveredByDateAndStatus(LocalDateTime date, Status status);

    @Query("select count(*) from Orders o where o.created_at = ?1 and o.status i= ?2")
    public int getCountForOrderByDateAndStatus(LocalDateTime date, Status status);

    @Query("select count(*) from Orders o where o.out_of_delivered_at<=:date")
    public int getCountForPendingDeliveredByDate(@Param("date") LocalDateTime localDateTime);

    public Page<Orders> findByOutOfDeliveryDateAndStatus(LocalDateTime localDateTime, Status status);

    public Page<Orders> findByClosedDateAndStatus(LocalDateTime localDateTime, Status status);

    public Page<Orders> findByCreatedDateAndStatus(LocalDateTime localDateTime, Status status);

    @Query("select * from Orders o where o.out_of_delivered_at<=:date")
    public Page<Orders> findByPendingDeliveredByDate(@Param("date") LocalDateTime localDateTime);*/
}
