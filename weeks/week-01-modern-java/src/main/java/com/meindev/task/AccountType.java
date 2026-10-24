package com.meindev.task;

import java.math.BigDecimal;

/**
 * SAVINGS: minimum balance ₹1,000, interest 4% per year.
 * CURRENT: minimum balance ₹0, interest 0%.
 * Skeleton: add fields + a constructor so each constant carries its own values.
 */
public enum AccountType {
    SAVINGS,
    CURRENT;

    public IndianRupee minimumBalance() {
        throw new UnsupportedOperationException("TODO");
    }

    /** For example, 4 for 4% per year. */
    public BigDecimal annualInterestPercent() {
        throw new UnsupportedOperationException("TODO");
    }
}
