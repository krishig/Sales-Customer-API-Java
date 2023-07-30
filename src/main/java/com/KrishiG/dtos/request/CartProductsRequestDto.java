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

    private Long cartId;

    private ProductRequestDto product;

    private int productQuantity;

    private Double price;

    private Double totalPrice;

    private  Long createdBy;

    private Date createdDate;

    private Long modifiedBY;

    private Date modifiedDate;
}
