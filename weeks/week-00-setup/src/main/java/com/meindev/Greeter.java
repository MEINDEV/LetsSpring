package com.meindev;
public class Greeter{
    public String greet(String name){
        if (name == null || name.isBlank()){
            return "Hello, World!";
        }

        return "Hello, "+name.strip() +"!";
    }

    public static void main(String[] args){
        Greeter greeter = new Greeter();
        System.out.println(greeter.greet("Sundram"));
    }
}