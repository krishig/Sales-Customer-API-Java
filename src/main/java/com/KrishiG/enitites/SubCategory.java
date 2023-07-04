package com.KrishiG.enitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "subCategory")
    private List<Product> productList;

    @ManyToOne
    @JoinColumn(name = "category_Id")
    private Category category;

    @Column(name = "image_url")
    private String imageUrl;


}
