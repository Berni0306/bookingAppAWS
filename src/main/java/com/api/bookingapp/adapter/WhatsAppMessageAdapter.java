package com.api.bookingapp.adapter;

import com.api.bookingapp.model.CalendarEvent;
public interface WhatsAppMessageAdapter {
    void sendConfirmationMessage(CalendarEvent calendarEvent);
    void sendReminderMessage(CalendarEvent calendarEvent);
}
