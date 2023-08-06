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

    private Long createdBy;

    private Date createdDate;

    private Long modifiedBy;

    private Date modifiedDate;
}
