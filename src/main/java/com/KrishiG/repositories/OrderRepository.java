package com.KrishiG.repositories;

import com.KrishiG.entities.Customer;
import com.KrishiG.entities.Orders;
import com.KrishiG.util.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCustomerId(Customer customer);

    Page<Orders> findByCreatedBy(Long userId, Pageable pageable);

    Page<Orders> findByOrderIdLike(String orderId, Pageable pageable);

    @Query(value = "select count(*) from krishig_db.orders o where DATE(o.out_of_delivered_at) = ?1 and o.status = ?2", nativeQuery = true)
    public int getCountForOutOfDeliveryByDateAndStatus(Date date, String status);

    @Query(value = "select count(*) from krishig_db.orders o where DATE(o.closed_at) = ?1 and o.status = ?2", nativeQuery = true)
    public int getCountForDeliveredByDateAndStatus(Date date, String status);

    @Query(value = "select count(*) from krishig_db.orders o where DATE(o.created_at) = ?1 and o.status = ?2", nativeQuery = true)
    public int getCountForOrderByDateAndStatus(Date date, String status);

    @Query(value = "select count(*) from krishig_db.orders o where DATE(o.out_of_delivered_at)<=:date", nativeQuery = true)
    public int getCountForPendingDeliveredByDate(@Param("date") Date localDateTime);

    public Page<Orders> findByOutOfDeliveryDateAndStatus(Date localDateTime, Status status, Pageable pageable);

    public Page<Orders> findByClosedDateAndStatus(Date localDateTime, Status status, Pageable pageable);

    public Page<Orders> findByCreatedDateAndStatus(Date localDateTime, Status status, Pageable pageable);

    @Query(value = "select * from krishig_db.orders o where DATE(o.out_of_delivered_at)<=:date", nativeQuery = true)
    public Page<Orders> findByPendingDeliveredByDate(@Param("date") Date localDateTime, Pageable pageable);
}
