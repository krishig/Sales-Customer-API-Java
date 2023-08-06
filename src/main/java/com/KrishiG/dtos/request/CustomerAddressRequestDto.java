package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAddressRequestDto {

    private Long id;

    private CustomerRequestDto customer;

    private int houseNumber;

    private String streetName;

    private String villageName;

    private String district;

    private String state;

    private int postalCode;

    private Long createdBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
