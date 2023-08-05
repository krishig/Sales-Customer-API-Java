package com.KrishiG.util;

public class PriceCalculation {

    public static double calculationDiscountPrice(double actualPrice) {
        double discountPrice = actualPrice - (actualPrice * 10) / 100;
        return discountPrice;
    }
}
