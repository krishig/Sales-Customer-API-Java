package com.KrishiG.dtos.request;

import com.KrishiG.enitites.CartProducts;
import com.KrishiG.enitites.Customer;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "CustomerId Required !!")
    private Long customerId;

    @NotBlank
    private Customer customer;

    @NotBlank
    private List<CartProducts> cartProducts;

    @NotBlank
    private float totalPrice;

    @NotBlank
    private Long createdBy;

    @NotBlank
    private Date createdDate;

    @NotBlank
    private Long modifiedBy;
    
    @NotBlank
    private Date modifiedDate;
}
