package com.meindev.task;

import java.util.EnumSet;
/**
 * Allowed moves: ACTIVE → FROZEN, FROZEN → ACTIVE, ACTIVE → CLOSED. CLOSED is final.
 */
public enum AccountStatus {
    ACTIVE, FROZEN, CLOSED;

    public EnumSet<AccountStatus> allowedTo() {
       return switch (this) {
            case ACTIVE -> EnumSet.of(FROZEN, CLOSED);
            case FROZEN -> EnumSet.of(ACTIVE);
            case CLOSED -> EnumSet.noneOf(AccountStatus.class);
        };
    }

    public boolean canMoveTo(AccountStatus next) {
        return this.allowedTo().contains(next);
    }
}
