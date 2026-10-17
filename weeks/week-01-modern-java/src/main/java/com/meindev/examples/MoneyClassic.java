package com.meindev.examples;

import java.util.Objects;

public final class MoneyClassic{
    private final long amountInPaise;
    private final String currency;

    public MoneyClassic(long amountInPaise,String currency){
        if (amountInPaise < 0){
            throw new IllegalArgumentException("amount must be > 0");
        }

        this.amountInPaise = amountInPaise;
        this.currency = currency;
    }

    public long getAmountInPaise(){
        return amountInPaise;
    }

    public String getCurrency(){
        return currency;
    }

    public MoneyClassic plus(MoneyClassic other){
        if(!currency.equals(other.currency)){
            throw new IllegalArgumentException("currency mismatch");
        }

        return new MoneyClassic(amountInPaise + other.amountInPaise,currency);
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if(!(o instanceof MoneyClassic other)) return false;
        return amountInPaise == other.amountInPaise && currency.equals(other.currency);
    }

    public int hashCode(){
        return Objects.hash(amountInPaise, currency);
    }

    public String toString(){
        return "MoneyClassic[" + amountInPaise + " " + currency + "]";
    }
}