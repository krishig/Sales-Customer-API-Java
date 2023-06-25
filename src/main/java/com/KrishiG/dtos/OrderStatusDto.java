package com.KrishiG.dtos;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusDto {

    private Long id;

    private String type;

    private String createBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;
}
