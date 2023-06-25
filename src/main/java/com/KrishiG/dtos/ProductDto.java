package com.KrishiG.dtos;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;

    @Column(name = "product_name")
    private String productName;

    private double actualPrice;

    private int discount;

    private int category;

    private String brand_name;

    private String productDescription;

    private String createdBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;

}
