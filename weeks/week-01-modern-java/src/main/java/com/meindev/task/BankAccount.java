package com.meindev.task;

import java.util.List;

/**
 * A bank account. Unlike IndianRupee this is a MUTABLE entity: its balance and status change over time.
 * See TASK.md, Part B, for the rules.
 * Skeleton: keep the public method signatures; add any private fields and helpers you need.
 */
public class BankAccount {

    public BankAccount(String accountNumber, String holderName, AccountType type, IndianRupee openingDeposit) {
        // TODO
    }

    public String accountNumber() {
        throw new UnsupportedOperationException("TODO");
    }

    public String holderName() {
        throw new UnsupportedOperationException("TODO");
    }

    public AccountType type() {
        throw new UnsupportedOperationException("TODO");
    }

    public AccountStatus status() {
        throw new UnsupportedOperationException("TODO");
    }

    public IndianRupee balance() {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Transaction> transactions() {
        throw new UnsupportedOperationException("TODO");
    }

    public void deposit(IndianRupee amount) {
        throw new UnsupportedOperationException("TODO");
    }

    public void withdraw(IndianRupee amount) {
        throw new UnsupportedOperationException("TODO");
    }

    public void transferTo(BankAccount target, IndianRupee amount) {
        throw new UnsupportedOperationException("TODO");
    }

    /** @return the interest that was credited (ZERO if none) */
    public IndianRupee applyMonthlyInterest() {
        throw new UnsupportedOperationException("TODO");
    }

    public void freeze() {
        throw new UnsupportedOperationException("TODO");
    }

    public void unfreeze() {
        throw new UnsupportedOperationException("TODO");
    }

    /** @return the remaining balance paid out to the customer */
    public IndianRupee close() {
        throw new UnsupportedOperationException("TODO");
    }

    public String statement() {
        throw new UnsupportedOperationException("TODO");
    }
}
