package com.meindev.exercises;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TagFormatterTest {
    @Test
    public void tagFormatValidation(){
        List<String> tags = List.of("java","spring");
        String mode = "UPPER";
        assertThat(TagFormatter.format(tags,"UPPER")).isEqualTo("JAVA, SPRING");
    }
}
