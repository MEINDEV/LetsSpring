package com.meindev;

import java.util.List;
import java.util.stream.Collectors;

public class Greeter{
    public String greet(String name){
        if (name == null || name.isBlank()){
            return "Hello, World!";
        }

        return "Hello, "+name.strip() +"!";
    }

    public String greetAll(List<String> names){
        return names.stream()
            .map(this::greet)
            .collect(Collectors.joining("\n"));
    }

    public static void main(String[] args){
        Greeter greeter = new Greeter();
        System.out.println(greeter.greet("Sundram"));

        List<String> names = List.of("alice","bob","charlie");
        System.out.println(greeter.greetAll(names));
    }
}