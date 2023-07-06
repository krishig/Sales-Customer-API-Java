package com.KrishiG.dtos.request;


import jakarta.persistence.Column;

import java.util.Date;

public class SubCategoryDto
{

    private Long id;

    private String subCategoryName;

    private Long categoryId;

    private String imageUrl;

    private String createdBy;

    private Date createdDate;

    private String modifiedBy;

    private Date modifiedAt;
}
