package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "ORDERS")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "sales_user_id")
    private Long salesUserId;

    @Column(name = "product_id")
    private Long product_id;

    private int quantity;

    private boolean status;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_at")
    private Date modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "closed_at")
    private Date closedAt;

}
