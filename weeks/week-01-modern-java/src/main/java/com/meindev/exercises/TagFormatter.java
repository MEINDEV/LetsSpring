package com.meindev.exercises;

import java.util.List;

public class TagFormatter {

    enum FormatterMode {
        UPPER,AS_IS;

        public String format(String text){
            return switch(this){
                case UPPER -> text.toUpperCase();
                case AS_IS -> text;
            };
        }
    }

    public static String format(List<String> tags, String mode){
        var result = new StringBuilder();
        for (int i=0;i<tags.size();i++) {

            String tag = tags.get(i);

//            if("UPPER".equals(mode)) {
//                tag = tag.toUpperCase();
//            }

            tag = FormatterMode.UPPER.format(tag);

            result.append(tag);

            if(i != tags.size()-1){
                result.append(", ");
            }

        }
        return result.toString();
    }


}
