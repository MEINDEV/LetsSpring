package com.meindev.task;

import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

public class BankAccountTest {



    @Test
    public void transferringFromFrozenAccountThrowIllegalStateException(){
        BankAccount sourceAccount = new BankAccount(
                "IDBX-343",
                "Rohan Lal",
                AccountType.CURRENT,
                IndianRupee.of(10_000)
        );

        BankAccount targetAccount = new BankAccount(
                "IDBX-343",
                "Rohan Lal",
                AccountType.CURRENT,
                IndianRupee.of(10_000)
        );

        sourceAccount.freeze();

        var transferAmount = IndianRupee.of(5_000);

        assertThatThrownBy(()-> sourceAccount.transferTo(targetAccount,transferAmount))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    public void interestOnSavingAccountHavingMinimumBalance(){
        var sa = new BankAccount(
            "IDBX-344",
            "Ben Rack",
                AccountType.SAVINGS,
                IndianRupee.of("1000")
        );

        IndianRupee interest = sa.applyMonthlyInterest();

        assertThat(interest).isEqualByComparingTo(IndianRupee.of("3.33"));
    }

    @Test
    public void payoutMustEqualToAllTheDeposits(){
        var sa = new BankAccount(
                "IDBX-344",
                "Ben Rack",
                AccountType.SAVINGS,
                IndianRupee.of("1000")
        );

        sa.deposit(IndianRupee.of(500));
        sa.deposit(IndianRupee.of(246));

        assertThat(sa.close()).isEqualByComparingTo(IndianRupee.of(1746));
    }

    @Test
    public void transactionsShouldBeImmutable(){
        var sa = new BankAccount(
                "IDBX-344",
                "Ben Rack",
                AccountType.SAVINGS,
                IndianRupee.of("1000")
        );

        sa.deposit(IndianRupee.of(500));
        sa.deposit(IndianRupee.of(246));

        assertThatThrownBy(
                ()-> sa.transactions().add(new Transaction(TransactionType.DEPOSIT,IndianRupee.of("350"),IndianRupee.of("10000")))
        ).isInstanceOf(UnsupportedOperationException.class);
    }

}
