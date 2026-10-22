package com.meindev.examples;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PaymentTest{

    @Test
    public void cardCharge(){
        long cardFee = Payment.feePaise(new Payment.Card("4242",100_000));
        assertThat(cardFee).isEqualTo(2_000);
    }

    @Test
    public void upiCharge(){
        long upiFee = Payment.feePaise(new Payment.Upi("somee@upi",250_000));
        assertThat(upiFee).isEqualTo(12_500);
    }
}