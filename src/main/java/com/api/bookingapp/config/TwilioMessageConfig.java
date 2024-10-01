package com.api.bookingapp.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class TwilioMessageConfig{
    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Getter
    @Value("${twilio.whatsapp-number}")
    private String twilioWhatsAppNumber;
    @Getter
    @Value("${twilio.sms-number}")
    private String twilioSmsNumber;
    @PostConstruct
    public void initTwilioConfig() {
        Twilio.init(accountSid, authToken);
    }
}