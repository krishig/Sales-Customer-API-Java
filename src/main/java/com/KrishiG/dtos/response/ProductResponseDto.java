package com.KrishiG.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private Long id;

    private String productName;

    private Long subCategory;

    private Long brandId;

    private String productDescription;

    private Double price;

    private int discount;

}
