package com.meindev.exercises;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BillSplitterTest {

    public final
    BigDecimal total = new BigDecimal(100);

    @Test
    public void zeroOrNegativePeopleThrowIllegalArgException(){


        SoftAssertions.assertSoftly(softly -> {
            softly.assertThatThrownBy(()->BillSplitter.split(total, 0)).isInstanceOf(IllegalArgumentException.class);
            softly.assertThatThrownBy(()->BillSplitter.split(total,-1)).isInstanceOf(IllegalArgumentException.class);
        });

    }

    @Test
    public void firstGetsTheRemainder(){
        List<BigDecimal> shares = BillSplitter.split(total,3);

        assertThat(shares.getFirst()).isEqualByComparingTo(new BigDecimal("33.34"));
        assertThat(shares.get(1)).isEqualByComparingTo(new BigDecimal("33.33"));
        assertThat(shares.get(2)).isEqualByComparingTo(new BigDecimal("33.33"));
    }

    @Test
    public void equalDivision(){
        List<BigDecimal> shares = BillSplitter.split(total,4);

        shares.forEach(share -> assertThat(share).isEqualByComparingTo(new BigDecimal("25.00")));
    }
}
