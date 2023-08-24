package com.KrishiG.dtos.request;

import com.KrishiG.util.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusRequestDto {
    Status status;
}
