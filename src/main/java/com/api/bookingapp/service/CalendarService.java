package com.api.bookingapp.service;

import com.api.bookingapp.adapter.CalendarAdapter;
import com.api.bookingapp.adapter.SmsMessageAdapter;
import com.api.bookingapp.adapter.WhatsAppMessageAdapter;
import com.api.bookingapp.exceptions.CalendarServiceException;
import com.api.bookingapp.model.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@Service
public class CalendarService {

    @Autowired
    private CalendarAdapter calendarAdapter;
    //@Autowired
    //@Qualifier("twilioWhatsAppMessageAdapter")
    //private WhatsAppMessageAdapter whatsAppMessageAdapter;
    @Autowired
    private SmsMessageAdapter smsMessageAdapter;
    public List<CalendarEvent> getEventsForDay (LocalDate localDate) {
        try {
            return calendarAdapter.getEventsForDay(localDate);
        } catch (IOException e){
            throw new CalendarServiceException("Error fetching events from Google Calendar API", e);
        }
    }
    public List<String> getAvailableTime(LocalDate localDate) {
        try {
            return calendarAdapter.getAvailableTimeSlots(localDate);
        } catch (IOException e) {
            throw new CalendarServiceException("Error fetching available time slots", e);
        }
    }
    public void createEvent(CalendarEvent calendarEvent, String timeZone) {
        if (calendarEvent == null) throw new IllegalArgumentException("calendarEvant cannot be null");
        try {
            calendarAdapter.createEvent(calendarEvent, timeZone);
        } catch (IOException e) {
            throw new CalendarServiceException("Error creating calendar event", e);
        }
        //try {
        //    whatsAppMessageAdapter.sendConfirmationMessage(calendarEvent);
        //} catch (IOException e) {
        //    throw new CalendarServiceException("Error sending WhatsApp confirmation message", e);
        //}
        try {
            smsMessageAdapter.sendConfirmationMessage(calendarEvent);
        } catch (IOException e) {
            throw new CalendarServiceException("Error sending SMS confirmation message", e);
        }
    }
    public void sendRemainderMessage() {
        try {
            List<CalendarEvent> tomorrowEvents = calendarAdapter.getEventsForTomorrow();
            tomorrowEvents.forEach(calendarEvent -> {
                //try {
                //    whatsAppMessageAdapter.sendReminderMessage(calendarEvent);
                //} catch (IOException e) {
                //    throw new CalendarServiceException("Error sending WhatsApp reminder message", e);
                //}
                try {
                    smsMessageAdapter.sendReminderMessage(calendarEvent);
                } catch (IOException e) {
                    throw new CalendarServiceException("Error sending SMS reminder message", e);
                }
            });
        } catch (IOException e){
            throw new CalendarServiceException("Error fetching tomorrow events from Google Calendar API", e);
        }
    }
}