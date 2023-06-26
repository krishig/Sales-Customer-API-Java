package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNumber;
    private String password;
    private String address;
    private Long pinCode;
    private String tehsil;
    private String city;
    private String state;
    private String gender;
    private String landMark;
    private Long role;
    private Long createdBy;
    private Date createdAt;
    private Long modifiedBy;
    private Date modifiedAt;
}
