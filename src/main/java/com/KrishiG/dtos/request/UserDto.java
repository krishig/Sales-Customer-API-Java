package com.KrishiG.dtos.request;

import com.KrishiG.enitites.CartProducts;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @Size(min = 3, max = 15,message = "Invalid userName !!")
    private String userName;

    @Size(min = 3, max = 15, message = "Invalid fullName !!")
    private String fullName;

    @NotBlank(message = "Gender is Required")
    private String gender;

    @Email(message = "Invalid Email !!")
    private String emailId;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Mobile Number is Required")
    private String mobileNumber;

    @NotBlank(message = "Aadhaar Number is Required")
    @Size(max = 12,message = "Invalid aadhaar number!!")
    private String aadhaarNumber;

    @NotBlank(message = "HouseNumber Required ")
    private String houseNumber;

    @NotBlank(message = "Pin code Required")
    private Long pinCode;

    @NotBlank(message = "Please Enter District Name")
    private String district;

    @NotBlank(message = "City Name Required")
    private String city;

    @NotBlank(message = "State Name Required")
    private String state;

    @NotBlank(message = "Role is Required")
    private Long role;

    @NotBlank
    private String landMark;
    @NotBlank
    private Long createdBy;
    @NotBlank
    private Date createdAt;
    @NotBlank
    private Long modifiedBy;
    @NotBlank
    private Date modifiedAt;
}