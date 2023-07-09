package com.KrishiG.dtos.response;

import com.KrishiG.dtos.request.CustomerAddressDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    private String mobileNumber;

    private String gender;

    private List<CustomerAddressResponseDto> address;

    private Long createdBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;

}