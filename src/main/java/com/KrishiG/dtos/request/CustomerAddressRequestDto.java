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

    private String createdBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;
}
