package com.meindev.examples;

import java.util.Set;
import java.util.EnumSet;


public enum OrderStatus{
    CREATED,PAID,SHIPPED,DELIVERED,CANCELLED;

    public Set<OrderStatus> allowedNext(){
        return switch(this){
            case CREATED -> EnumSet.of(PAID, CANCELLED);
            case PAID -> EnumSet.of(SHIPPED, CANCELLED);
            case SHIPPED -> EnumSet.of(DELIVERED);
            case DELIVERED, CANCELLED -> EnumSet.noneOf(OrderStatus.class);
        };
    }

    public boolean canMoveTo(OrderStatus next){
        return allowedNext().contains(next);
    }

    public boolean isFinal(){
        return allowedNext().isEmpty();
    }
}