package com.meindev.examples;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.assertj.core.api.SoftAssertions;


import org.junit.jupiter.api.Test;


public class OrderStatusTest{

    @Test
    public void createdNextStatusIsPaid(){
        assertThat(OrderStatus.CREATED.canMoveTo(OrderStatus.PAID)).isTrue();
    }

    @Test
    public void createdNextStatusIsNotDelivered(){
        assertThat(OrderStatus.CREATED.canMoveTo(OrderStatus.DELIVERED)).isFalse();
    }

    @Test
    public void isValidNextOrderStatus(){
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(OrderStatus.CREATED.canMoveTo(OrderStatus.DELIVERED)).isFalse();
            softly.assertThat(OrderStatus.DELIVERED.isFinal()).isTrue();
            softly.assertThat(OrderStatus.PAID.canMoveTo(OrderStatus.CANCELLED)).isTrue();
        });
    }


}