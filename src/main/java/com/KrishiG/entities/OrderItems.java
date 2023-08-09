package com.KrishiG.entities;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price_after_discount")
    private Double priceAfterDiscount;

    @Column(name = "totalProductDiscountPrice")
    private Double totalDiscountPrice;

    private int quantity;

    @CreationTimestamp
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "modified_at")
    private Date modifiedAt;

    @UpdateTimestamp
    @Column(name = "modified_by")
    private Long modifiedDate;

}
