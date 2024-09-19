package com.api.bookingapp.config;

import com.twilio.Twilio;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class TwilioWhatsAppConfig {
    private String accountSid = "AC584f94bbbc15f681e551dbd20007d4d7";
    private String authToken = "526db329268c9f7924931f1e60c67f34";
    @Getter
    private String twilioPhoneNumber = "whatsapp:+14155238886";
    public TwilioWhatsAppConfig() {
        Twilio.init(accountSid, authToken);
    }
}