package com.KrishiG.dtos.request;

import com.KrishiG.enitites.Customer;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductsRequestDto {


    private Long id;

    private Long customerId;

    private Customer customer;

    private Long cartId;

    private Long productId;

    private int productQuantity;

    private float actualPrice;

    private float totalAmount;

    private float purchasePrice;

    private  Long createdBy;

    private Date createdDate;

    private Long modifiedBY;

    private Date modifiedDate;
}
