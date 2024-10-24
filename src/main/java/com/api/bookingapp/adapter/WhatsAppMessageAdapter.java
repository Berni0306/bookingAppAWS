package com.api.bookingapp.adapter;

import com.api.bookingapp.model.CalendarEvent;

import java.io.IOException;

public interface WhatsAppMessageAdapter {
    void sendConfirmationMessage(CalendarEvent calendarEvent) throws IOException;
    void sendReminderMessage(CalendarEvent calendarEvent) throws IOException;
}
