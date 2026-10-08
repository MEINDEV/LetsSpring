package com.meindev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GreaterTest{
    private final Greeter greeter = new Greeter();

    @Test
    void greetsByName(){
        assertEquals("Hello, Asha!",greeter.greet("Asha"));
    }

    @Test 
    void fallBackToWorldWhenNameIsBlank(){
        assertEquals("Hello, World!", greeter.greet(""));
    }
}