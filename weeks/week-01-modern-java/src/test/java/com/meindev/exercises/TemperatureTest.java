package com.meindev.exercises;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.Test;

public class TemperatureTest{

    @Test
    public void absoluteZeroTest(){
        assertThatThrownBy(()-> new Temperature(-300))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("absolute zero");
    }

    @Test
    public void CelciusToFahrenheitTest(){
        double toFarenheit = Temperature.ofFahrenheit(252).celsius();
        assertThat(toFarenheit).isCloseTo(122.2222,within(0.0001));
    }
}