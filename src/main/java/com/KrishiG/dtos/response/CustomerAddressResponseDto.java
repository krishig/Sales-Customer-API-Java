package com.KrishiG.dtos.response;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAddressResponseDto {

    private Long id;

    private Long customer;

    private int houseNumber;

    private String streetName;

    private String villageName;

    private String district;

    private String state;

    private int postalCode;

    private Long createdBy;

    private Date createdAt;

    private Long modifiedBy;

    private Date modifiedAt;

    private CustomerCartResponseDto customerCartResponseDto;
}
