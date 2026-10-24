package com.meindev.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * PROVIDED TESTS: do not modify. Write your own extra tests in BankAccountTest.java.
 */
class BankAccountProvidedTest {

    private static IndianRupee rs(String amount) {
        return IndianRupee.of(amount);
    }

    private static BankAccount savings(String openingDeposit) {
        return new BankAccount("SB-1001", "Asha Rao", AccountType.SAVINGS, rs(openingDeposit));
    }

    private static BankAccount current(String openingDeposit) {
        return new BankAccount("CA-2001", "Ravi Kumar", AccountType.CURRENT, rs(openingDeposit));
    }

    @Nested
    class AccountTypes {

        @Test
        void savingsAndCurrentRules() {
            assertThat(AccountType.SAVINGS.minimumBalance()).isEqualTo(rs("1000"));
            assertThat(AccountType.SAVINGS.annualInterestPercent()).isEqualByComparingTo("4");
            assertThat(AccountType.CURRENT.minimumBalance()).isEqualTo(IndianRupee.ZERO);
            assertThat(AccountType.CURRENT.annualInterestPercent()).isEqualByComparingTo("0");
        }
    }

    @Nested
    class Opening {

        @Test
        void opensActiveWithOpeningDepositRecorded() {
            BankAccount account = savings("5000");

            assertThat(account.accountNumber()).isEqualTo("SB-1001");
            assertThat(account.holderName()).isEqualTo("Asha Rao");
            assertThat(account.type()).isEqualTo(AccountType.SAVINGS);
            assertThat(account.status()).isEqualTo(AccountStatus.ACTIVE);
            assertThat(account.balance()).isEqualTo(rs("5000"));
            assertThat(account.transactions())
                    .containsExactly(new Transaction(TransactionType.DEPOSIT, rs("5000"), rs("5000")));
        }

        @Test
        void savingsRequiresMinimumOpeningDeposit() {
            assertThatThrownBy(() -> savings("999.99"))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThat(savings("1000").balance()).isEqualTo(rs("1000"));   // boundary is allowed
        }

        @Test
        void currentAccountCanOpenWithZero() {
            assertThat(current("0").balance()).isEqualTo(IndianRupee.ZERO);
        }

