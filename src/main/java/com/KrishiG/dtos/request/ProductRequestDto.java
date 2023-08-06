package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDto {

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

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
