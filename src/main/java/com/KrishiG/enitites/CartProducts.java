package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="CART_PRODUCTS")
@Builder
public class CartProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CustomerCart cart;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_quantity")
    private int productQuantity;

    @Column(name = "actual_price")
    private float actualPrice;

    @Column(name = "discount")
    private int discount;

    @Column(name = "purchase_price")
    private float purchasePrice;

    @Column(name = "total_amount")
    private float totalAmount;

    @Column(name = "created_by")
    private  Long createdBy;

    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "modified_by")
    private Long modifiedBY;

    @Column(name = "modified_at")
    private Date modifiedDate;
}
