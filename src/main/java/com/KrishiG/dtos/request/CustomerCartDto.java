package com.KrishiG.dtos.request;

import com.KrishiG.enitites.CartProducts;
import com.KrishiG.enitites.Customer;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCartDto {

    private Long id;

    private Long customerId;

    private Customer customer;

    private List<CartProducts> cartProducts;

    private float totalPrice;

    private Long createdBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
