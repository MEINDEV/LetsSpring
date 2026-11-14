package com.meindev.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable money value in Indian Rupees. See TASK.md, Part A, for the rules.
 * Skeleton: replace every "TODO" with your implementation. You may add private helpers and constants.
 */
public record IndianRupee(BigDecimal amount) implements Comparable<IndianRupee> {

    public static final IndianRupee ZERO = new IndianRupee(BigDecimal.ZERO);

    public IndianRupee {  
        // TODO: validation + normalization (compact constructor!)
        Objects.requireNonNull(amount);
    }

    public static IndianRupee of(String amount) {
        var bd = new BigDecimal(amount).stripTrailingZeros();

        if (bd.signum() == -1) throw new IllegalArgumentException();

        if (bd.scale() > 2) throw new IllegalArgumentException();

        return new IndianRupee((new BigDecimal(amount)).setScale(2, RoundingMode.HALF_UP));
    }

    public static IndianRupee of(long rupees) {
        var bd = BigDecimal.valueOf(rupees);
        return new IndianRupee(bd.setScale(2,RoundingMode.HALF_UP));
    }

    public static IndianRupee ofPaise(long paise) {
        return new IndianRupee(of(paise).amount.movePointLeft(2).setScale(2,RoundingMode.HALF_EVEN));
    }

    public IndianRupee plus(IndianRupee other) {
        return  new IndianRupee(this.amount.add(other.amount));
    }

    public IndianRupee minus(IndianRupee other) {
        if(!this.amount.max(other.amount).equals(this.amount)){
            throw new IllegalArgumentException();
        }
        return new IndianRupee(this.amount.subtract(other.amount));
    }

    public IndianRupee times(int quantity) {
        if(quantity ==0){
            return ZERO;
        }
        if(quantity < 0){
            throw new IllegalArgumentException();
        }
        return new IndianRupee(this.amount.multiply(new BigDecimal(quantity)));
    }

    public IndianRupee percent(BigDecimal percent) {
        var multiplier = percent.movePointLeft(2);
       return new IndianRupee(this.amount.multiply(multiplier).setScale(2,RoundingMode.HALF_UP));
    }

    public boolean isGreaterThan(IndianRupee other) {
        if (compareTo(other) == 0){
            return false;
        }
        return this.amount.max(other.amount).equals(this.amount);
    }

    public boolean isLessThan(IndianRupee other) {
        if (compareTo(other) == 0){
            return false;
        }
        return this.amount.min(other.amount).equals(this.amount);
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO)==0;
    }

    @Override
    public int compareTo(IndianRupee other) {
        return this.amount.compareTo(other.amount);
    }

    public String format() {
        return "%s%,.2f".formatted("\u20B9",this.amount);
    }
}
