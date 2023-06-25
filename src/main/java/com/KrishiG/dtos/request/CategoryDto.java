package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Long id;

    private String categoryName;

    private int imageId;

    private String createdBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;
}
