package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImagesDto {
    private Long id;

    private Long product_id;

    private String imageName;

    private String imageUrl;

    private String createdBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;
}
