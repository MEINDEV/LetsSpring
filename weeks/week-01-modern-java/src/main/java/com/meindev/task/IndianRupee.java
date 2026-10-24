package com.meindev.task;

import java.math.BigDecimal;

/**
 * Immutable money value in Indian Rupees. See TASK.md, Part A, for the rules.
 * Skeleton: replace every "TODO" with your implementation. You may add private helpers and constants.
 */
public record IndianRupee(BigDecimal amount) implements Comparable<IndianRupee> {

    public static final IndianRupee ZERO = new IndianRupee(BigDecimal.ZERO);

    public IndianRupee {
        // TODO: validation + normalization (compact constructor!)
    }

    public static IndianRupee of(String amount) {
        throw new UnsupportedOperationException("TODO");
    }

    public static IndianRupee of(long rupees) {
        throw new UnsupportedOperationException("TODO");
    }

    public static IndianRupee ofPaise(long paise) {
        throw new UnsupportedOperationException("TODO");
    }

    public IndianRupee plus(IndianRupee other) {
        throw new UnsupportedOperationException("TODO");
    }

    public IndianRupee minus(IndianRupee other) {
        throw new UnsupportedOperationException("TODO");
    }

    public IndianRupee times(int quantity) {
        throw new UnsupportedOperationException("TODO");
    }

    public IndianRupee percent(BigDecimal percent) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isGreaterThan(IndianRupee other) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isLessThan(IndianRupee other) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isZero() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int compareTo(IndianRupee other) {
        throw new UnsupportedOperationException("TODO");
    }

    public String format() {
        throw new UnsupportedOperationException("TODO");
    }
}
