package com.meindev.exercises;

public sealed interface Notification permits Notification.Email, Notification.Sms, Notification.Push {
    public record Email(String to, String subject) implements Notification {}
    public record Sms(String phone, String text) implements Notification{}
    public record Push(String deviceId, String title) implements Notification{}

    public static String describe(Notification n){
        return switch(n){
            case Email(String to, String subject) -> "Email to "+to + ": "+subject;
            case Sms(String phone, String text) -> {
                int maxChar = 160;
                int textLength = text.length();
                int parts = ( textLength+(maxChar -1 ))/maxChar;
                if(textLength <= 160){
                    yield "SMS to "+phone+": "+ text;
                }else {
                    yield "Long SMS ("+parts+" parts) to " +phone;
                }
            }
            case Push(String deviceId, String title) -> "Push to "+deviceId+": "+title;
        };
    }
}
