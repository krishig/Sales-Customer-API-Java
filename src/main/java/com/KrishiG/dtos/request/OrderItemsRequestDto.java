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

    private Long createdBy;

    private Date createdDate;

    private Date modifiedDate;

    private Long modifiedBy;
}
