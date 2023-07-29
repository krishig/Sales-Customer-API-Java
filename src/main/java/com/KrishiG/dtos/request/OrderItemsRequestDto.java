package com.KrishiG.dtos.request;

import com.KrishiG.enitites.Orders;
import com.KrishiG.enitites.Product;

import java.util.Date;

public class OrderItemsRequestDto {

    private Long id;

    private Orders orders;

    private Product product;

    private Float priceAfterDiscount;

    private int quantity;

    private String createdBy;

    private Date createdAt;

    private Date modifiedAt;

    private String modifiedBy;
}
