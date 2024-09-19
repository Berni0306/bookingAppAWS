package com.api.bookingapp.service;

import com.api.bookingapp.adapter.CalendarAdapter;
import com.api.bookingapp.adapter.TwilioWhatsAppMessageAdapter;
import com.api.bookingapp.model.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@Service
public class CalendarService {

    @Autowired
    private CalendarAdapter calendarAdapter;
    @Autowired
    private TwilioWhatsAppMessageAdapter twilioWhatsAppMessageAdapter;
    public List<CalendarEvent> getEventsForDay (LocalDate localDate) throws IOException {
        return calendarAdapter.getEventsForDay(localDate);
    }
    public List<String> getAvailableTime(LocalDate localDate) throws IOException {
        return calendarAdapter.getAvailableTimeSlots(localDate);
    }
    public void createEvent(CalendarEvent calendarEvent, String timeZone) throws IOException {
        calendarAdapter.createEvent(calendarEvent, timeZone);
        twilioWhatsAppMessageAdapter.sendConfirmationMessage(calendarEvent);
    }
    public void sendRemainderMessage() throws IOException {
        List<CalendarEvent> tomorrowEvents = calendarAdapter.getEventsForTomorrow();
        tomorrowEvents.forEach(calendarEvent -> {
            twilioWhatsAppMessageAdapter.sendReminderMessage(calendarEvent);
        });
    }
}