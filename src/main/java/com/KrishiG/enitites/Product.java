package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "product")
    private List<Image> images;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private double actualPrice;

    @Column(name = "whole_sale_price")
    private double wholeSalePrice;

    private Long quantity;

    @Column(name = "discount")
    private int discount;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brands brand;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private Date modifiedDate;

}
