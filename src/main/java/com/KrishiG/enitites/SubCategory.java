package com.KrishiG.enitites;

import jakarta.persistence.*;

public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "sub_category_name")
    private String subCategoryName;

    @Column(name = "category_id")
    @ManyToOne
    private Category categoryId;

    @Column(name = "image_url")
    private String imageUrl;


}
