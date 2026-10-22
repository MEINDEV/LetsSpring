package com.meindev.examples;

public sealed interface Payment permits Payment.Card, Payment.Upi, Payment.Cash{
    record Card(String last4Digits, long amountPaise) implements Payment {}
    record Upi(String upiId, long amountPaise) implements Payment{}
    record Cash(long amountRupee) implements Payment{}

    // Fee rules: card 2% UPI free up to 2000 then 0.5%, cash free
    static long feePaise(Payment payment){
        return switch (payment){
            case Card(String last4, long amount) -> amount * 2 / 100;
            case Upi(String id, long amount) when amount <= 200_000 -> 0;
            case Upi(String id, long amount) -> amount * 5/100;
            case Cash cas -> 0;
        };
    }
}