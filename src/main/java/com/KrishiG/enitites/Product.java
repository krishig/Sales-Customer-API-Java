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

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private double actualPrice;

    @Column(name = "discounted_price")
    private int discountPrice;

    private int category;

    @Column(name = "whole_sale_price")
    private int wholeSalePrice;

    private int quantity;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_at")
    private Date modifiedAt;

}
