package com.api.bookingapp.adapter;

import com.api.bookingapp.model.CalendarEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface CalendarAdapter {
    List<CalendarEvent> getEventsForDay(LocalDate date) throws IOException;
    List<String> getAvailableTimeSlots(LocalDate date) throws IOException;
    void createEvent(CalendarEvent event, String timeZone) throws IOException;
    List<CalendarEvent> getEventsForTomorrow() throws IOException;
}
