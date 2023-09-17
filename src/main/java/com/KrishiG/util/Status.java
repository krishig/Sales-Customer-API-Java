package com.KrishiG.util;

public enum Status {
    OPEN, //when sales person books the order for customer
    PACKAGING, //When admin start looking in order(start packaging)
    READY_TO_DISPATCH, //completed packaging and ready to deliver
    OUT_OF_DELIVERED, //package is out of delivery
    DELIVERED, //once packag is delivered to customer
    PENDING_DELIVER_ORDER
}
