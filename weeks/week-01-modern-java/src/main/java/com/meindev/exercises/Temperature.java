package com.meindev.exercises;

public record Temperature(
         double celsius
) {
    public Temperature(double celsius){
        if (celsius <  -273.15){
            throw new IllegalArgumentException("absolute zero");
        }
        this.celsius = celsius;
    }

    double fahrenheit(){
        return celsius * 9/5 + 32;
    }

    static Temperature ofFahrenheit(double f){
        double cel = (f - 32)*5/9;
        return new Temperature(cel);
    }
}