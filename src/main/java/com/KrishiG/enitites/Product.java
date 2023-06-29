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
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private SubCategory sub_category;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private double actualPrice;

    @Column(name = "whole_sale_price")
    private int wholeSalePrice;

    private int quantity;

    @Column(name = "discount")
    private int discountPrice;

    @ManyToOne
    @Column(name = "brand_id")
    private Brands brandId;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_at")
    private Date modifiedAt;

}
