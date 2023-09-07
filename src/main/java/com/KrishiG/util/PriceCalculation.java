package com.KrishiG.util;

public class PriceCalculation {

    public static double calculationDiscountPrice(double actualPrice, int discount) {
        double discountPrice = actualPrice - (actualPrice * discount) / 100;
        return discountPrice;
    }
}
