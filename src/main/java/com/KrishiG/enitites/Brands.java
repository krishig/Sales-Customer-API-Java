package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "BRANDS")
public class Brands {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "brand")
    private List<Product> products;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "brand_image_url", columnDefinition = "TEXT")
    private String brandImageUrl;

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
