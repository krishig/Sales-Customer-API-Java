package com.KrishiG.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CustomerRequestDto {

    private Long id;

    @NotBlank(message = "Enter Name")
    @Size(min = 3,message = "Invalid Name!!")
    private String fullName;

    @NotBlank(message = "Phone Number Required !!")
    @Size(min = 10,max = 11,message = "Invalid Number")
    private String mobileNumber;

    private String gender;

    private List<CustomerAddressRequestDto> address;

    private Long createdBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
