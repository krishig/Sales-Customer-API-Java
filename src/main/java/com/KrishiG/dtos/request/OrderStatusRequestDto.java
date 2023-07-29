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

    private String createBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;
}
