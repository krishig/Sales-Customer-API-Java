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

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int productQuantity;

    @Column(name = "price")
    private Double actualPrice;

    @Column(name = "purchase_price")
    private Double purchasePrice;

    @Column(name = "discount")
    private float discount;

    @Column(name = "created_by")
    private  Long createdBy;

    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "modified_by")
    private Long modifiedBY;

    @Column(name = "modified_at")
    private Date modifiedDate;
}
