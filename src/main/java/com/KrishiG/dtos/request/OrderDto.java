package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;

    private String orderId;

    private Long customerId;

    private Long salesUserId;

    private Long product_id;

    private int quantity;

    private boolean status;

    private String createdBy;

    private Date createdAt;

    private Date modifiedAt;

    private String modifiedBy;

    private Date closedAt;
}
