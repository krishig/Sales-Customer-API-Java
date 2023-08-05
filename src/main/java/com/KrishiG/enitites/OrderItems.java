package com.KrishiG.enitites;

import jakarta.persistence.*;

import lombok.*;

import java.util.Date;
import java.util.function.DoubleToIntFunction;

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

    @Column(name="totalProductDiscountPrice")
    private Double totalDiscountPrice;

    private int quantity;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_at")
    private Date modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

}
