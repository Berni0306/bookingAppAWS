package com.api.bookingapp.adapter;

import com.api.bookingapp.model.CalendarEvent;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class GoogleCalendarAdapter implements CalendarAdapter{
    private final int OPENING_TIME = 10; // 10am
    private final int CLOSING_TIME = 18; // 6pm
    private final int APPOINTMENT_DURATION = 2; // hours per appointment

    @Autowired
    private Calendar calendarService;
    @Override
    public List<CalendarEvent> getEventsForDay(LocalDate localDate) throws IOException {
        ZonedDateTime startOfDay = localDate.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = localDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault());

        DateTime startDateTime = new DateTime(Date.from(startOfDay.toInstant()));
        DateTime endDateTime = new DateTime(Date.from(endOfDay.toInstant()));

        Events events = calendarService.events().list("primary")
                .setTimeMin(startDateTime)
                .setTimeMax(endDateTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        return events.getItems().stream().map(event -> {
            String summary = event.getSummary();
            String start = event.getStart().getDateTime().toStringRfc3339();
            String end = event.getEnd().getDateTime().toStringRfc3339();
            return new CalendarEvent(summary, start, end);
        }).collect(Collectors.toList());
    }
    @Override
    public List<String> getAvailableTimeSlots(LocalDate localDate) throws IOException {
        ZonedDateTime startOfDay = localDate.atTime(OPENING_TIME, 0).atZone(ZoneId.systemDefault());
        ZonedDateTime endOfDay = localDate.atTime(CLOSING_TIME, 0).atZone(ZoneId.systemDefault());

        DateTime googleStartOfDay = new DateTime(Date.from(startOfDay.toInstant()));
        DateTime googleEndOfDay = new DateTime(Date.from(endOfDay.toInstant()));

        Events events = calendarService.events().list("primary")
                .setTimeMin(googleStartOfDay)
                .setTimeMax(googleEndOfDay)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<CalendarEvent> googleCalendarEvents = events.getItems().stream()
                .map(event -> {
                    String eventStart = event.getStart().getDateTime().toStringRfc3339();
                    String eventEnd = event.getEnd().getDateTime().toStringRfc3339();
                    return new CalendarEvent(event.getSummary(), eventStart, eventEnd);
                }).toList();

        return generateAvailableSlots().stream()
                .filter(slot -> {
                    LocalTime slotTime = LocalTime.parse(slot);
                    return googleCalendarEvents.stream().noneMatch(event -> {
                        LocalTime eventStart = LocalTime.parse(event.getStart().substring(11, 16));
                        LocalTime eventEnd = LocalTime.parse(event.getEnd().substring(11, 16));
                        return slotTime.isBefore(eventEnd) && slotTime.plusHours(APPOINTMENT_DURATION).isAfter(eventStart);
                    });
                }).toList();
    }
    @Override
    public void createEvent(CalendarEvent calendarEvent, String timeZone) throws IOException {
        Event googleEvent = new Event().setSummary(calendarEvent.getSummary());

        DateTime startDateTime = new DateTime(calendarEvent.getStart());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(timeZone);
        googleEvent.setStart(start);

        DateTime endDateTime = new DateTime(calendarEvent.getEnd());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(timeZone);
        googleEvent.setEnd(end);

        calendarService.events().insert("primary", googleEvent).execute();
    }
    @Override
    public List<CalendarEvent> getEventsForTomorrow() throws IOException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ZonedDateTime startOfDay = tomorrow.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = tomorrow.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault());

        DateTime startDateTime = new DateTime(Date.from(startOfDay.toInstant()));
        DateTime endDateTime = new DateTime(Date.from(endOfDay.toInstant()));

        Events events = calendarService.events().list("primary")
                .setTimeMin(startDateTime)
                .setTimeMax(endDateTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        return events.getItems().stream().map(event -> {
            String summary = event.getSummary();
            String start = event.getStart().getDateTime().toStringRfc3339();
            String end = event.getEnd().getDateTime().toStringRfc3339();
            return new CalendarEvent(summary, start, end);
        }).collect(Collectors.toList());
    }
    private List<String> generateAvailableSlots() {
        List<String> availableSlots = new ArrayList<>();

        LocalTime currentTime = LocalTime.of(OPENING_TIME, 0);
        LocalTime closingTime = LocalTime.of(CLOSING_TIME, 0);

        while (currentTime.isBefore(closingTime)) {
            availableSlots.add(currentTime.toString());
            currentTime = currentTime.plusHours(APPOINTMENT_DURATION);
        }
        return availableSlots;
    }
}