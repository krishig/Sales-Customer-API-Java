package com.KrishiG.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsAndCountResponseDto {
    private PageableResponse<OrderResponseDto> orderResponseDto;
    private int outOfDeliveryCount;
    private int deliveredCount;
    private double totalCash;
    private int totalOrderCount;
    private int pendingDeliveryCount;
}
