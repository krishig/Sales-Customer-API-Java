package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandsRequestDto {

    private Long id;

    private String brandName;

    private int brandImageId;

    private Long createdBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
