package com.meindev.task;

/**
 * Allowed moves: ACTIVE → FROZEN, FROZEN → ACTIVE, ACTIVE → CLOSED. CLOSED is final.
 */
public enum AccountStatus {
    ACTIVE, FROZEN, CLOSED;

    public boolean canMoveTo(AccountStatus next) {
        throw new UnsupportedOperationException("TODO");
    }
}
