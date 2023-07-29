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

    private String createdBy;

    private Date createdAt;

    private Date modifiedAt;

    private String modifiedBy;

    private Date closedAt;
}
