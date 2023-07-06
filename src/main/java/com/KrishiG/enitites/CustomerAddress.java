package com.KrishiG.enitites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "CUSTOMER_ADDRESS")
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    @Column(name = "house_number")
    private int houseNumber;

    @Column(name = "street_name_or_locality")
    private String streetName;

    @Column(name = "village_name")
    private String villageName;

    private String district;

    private String state;

    @Column(name = "postalcode")
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
