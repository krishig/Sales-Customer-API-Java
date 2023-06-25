package com.KrishiG.dtos.request;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
    private Long id;

    private String roleName;

    private String createdBy;

    private Date createdAt;

    private String modifiedBy;

    private Date modifiedAt;
}
