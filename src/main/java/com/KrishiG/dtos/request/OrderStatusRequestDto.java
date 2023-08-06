package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusRequestDto {

    private Long id;

    private String type;

    private Long createBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
