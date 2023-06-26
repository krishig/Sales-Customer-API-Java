package com.KrishiG.dtos.request;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCartDto {

    private Long id;

    private Long customerId;

    private Long createdBy;

    private Date createdAt;

    private Long modifiedBy;

    private Date modifiedAt;
}
