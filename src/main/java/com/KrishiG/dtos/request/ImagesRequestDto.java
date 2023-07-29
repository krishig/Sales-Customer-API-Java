package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagesRequestDto {
    private Long id;

    private Long product_id;

    private String imageName;

    private String imageUrl;

    private Long createdBy;

    private Date createdAt;

    private Long modifiedBy;

    private Date modifiedAt;
}
