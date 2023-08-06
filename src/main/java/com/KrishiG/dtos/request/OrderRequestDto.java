package com.KrishiG.dtos.request;

import com.KrishiG.enitites.Customer;
import com.KrishiG.enitites.OrderStatus;
import com.KrishiG.enitites.PaymentMethod;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {

    private Long id;

    private String orderId;

    private Customer customerId;

    private float totalPrice;

    private OrderStatus status;

    private PaymentMethod paymentMethod;

    private String contactNumber;

    private Long addressId;

    private Long createdBy;

    private Date createdDate;

    private Date modifiedDate;

    private Long modifiedBy;

    private Date closedDate;
}
