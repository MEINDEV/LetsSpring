package com.meindev.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A bank account. Unlike IndianRupee this is a MUTABLE entity: its balance and status change over time.
 * See TASK.md, Part B, for the rules.
 * Skeleton: keep the public method signatures; add any private fields and helpers you need.
 */
public class BankAccount {

    private final String accountNumber;
    private final String holderName;
    private final AccountType type;
    private IndianRupee balance;
    private AccountStatus status;
    private final List<Transaction> transactions;

    public String accountNumber(){
        return accountNumber;
    }
    public String holderName() { return holderName;}
    public AccountType type(){return type;}
    public AccountStatus status(){return status;}
    public IndianRupee balance(){return balance;}

    public BankAccount(String accountNumber, String holderName, AccountType type, IndianRupee openingDeposit) {

        Objects.requireNonNull(type);
        Objects.requireNonNull(openingDeposit);

        if(openingDeposit.isLessThan(type.minimumBalance())) throw new IllegalArgumentException();

        if(accountNumber == null || accountNumber.isBlank()) throw new IllegalArgumentException("Account must not be null or blank");
        if(holderName == null || holderName.isBlank()) throw new IllegalArgumentException("Account Holder name must not be null or blank");

        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.type = type;
        this.transactions = new ArrayList<>();
        transactions.add(new Transaction(TransactionType.DEPOSIT,openingDeposit,openingDeposit));
        this.balance = openingDeposit.isZero() ? IndianRupee.ZERO : openingDeposit;
        this.status = AccountStatus.ACTIVE;
    }

    public List<Transaction> transactions() {
        return List.copyOf(transactions);
    }

    public void deposit(IndianRupee amount) {

        if(AccountStatus.ACTIVE != status) throw new IllegalStateException();

        if(!amount.isGreaterThan(IndianRupee.ZERO)) throw new IllegalArgumentException();
        balance = this.balance.plus(amount);
        transactions.add(new Transaction(TransactionType.DEPOSIT,amount,balance));
    }

    public void withdraw(IndianRupee amount) {

        if(AccountStatus.ACTIVE != status) throw new IllegalStateException();

        if(!amount.isGreaterThan(IndianRupee.ZERO) ) throw new IllegalThreadStateException();
        var remaining_balance = this.balance.minus(amount);

        if(remaining_balance.isLessThan(this.type.minimumBalance())) throw new InsufficientFundsException("Insufficient funds");

        balance = remaining_balance.isZero() ? IndianRupee.ZERO : remaining_balance;
        transactions.add(new Transaction(TransactionType.WITHDRAWAL,amount,remaining_balance));
    }

    public void transferTo(BankAccount target, IndianRupee amount) {
        if(this.equals(target) || amount.isGreaterThan(balance)) throw new IllegalArgumentException();
        if((this.status != AccountStatus.ACTIVE) || (target.status != AccountStatus.ACTIVE)) throw new IllegalStateException();

        var balance_remaining = this.balance.minus(amount);
        if(balance_remaining.isLessThan(type.minimumBalance())) throw new InsufficientFundsException("Insufficient Funds");
        transactions.add(new Transaction(TransactionType.TRANSFER_OUT, amount, balance_remaining ));
        this.balance = balance_remaining;

        target.balance = target.balance.plus(amount);
        target.transactions.add(new Transaction(TransactionType.TRANSFER_IN,amount,target.balance));


    }

    /** @return the interest that was credited (ZERO if none) */
    public IndianRupee applyMonthlyInterest() {

        var annualInterest = this.balance.percent(this.type.annualInterestPercent().amount());
        if(annualInterest.compareTo(IndianRupee.ZERO) == 0){
            return IndianRupee.ZERO;
        }

        var monthlyInterest = annualInterest.amount().divide(BigDecimal.valueOf(12),2,RoundingMode.HALF_UP);

        System.out.printf("Monthly Interest of %,.2f is %,.2f%n",this.balance.amount(),monthlyInterest);

        var balanceAfterInterestApplied = new IndianRupee(monthlyInterest.add(this.balance.amount()));

        transactions.add(new Transaction(TransactionType.INTEREST, new IndianRupee(monthlyInterest),balanceAfterInterestApplied));

        balance = balanceAfterInterestApplied;

        return new IndianRupee(monthlyInterest);
    }

    public void freeze() {
        if(!this.status.canMoveTo(AccountStatus.FROZEN)) throw new IllegalStateException();

        this.status = AccountStatus.FROZEN;
    }

    public void unfreeze() {
        if(!this.status.canMoveTo(AccountStatus.ACTIVE)) throw new IllegalStateException();
        this.status = AccountStatus.ACTIVE;
    }

    /** @return the remaining balance paid out to the customer */
    public IndianRupee close() {
        if(!this.status.canMoveTo(AccountStatus.CLOSED)) throw new IllegalStateException();


        var payout = this.balance;
        this.balance = IndianRupee.ZERO;
        transactions.add(new Transaction(TransactionType.WITHDRAWAL,payout,this.balance));
        this.status = AccountStatus.CLOSED;

        return payout;
    }

    public String statement() {
        StringBuilder transactionLog = new StringBuilder();

        transactions.forEach(item-> {
                var type = "%s ".formatted(item.type());
                var amount = "₹%,.2f".formatted(item.amount().amount());
                var balance = "₹%,.2f".formatted(item.balanceAfter().amount());

                transactionLog.append("%-12s".formatted(type)).append("%14s".formatted(amount)).append("%14s".formatted(balance)).append("\n");
            }
        );

        String body = """
                Account: %s | %s | %s
                Status: %s
                %s
                %s%s
                Balance: ₹%,.2f
                """.formatted(accountNumber
                ,type
                ,holderName
                ,status
                ,"-".repeat(40)
                ,transactionLog.toString()
                ,"-".repeat(40)
                ,balance.amount()
        );

        return body;
    }
}
