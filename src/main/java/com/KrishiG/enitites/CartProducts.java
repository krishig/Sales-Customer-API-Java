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

    @Column(name = "cart_id")
    @ManyToOne
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

    @Column(name = "created_by")
    private  String createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_by")
    private String modifiedBY;

    @Column(name = "modified_at")
    private Date modifiedAt;
}
