package com.meindev;

import com.meindev.examples.*;
import com.meindev.exercises.*;
import java.util.Map;
import java.util.HashMap;

import java.util.Objects;

public class Main{
    public static void main(String []args){
//        Money a = new Money(1000,"INR");
//        System.out.println(a);

        Map<Employee,String> roles = new HashMap<>();
        roles.put(new Employee("E1"), "Developer");
        System.out.println(roles.get(new Employee("E1")));
    }
}
