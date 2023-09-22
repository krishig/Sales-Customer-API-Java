package com.KrishiG.repositories;

import com.KrishiG.entities.Customer;
import com.KrishiG.entities.Orders;
import com.KrishiG.util.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    List<Orders> findByCustomerId(Customer customer);

    Page<Orders> findByCreatedBy(Long userId, Pageable pageable);

    Page<Orders> findByOrderIdLike(String orderId, Pageable pageable);

    @Query(value = "select count(*) from krishig_db.orders o where o.out_of_delivered_at LIKE %?1% AND o.status=?2", nativeQuery = true)
    public int getCountForOutOfDeliveryByDateAndStatus(String date, String status);

    @Query(value = "select count(*) from krishig_db.orders o where o.closed_at LIKE %?1% AND o.status=?2", nativeQuery = true)
    public int getCountForDeliveredByDateAndStatus(String date, String status);

    @Query(value = "select count(*) from krishig_db.orders o where o.created_at LIKE %?1%", nativeQuery = true)
    public int getCountForOrderByDateAndStatus(String date);

    @Query(value = "select count(*) from krishig_db.orders o where DATE(o.out_of_delivered_at)< ?1", nativeQuery = true)
    public int getCountForPendingDeliveredByDate(String date);

    @Query(value = "select * from krishig_db.orders o where o.out_of_delivered_at LIKE %?1% AND o.status=?2", nativeQuery = true)
    public Page<Orders> findByOutOfDeliveryDateAndStatus(String date, String status, Pageable pageable);

    @Query(value = "select * from krishig_db.orders o where o.closed_at LIKE %?1% AND o.status=?2", nativeQuery = true)
    public Page<Orders> findByClosedDateAndStatus(String date, String status, Pageable pageable);

    @Query(value = "select * from krishig_db.orders o where o.created_at LIKE %?1%", nativeQuery = true)
    public Page<Orders> findByCreatedDateAndStatus(String date, Pageable pageable);

    @Query(value = "select * from krishig_db.orders o where DATE(o.out_of_delivered_at)< ?1", nativeQuery = true)
    public Page<Orders> findByPendingDeliveredByDate(String date, Pageable pageable);

    @Query(value = "with order_date as (SELECT SUM(o.TOTAL_PRICE) as sum_amount from krishig_db.orders o group by o.closed_at, o.status having o.closed_at like %?1% and o.status=?2)\n" +
            "select sum(sum_amount) from order_date", nativeQuery = true)
    public Double getTotalPrice(String Date, String status);

    @Query(value = "select * from krishig_db.orders o " +
            "where (:createdDate is null or o.created_at like '%:createdDate%') AND (:status is null or o.status = status)", nativeQuery = true)
    public Page<Orders> getOrderDetailsByKeyword(@Param("createdDate") String createdDate, @Param("status") String status, Pageable pageable);

}
