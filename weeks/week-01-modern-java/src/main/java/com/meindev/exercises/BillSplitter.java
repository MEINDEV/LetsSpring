package com.meindev.exercises;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;

public class BillSplitter {
    public static final int PAISE_SCALE = 2;

    public static List<BigDecimal> split(BigDecimal total, int people){

        if(people <= 0){
            throw new IllegalArgumentException("People must be a positive number");
        }

        var eachShare = total.divide(new BigDecimal(people),PAISE_SCALE, RoundingMode.DOWN);
        var difference = total.subtract(eachShare.multiply(new BigDecimal((people))));

//        BigDecimal [] eachShare = total.divideAndRemainder(new BigDecimal(people));

        var shares = new ArrayList<BigDecimal>();

        for (int i = 0; i < people; i++) {

            if (i==0){
                shares.add(eachShare.add(difference));
                continue;
            }

            shares.add(eachShare);

        }

        return shares;
    }

}
