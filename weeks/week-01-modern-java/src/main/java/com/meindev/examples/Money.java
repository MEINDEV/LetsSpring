package com.meindev.examples;

import java.util.Objects;

public record Money(
        long amount, // default to INR
        String currency
){
    public Money{
        if(amount < 0){
            throw new IllegalArgumentException("amount must be > 0");
        }
        Objects.requireNonNull(currency,"currency must be null");
        currency = currency.strip().toUpperCase();
    }

    public Money plus(Money other){
        requireSameCurrency(other);
        return new Money(amount+other.amount, currency);
    }

    public boolean isGreaterThan(Money other){
        requireSameCurrency(other);
        return amount > other.amount;
    }

    private void requireSameCurrency(Money other){
        if (!currency.equals(other.currency)){
            throw new IllegalArgumentException(currency + " vs "+ other.currency);
        }
    }
}