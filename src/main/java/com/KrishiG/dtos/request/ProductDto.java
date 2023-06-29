package com.KrishiG.dtos.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    private Long sub_category;

    private String productName;

    private double actualPrice;

    private int wholeSalePrice;

    private int quantity;

    private int discountPrice;

    private int subCategory;

    private Long brandId;

    private String productDescription;

    private Long createdBy;

    private Date createdAt;

    private Long modifiedBy;

    private Date modifiedAt;
}
