package com.meindev.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * PROVIDED TESTS: do not modify. Write your own extra tests in IndianRupeeTest.java.
 */
class IndianRupeeProvidedTest {

    @Nested
    class Creation {

        @Test
        void normalizesScaleToTwoDecimals() {
            assertThat(IndianRupee.of("10.5").amount().toPlainString()).isEqualTo("10.50");
            assertThat(IndianRupee.of(500).amount().toPlainString()).isEqualTo("500.00");
        }

        @Test
        void createsFromPaise() {
            assertThat(IndianRupee.ofPaise(12_345)).isEqualTo(IndianRupee.of("123.45"));
        }

        @Test
        void sameValueWithDifferentScaleIsEqual() {
            assertThat(IndianRupee.of("5")).isEqualTo(IndianRupee.of("5.00"));
            assertThat(IndianRupee.of("1.500")).isEqualTo(IndianRupee.of("1.5"));
        }

        @Test
        void worksAsHashSetElement() {
            Set<IndianRupee> set = new HashSet<>();
            set.add(IndianRupee.of("5"));
            set.add(IndianRupee.of("5.00"));
            assertThat(set).hasSize(1);
        }

        @Test
        void rejectsNegativeAmount() {
            assertThatThrownBy(() -> IndianRupee.of("-0.01"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void rejectsMoreThanTwoDecimalPlaces() {
            assertThatThrownBy(() -> IndianRupee.of("1.005"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void rejectsNull() {
            assertThatThrownBy(() -> new IndianRupee(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void zeroConstantIsZero() {
            assertThat(IndianRupee.ZERO.isZero()).isTrue();
            assertThat(IndianRupee.of("0.01").isZero()).isFalse();
        }
    }

    @Nested
    class Arithmetic {

        @Test
        void plusAndMinusReturnNewValues() {
            IndianRupee hundred = IndianRupee.of(100);

            assertThat(hundred.plus(IndianRupee.of("0.50"))).isEqualTo(IndianRupee.of("100.50"));
            assertThat(hundred.minus(IndianRupee.of("99.99"))).isEqualTo(IndianRupee.of("0.01"));
            assertThat(hundred).isEqualTo(IndianRupee.of(100));   // unchanged: immutable
        }

        @Test
        void minusBelowZeroIsRejected() {
            assertThatThrownBy(() -> IndianRupee.of(10).minus(IndianRupee.of("10.01")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void timesMultipliesByQuantity() {
            assertThat(IndianRupee.of("19.99").times(3)).isEqualTo(IndianRupee.of("59.97"));
            assertThat(IndianRupee.of("19.99").times(0)).isEqualTo(IndianRupee.ZERO);
        }

        @Test
        void timesRejectsNegativeQuantity() {
            assertThatThrownBy(() -> IndianRupee.of(1).times(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void percentRoundsHalfUpToPaise() {
            // 999.99 x 18% = 179.9982, which rounds to 180.00
            assertThat(IndianRupee.of("999.99").percent(new BigDecimal("18"))).isEqualTo(IndianRupee.of("180.00"));
            // 0.25 x 10% = 0.025, which rounds HALF_UP to 0.03
            assertThat(IndianRupee.of("0.25").percent(BigDecimal.TEN)).isEqualTo(IndianRupee.of("0.03"));
        }
    }

    @Nested
    class Comparison {

        @Test
        void comparesByValue() {
            IndianRupee small = IndianRupee.of("10.00");
            IndianRupee big = IndianRupee.of("10.01");

            assertThat(big.isGreaterThan(small)).isTrue();
            assertThat(small.isLessThan(big)).isTrue();
            assertThat(small.isGreaterThan(small)).isFalse();
            assertThat(small.compareTo(IndianRupee.of("10"))).isZero();
        }

        @Test
        void sortsNaturally() {
            List<IndianRupee> amounts = new ArrayList<>(List.of(
                    IndianRupee.of(30), IndianRupee.of(10), IndianRupee.of(20)));

            amounts.sort(null);   // null = use compareTo (natural order)

            assertThat(amounts).containsExactly(IndianRupee.of(10), IndianRupee.of(20), IndianRupee.of(30));
        }
    }

    @Nested
    class Formatting {

        @Test
        void formatsWithRupeeSymbolAndThousandSeparators() {
            assertThat(IndianRupee.of("1179.99").format()).isEqualTo("₹1,179.99");
            assertThat(IndianRupee.of(1_234_567).format()).isEqualTo("₹1,234,567.00");
            assertThat(IndianRupee.ZERO.format()).isEqualTo("₹0.00");
        }
    }
}
