package com.meindev.examples;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GSTCalculator {
    private static final BigDecimal GST_RATE = BigDecimal.valueOf(0.18);
    private static final int PAISE_SCALE = 2;

    public static BigDecimal gstOn(BigDecimal amount){
        return amount.multiply(GST_RATE).setScale(PAISE_SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal grossOf(BigDecimal amount){
        return amount.add(gstOn(amount));
    }

    public static void main(String[] args) {
        var amount = new BigDecimal("54353");
        System.out.println(gstOn(amount));
        System.out.println(grossOf(amount));
    }
}
