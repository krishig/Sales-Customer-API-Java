package com.KrishiG.dtos.request;

import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.CustomerCart;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartProductsDto {


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
