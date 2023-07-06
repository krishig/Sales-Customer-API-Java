package com.KrishiG.dtos.request;

import com.KrishiG.enitites.CartProducts;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
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
    private String userName;
    private String fullName;
    private String gender;
    private String emailId;
    private String password;
    private String mobileNumber;
    private String aadhaarNumber;
    private String houseNumber;
    private Long pinCode;
    private String district;
    private String city;
    private String state;
    private Long role;
    private String landMark;
    private Long createdBy;
    private Date createdAt;
    private Long modifiedBy;
    private Date modifiedAt;
}