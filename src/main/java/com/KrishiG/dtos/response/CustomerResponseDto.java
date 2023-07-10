package com.KrishiG.dtos.response;

import com.KrishiG.dtos.request.CustomerAddressDto;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponseDto {

    private Long id;

    private String fullName;

    private String mobileNo;

    private String gender;

    private List<CustomerAddressResponseDto> address;

    private Long createdBy;

    private Date createdAt;

    private Long modifiedBy;

    private Date modifiedAt;

}