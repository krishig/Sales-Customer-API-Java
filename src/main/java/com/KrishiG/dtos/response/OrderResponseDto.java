package com.KrishiG.dtos.response;

import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.OrderStatus;
import com.KrishiG.enitites.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;

    private String orderId;

    private Customer customerId;

    private float totalPrice;

    private OrderStatus status;

    private PaymentMethod paymentMethod;

    private String contactNumber;

    private Long addressId;

    private String createdBy;

    private Date createdAt;

    private Date modifiedAt;

    private String modifiedBy;

    private Date closedAt;
}
