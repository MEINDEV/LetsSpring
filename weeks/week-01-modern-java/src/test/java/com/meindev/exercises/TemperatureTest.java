package com.meindev.exercises;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

public class TemperatureTest{

    @Test
    public void AbsoluteZeroTest(){
        assertThatThrownBy(()-> new Temperature(-300))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("absolute zero");
    }

    @Test
    public void CelciusToFahrenheitTest(){
        double toFarenheit = new Temperature(343).ofFahrenheit(212).celcius();
        assertThat(toFarenheit).isEqualTo(100);
    }
}