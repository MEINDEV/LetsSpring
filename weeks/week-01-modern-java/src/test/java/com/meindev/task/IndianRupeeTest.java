package com.meindev.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class IndianRupeeTest {
    @Nested
    class Creation{
        @Test
        public void creationOfIndianRupeeFromPaise(){
            var rupee = IndianRupee.ofPaise(100_00);
            assertThat(rupee).isEqualByComparingTo(IndianRupee.of(100));
        }

        @Test
        public void ofPaiseVsOf(){
            var rupee = IndianRupee.ofPaise(15);
            assertThat(rupee).isEqualByComparingTo(IndianRupee.of("0.15"));
        }
    }
}
