package com.KrishiG.dtos.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalCartProductResponseDto {
    private List<CartProductResponseDto> cartProductResponseDtoList;
    private Double totalPrice;
}
