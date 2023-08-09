package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductsRequestDto {

    private Long id;

    private Long cartId;

    private ProductRequestDto product;

    private int productQuantity;

    private Long createdBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
