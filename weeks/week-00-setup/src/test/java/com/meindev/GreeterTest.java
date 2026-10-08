package com.meindev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class GreeterTest {
    private final Greeter greeter = new Greeter();

    @Test
    void greetsByName(){
        assertEquals("Hello, Asha!",greeter.greet("Asha"));
    }

    @Test 
    void fallBackToWorldWhenNameIsBlank(){
        assertEquals("Hello, World!", greeter.greet(" "));
    }

    @Test 
    void allNamesAreValid(){
        String[] names = {"Sundram","alice","bob","charlie"};
        assertEquals("Hello, Sundram!\nHello, alice!\nHello, bob!\nHello, charlie!", greeter.greetAll(Arrays.asList(names)));
    }

    @Test 
    void fewNamesAreValid(){
        String[] names = {"Sundram","","bob"," "};
        assertEquals("Hello, Sundram!\nHello, World!\nHello, bob!\nHello, World!", greeter.greetAll(Arrays.asList(names)));
    }
}