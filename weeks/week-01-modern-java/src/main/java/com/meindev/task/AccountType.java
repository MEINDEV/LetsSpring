package com.meindev.task;

import java.math.BigDecimal;

/**
 * SAVINGS: minimum balance ₹1,000, interest 4% per year.
 * CURRENT: minimum balance ₹0, interest 0%.
 * Skeleton: add fields + a constructor so each constant carries its own values.
 */
public enum AccountType {
    SAVINGS(IndianRupee.of(1_000),IndianRupee.of(4)),
    CURRENT(IndianRupee.ZERO,IndianRupee.ZERO);

    private final IndianRupee minimumBalance;
    private final IndianRupee annualInterest;

    AccountType(IndianRupee minimumBalance, IndianRupee annualInterest){
        this.minimumBalance = minimumBalance;
        this.annualInterest = annualInterest;
    }

    public IndianRupee minimumBalance() {
        return this.minimumBalance;
    }

    /** For example, 4 for 4% per year. */
    public IndianRupee annualInterestPercent() {
        return this.annualInterest;
    }
}
