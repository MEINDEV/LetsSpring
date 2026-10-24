package com.meindev.exercises;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BigDecimalLearningTest {

    @Test
    public void doubleAdditionGivesIncorrectSum(){
        double a = 0.1;
        double b = 0.2;

        var sum = a+b;

        assertThat(sum == 0.3).as("double 0.2 and 0.1 addition is not equal to 0.3").isFalse();
    }

    @Test
    public void bigDecimalAddition(){
        var a = new BigDecimal("0.1");
        var b = new BigDecimal("0.2");

        var result = a.add(b);

        assertThat(result.equals(BigDecimal.valueOf(0.3))).isTrue(); // its giving true
    }

    @Test
    public void bigDecimalValueComparison(){
        var bd1 = new BigDecimal(0.1);
        var bd2 = new BigDecimal("0.1");

        assertThat(bd1.equals(bd2)).isFalse();  // its giving false
    }


    // 2.0 and 2.00 are not equals, but compareTo returns 0. (AssertJ also has isEqualByComparingTo(...). Try it!)

    @Test
    public void bigDecimalEqualsComparison(){
        var bd1 = new BigDecimal("2.0");
        var bd2 = new BigDecimal("2.00");

        // This is failing.
        // assertThat(bd1.equals(bd2)).isTrue();

        // this gives True
//        assertThat(bd1.compareTo(bd2)==0).isTrue();
        assertThat(bd1).isEqualByComparingTo(bd2);
    }

//    10 / 3 without a rounding mode throws ArithmeticException
    @Test
    public void divisionThrowArithmeticException(){
        var bd1 = new BigDecimal("10");
        var bd2 = new BigDecimal("3");

//        Below throws exception
        assertThatThrownBy( ()  -> {
                    bd1.divide(bd2);
                }
        ).isInstanceOf(ArithmeticException.class);


//        Fix
//        assertThatThrownBy( ()  -> {
//                    bd1.divide(bd2,RoundingMode.HALF_UP);
//                }
//        ).isInstanceOf(ArithmeticException.class);
    }


//    add() does not change the original object
    @Test
    public void bigDecimalObjectIsImmutable(){

        var bd1 = new BigDecimal("34");
        var bd2 = new BigDecimal("37");

        var sum = bd1.add(bd2);

        assertThat(bd1.toString()).isEqualTo("34"); // True
        assertThat(sum.toString()).isEqualTo("71"); // Ture


    }
}
