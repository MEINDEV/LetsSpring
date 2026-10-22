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

//        Map<Employee,String> roles = new HashMap<>();
//        roles.put(new Employee("E1"), "Developer");
//        System.out.println(roles.get(new Employee("E1")));

        Plan plan = Plan.fromCode("proffesional");
        System.out.println(plan);
        System.out.println(plan.allows(25));
        System.out.println(plan == Plan.PRO);

//        Set<Plan> paidPlans = EnumSet.of(Plan.PRO, Plan.TEAM);
//        Map<Plan, Integer> subscribers = new EnumMap<>(Plan.class);
//        subscribers.put(Plan.PRO, 120);

        // New switch expression under OrderStatus





    }
}