        @Test
        void rejectsBlankAccountNumberOrHolderName() {
            assertThatThrownBy(() -> new BankAccount(" ", "Asha", AccountType.CURRENT, rs("0")))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new BankAccount("CA-1", "", AccountType.CURRENT, rs("0")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class DepositAndWithdraw {

        @Test
        void depositIncreasesBalance() {
            BankAccount account = current("100");

            account.deposit(rs("50.25"));

            assertThat(account.balance()).isEqualTo(rs("150.25"));
            assertThat(account.transactions()).last()
                    .isEqualTo(new Transaction(TransactionType.DEPOSIT, rs("50.25"), rs("150.25")));
        }

        @Test
        void zeroAmountIsRejected() {
            BankAccount account = current("100");

            assertThatThrownBy(() -> account.deposit(IndianRupee.ZERO)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> account.withdraw(IndianRupee.ZERO)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void withdrawDecreasesBalance() {
            BankAccount account = savings("5000");

            account.withdraw(rs("1200.50"));

            assertThat(account.balance()).isEqualTo(rs("3799.50"));
            assertThat(account.transactions()).last()
                    .isEqualTo(new Transaction(TransactionType.WITHDRAWAL, rs("1200.50"), rs("3799.50")));
        }

        @Test
        void savingsCanWithdrawExactlyDownToMinimumBalance() {
            BankAccount account = savings("5000");

            account.withdraw(rs("4000"));

            assertThat(account.balance()).isEqualTo(rs("1000"));
        }

        @Test
        void savingsCannotGoBelowMinimumBalance() {
            BankAccount account = savings("5000");

            assertThatThrownBy(() -> account.withdraw(rs("4000.01")))
                    .isInstanceOf(InsufficientFundsException.class)
                    .hasMessageContaining("Insufficient funds");

            assertThat(account.balance()).isEqualTo(rs("5000"));   // nothing changed
            assertThat(account.transactions()).hasSize(1);          // no failed transaction recorded
        }

        @Test
        void currentAccountCanWithdrawEverything() {
            BankAccount account = current("300");

            account.withdraw(rs("300"));

            assertThat(account.balance()).isEqualTo(IndianRupee.ZERO);
        }
    }

    @Nested
    class Transfers {

        @Test
        void movesMoneyBetweenAccounts() {
            BankAccount from = savings("5000");
            BankAccount to = current("100");

            from.transferTo(to, rs("1500"));

            assertThat(from.balance()).isEqualTo(rs("3500"));
            assertThat(to.balance()).isEqualTo(rs("1600"));
            assertThat(from.transactions()).last()
                    .isEqualTo(new Transaction(TransactionType.TRANSFER_OUT, rs("1500"), rs("3500")));
            assertThat(to.transactions()).last()
                    .isEqualTo(new Transaction(TransactionType.TRANSFER_IN, rs("1500"), rs("1600")));
        }

        @Test
        void failedTransferChangesNeitherAccount() {
            BankAccount from = savings("2000");
            BankAccount to = current("100");

            assertThatThrownBy(() -> from.transferTo(to, rs("1500")))
                    .isInstanceOf(InsufficientFundsException.class);

            assertThat(from.balance()).isEqualTo(rs("2000"));
            assertThat(to.balance()).isEqualTo(rs("100"));
            assertThat(to.transactions()).hasSize(1);
        }

        @Test
        void cannotTransferToFrozenAccount() {
            BankAccount from = savings("5000");
            BankAccount to = current("100");
            to.freeze();

            assertThatThrownBy(() -> from.transferTo(to, rs("500")))
                    .isInstanceOf(IllegalStateException.class);

            assertThat(from.balance()).isEqualTo(rs("5000"));   // money must not disappear!
        }

        @Test
        void cannotTransferToSameAccount() {
            BankAccount account = savings("5000");

            assertThatThrownBy(() -> account.transferTo(account, rs("10")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Lifecycle {

        @Test
        void frozenAccountRejectsMoneyMovementUntilUnfrozen() {
            BankAccount account = current("100");

            account.freeze();
            assertThat(account.status()).isEqualTo(AccountStatus.FROZEN);
            assertThatThrownBy(() -> account.deposit(rs("1"))).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> account.withdraw(rs("1"))).isInstanceOf(IllegalStateException.class);

            account.unfreeze();
            account.deposit(rs("1"));
            assertThat(account.balance()).isEqualTo(rs("101"));
        }

        @Test
        void closingPaysOutRemainingBalance() {
            BankAccount account = savings("2500.75");

            IndianRupee payout = account.close();

            assertThat(payout).isEqualTo(rs("2500.75"));
            assertThat(account.balance()).isEqualTo(IndianRupee.ZERO);
            assertThat(account.status()).isEqualTo(AccountStatus.CLOSED);
            assertThat(account.transactions()).last()
                    .isEqualTo(new Transaction(TransactionType.WITHDRAWAL, rs("2500.75"), IndianRupee.ZERO));
        }

        @Test
        void closedAccountIsFinal() {
            BankAccount account = current("10");
            account.close();

            assertThatThrownBy(account::unfreeze).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> account.deposit(rs("1"))).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(account::close).isInstanceOf(IllegalStateException.class);
        }

        @Test
        void frozenAccountCannotBeClosed() {
            BankAccount account = current("10");
            account.freeze();

            assertThatThrownBy(account::close).isInstanceOf(IllegalStateException.class);
            assertThat(account.balance()).isEqualTo(rs("10"));
        }

        @Test
        void statusTransitions() {
            assertThat(AccountStatus.ACTIVE.canMoveTo(AccountStatus.FROZEN)).isTrue();
            assertThat(AccountStatus.ACTIVE.canMoveTo(AccountStatus.CLOSED)).isTrue();
            assertThat(AccountStatus.FROZEN.canMoveTo(AccountStatus.ACTIVE)).isTrue();
            assertThat(AccountStatus.FROZEN.canMoveTo(AccountStatus.CLOSED)).isFalse();
            assertThat(AccountStatus.CLOSED.canMoveTo(AccountStatus.ACTIVE)).isFalse();
            assertThat(AccountStatus.ACTIVE.canMoveTo(AccountStatus.ACTIVE)).isFalse();
        }
    }

    @Nested
    class Interest {

        @Test
        void savingsEarnsMonthlyInterest() {
            BankAccount account = savings("10000");

            // 10,000 x 4% / 12 = 33.333..., which rounds to 33.33
            IndianRupee interest = account.applyMonthlyInterest();

            assertThat(interest).isEqualTo(rs("33.33"));
            assertThat(account.balance()).isEqualTo(rs("10033.33"));
            assertThat(account.transactions()).last()
                    .isEqualTo(new Transaction(TransactionType.INTEREST, rs("33.33"), rs("10033.33")));
        }

        @Test
        void currentAccountEarnsNoInterestAndRecordsNothing() {
            BankAccount account = current("10000");

            assertThat(account.applyMonthlyInterest()).isEqualTo(IndianRupee.ZERO);
            assertThat(account.transactions()).hasSize(1);
        }
    }

    @Nested
    class History {

        @Test
        void transactionHistoryCannotBeModifiedFromOutside() {
            BankAccount account = current("100");

            assertThatThrownBy(() -> account.transactions().clear())
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        void printsStatement() {
            BankAccount account = savings("5000");
            account.withdraw(rs("1200.50"));
            account.deposit(rs("250"));

            String expected = """
                    Account: SB-1001 | SAVINGS | Asha Rao
                    Status: ACTIVE
                    ----------------------------------------
                    DEPOSIT          ₹5,000.00     ₹5,000.00
                    WITHDRAWAL       ₹1,200.50     ₹3,799.50
                    DEPOSIT            ₹250.00     ₹4,049.50
                    ----------------------------------------
                    Balance: ₹4,049.50
                    """;

            assertThat(account.statement()).isEqualTo(expected);
        }
    }
}
