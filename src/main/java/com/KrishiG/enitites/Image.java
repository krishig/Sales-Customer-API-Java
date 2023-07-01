package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "IMAGES")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_at")
    private Date modifiedAt;

}
