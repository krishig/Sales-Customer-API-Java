package com.KrishiG.dtos;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductsDto {

    private Long id;

    private Long salesUserId;

    private Long cartId;

    private Long productId;

    private int productQuantity;

    private float price;

    private  String createdBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;
}
