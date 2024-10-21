package com.KrishiG.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "DELIVERY_ADDRESS")
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
