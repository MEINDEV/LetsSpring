package com.meindev.exercises;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderConfirmationTest {
    @Test
    public void validateEmailConfirmationEmailMessage(){
        String expected = """
                Hi Asha,
                
                Your order ODR-42 is confirmed.
                Amount paid: ₹1,179.99
                
                Thanks for shopping with us!
                """;

        String customer = "Asha";
        String orderId = "ODR-42";
        var amount = new BigDecimal("1179.99");

        String emailMessage = OrderConfirmation.orderConfirmationEmail(customer,orderId,amount);

        assertThat(emailMessage).isEqualTo(expected);
    }
}
