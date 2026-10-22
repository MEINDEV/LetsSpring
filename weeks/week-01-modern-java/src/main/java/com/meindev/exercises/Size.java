package com.meindev.exercises;

import java.util.Arrays;

public enum Size {
    SMALL("s",250),
    MEDIUM("m",350),
    LARGE("l",500);

    private final String code;
    private final int size;

    Size(String code, int size){
        this.code = code;
        this.size = size;
    }

    public int ml(){
        return this.size;
    }

    public static Size fromCode(String code){
        return Arrays.stream(values())
                .filter(size->size.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("Unknown Size "+code));
    }

    public Size next(){
        return switch (this){
            case SMALL -> MEDIUM;
            case MEDIUM, LARGE -> LARGE;
        };
    }
}
