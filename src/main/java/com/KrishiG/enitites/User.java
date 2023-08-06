package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "fullname")
    private String fullName;

    private String gender;

    @Column(name = "email")
    private String emailId;

    private String password;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "aadhaar_number")
    private String aadhaarNumber;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "pincode")
    private Long pinCode;

    private String district;

    private String city;

    private String state;

    @Column(name = "Role")
    private Long role;

    @Column(name = "landmark")
    private String landMark;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private Date modifiedDate;

}
