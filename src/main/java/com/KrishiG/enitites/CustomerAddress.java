package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "USER_ADDRESSES")
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String address;

    @Column(name = "house_number")
    private int houseNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "village_name")
    private String villageName;

    private String district;

    private String state;

    @Column(name = "postal_code")
    private int postalCode;

    @Column(name = "crated_by")
    private int createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_by")
    private int modifiedBy;

    @Column(name = "modified_at")
    private Date modifiedAt;
}
