package com.meindev.exercises;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class NotificationTest {

    @Test
    public void sendEmailNotification(){
        String emailBody = Notification.describe(new Notification.Email("a@x.com","Hi"));
        assertThat(emailBody).isEqualTo("Email to a@x.com: Hi");
    }

    @Test
    public void sendPushNotitication(){
        String deviceId = "dev-1";
        String title = "Sale!";

        String notificationMessage = Notification.describe(new Notification.Push(deviceId,title));
        assertThat(notificationMessage).isEqualTo("Push to dev-1: Sale!");
    }

    @Test
    public void sendSmsNotificationOf160Character(){
        String phone = "+91999";
        String message = "a".repeat(160);

        assertThat(Notification.describe(new Notification.Sms(phone,message)))
                .isEqualTo("SMS to +91999: "+message);
    }

    @Test
    public void sendSmsNotificationOf161Character(){
        String phone = "+91999";
        String message = "a".repeat(161);

        String notificationMessage = Notification.describe(new Notification.Sms(phone,message));
        assertThat(notificationMessage).isEqualTo("Long SMS (2 parts) to "+phone);
    }
}
