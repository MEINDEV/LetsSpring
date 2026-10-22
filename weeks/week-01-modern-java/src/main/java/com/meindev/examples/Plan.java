package com.meindev.examples;

import java.util.Arrays;

public enum Plan{
    FREE("nocost",0,1),
    PRO("proffesional",499,10),
    TEAMS("enterprise",1_999,1_100);

    private final String code;
    private final long monthlyPriceRupees;
    private final int maxUsers;

    Plan(String code, long monthlyPriceRupees, int maxUsers){
        this.code = code;
        this.monthlyPriceRupees = monthlyPriceRupees;
        this.maxUsers = maxUsers;
    }

    public long monthlyPriceRupee(){ return monthlyPriceRupees;}
    public int maxUsers(){return maxUsers;}

    public boolean allows(int users){
        return users <= maxUsers;
    }

    public static Plan fromCode(String code){
        return Arrays.stream(values())
                .filter(plan->plan.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Unknow plan"+code));
    }
}