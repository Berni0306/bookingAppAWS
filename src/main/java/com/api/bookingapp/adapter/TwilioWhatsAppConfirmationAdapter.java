package com.api.bookingapp.adapter;

import com.api.bookingapp.model.CalendarEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Component;
@Component
public class TwilioWhatsAppConfirmationAdapter implements WhatsAppMessageAdapter {
    private final String ACCOUNT_SID = "AC584f94bbbc15f681e551dbd20007d4d7";
    private final String AUTH_TOKEN = "526db329268c9f7924931f1e60c67f34";
    private final String TWILIO_WHATSAPP_NUMBER = "whatsapp:+14155238886"; // Twilio sandbox number for WhatsApp
    private final String DOCTOR_WHATSAPP_NUMBER = "whatsapp:+5214171049568";
    private final String SEPARATOR_SUMMARY_EVANT = ":";
    public TwilioWhatsAppConfirmationAdapter() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    @Override
    public void sendConfirmationMessage(CalendarEvent calendarEvent) {
        String[] eventData = calendarEvent.getSummary().split(SEPARATOR_SUMMARY_EVANT);
        String[] dateData = calendarEvent.getStart().split("T");
        Message.creator(
                new PhoneNumber(DOCTOR_WHATSAPP_NUMBER),  // Receptor
                new PhoneNumber(TWILIO_WHATSAPP_NUMBER),
                "Hola Doctor, tiene una nueva cita con " + eventData[0] + "el dia " + dateData[0]
                        + " a las " + dateData[1].substring(0, 5) + "hrs." // Mensaje
        ).create();
        //Message.creator(
        //        new PhoneNumber("whatsapp:+521"+eventData[1]),  // Receptor
        //        new PhoneNumber(TWILIO_WHATSAPP_NUMBER),
        //        "Hola " + eventData[0] + ", tu cita está confirmada para el dia " + dateData[0]
        //              + " a las " + dateData[1].substring(0, 5) + "hrs." // Mensaje
        //).create();
    }

    @Override
    public void sendReminderMessage(CalendarEvent calendarEvent) {

    }
}