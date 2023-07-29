package com.KrishiG.dtos.request;

import com.KrishiG.enitites.SubCategory;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestDto {

    private Long id;

    private String categoryName;

    private SubCategory subCategory;

    private String createdBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;
}
