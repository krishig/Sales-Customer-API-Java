package com.KrishiG.dtos;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCartDto {

    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "total_price")
    private float totalPrice;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_at")
    private Date modifiedAt;
}
