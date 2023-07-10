package com.KrishiG.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductResponseDto {

    private Long id;


    private Long cartId;

    private ProductResponseDto productResponseDto;

    private int productQuantity;

    private float actualPrice;

    private float discount;

    private float purchasePrice;

    private float totalAmount;

    private Long createdBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
