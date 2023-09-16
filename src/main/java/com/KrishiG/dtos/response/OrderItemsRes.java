package com.KrishiG.dtos.response;

import com.KrishiG.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsRes {
    private ProductResponseDto product;
    private Double priceAfterDiscount;
    private Double totalDiscountPrice;
    private int quantity;

}
