package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SUBCATEGORY")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sub_category_name")
    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_Id")
    private Category category;

    @Column(name = "image_url")
    private String imageUrl;


}
