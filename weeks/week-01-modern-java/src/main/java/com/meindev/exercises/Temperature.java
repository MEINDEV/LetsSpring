package com.meindev.exercises;

public record Temperature(
         double celcius
) {
    public Temperature(double celcius){
        if (celcius <  -273.15){
            throw new IllegalArgumentException("absolute zero");
        }
        this.celcius = celcius;
    }

    double fahrenheit(){
        return celcius * 9/5 + 32;
    }

    Temperature ofFahrenheit(double f){
        double cel = (f - 32)*5/9;
        return new Temperature(cel);
    }
}