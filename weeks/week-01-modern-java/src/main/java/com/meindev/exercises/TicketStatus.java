package com.meindev.exercises;

import java.util.EnumSet;

public enum TicketStatus {
    OPEN,IN_PROGRESS,RESOLVED,CLOSED;

    EnumSet<TicketStatus> allowedTo(){
        return switch(this){
            case OPEN -> EnumSet.of(IN_PROGRESS,CLOSED);
            case IN_PROGRESS -> EnumSet.of(RESOLVED);
            case RESOLVED -> EnumSet.of(IN_PROGRESS,CLOSED);
            case CLOSED ->  EnumSet.noneOf(TicketStatus.class);
        };
    }

    boolean canMoveTo(TicketStatus next){
        return this.allowedTo().contains(next);
    }

    boolean isFinal(){
        return this.allowedTo().isEmpty();
    }
}
