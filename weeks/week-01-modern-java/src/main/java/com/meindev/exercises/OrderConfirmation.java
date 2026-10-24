package com.meindev.exercises;

import java.math.BigDecimal;

public class OrderConfirmation {

    public static String orderConfirmationEmail(String customer,String orderId, BigDecimal amount){
        return """
                Hi %s,
                
                Your order %s is confirmed.
                Amount paid: %s%,.2f
                
                Thanks for shopping with us!
                """.formatted(customer,orderId,"\u20B9", amount);
    }
}
