package com.meindev.exercises;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.assertj.core.api.SoftAssertions;

import org.junit.jupiter.api.Test;

public class SizeTest {

    @Test
    public void sizesInML(){
        SoftAssertions.assertSoftly(
                softly -> {
                    softly.assertThat(Size.SMALL.ml()).isEqualTo(250);
                    softly.assertThat(Size.MEDIUM.ml()).isEqualTo(350);
                    softly.assertThat(Size.LARGE.ml()).isEqualTo(500);
                }
        );
    }


    @Test
    public void findSizeByValidCode(){
        SoftAssertions.assertSoftly(softly -> {
           softly.assertThat(Size.fromCode("m")).isEqualTo(Size.MEDIUM);
           softly.assertThat(Size.fromCode("s")).isEqualTo(Size.SMALL);
           softly.assertThat(Size.fromCode("L")).isEqualTo(Size.LARGE);
        });
    }

    @Test
    public void findSizeByInvalidCodeThrowIllegalArgException(){
        String sizeToFind = "XL";
        assertThatThrownBy(() -> Size.fromCode(sizeToFind))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown Size " + sizeToFind);
    }

    @Test
    public void smallSizeNextIsMedium(){
        assertThat(Size.SMALL.next()).isEqualTo(Size.MEDIUM);
    }

    @Test
    public void mediumSizeNextIsLarge(){
        assertThat(Size.MEDIUM.next()).isEqualTo(Size.LARGE);
    }

    @Test
    public void LargetSizeIsMaxSize(){
        assertThat(Size.LARGE.next()).isEqualTo(Size.LARGE);
    }
}
