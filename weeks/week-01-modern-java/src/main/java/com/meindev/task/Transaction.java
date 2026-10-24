package com.meindev.task;

// Provided: complete, do not change.
// One line in the account history: what happened, how much, and the balance right after it.
public record Transaction(TransactionType type, IndianRupee amount, IndianRupee balanceAfter) {
}
