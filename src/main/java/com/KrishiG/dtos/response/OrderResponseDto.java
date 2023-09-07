package com.KrishiG.dtos.response;

import com.KrishiG.entities.Customer;
import com.KrishiG.entities.OrderStatus;
import com.KrishiG.entities.PaymentMethod;
import com.KrishiG.util.Status;
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

    private Status status;

    private Long paymentMethod;

    private String contactNumber;

    private Long addressId;

    private Long createdBy;

    private Date createdDate;

    private Date modifiedDate;

    private Long modifiedBy;

    private Date closedDate;
}
