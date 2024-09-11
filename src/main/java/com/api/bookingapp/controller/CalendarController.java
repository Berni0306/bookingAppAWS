package com.api.bookingapp.controller;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.Event;
import com.api.bookingapp.model.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CalendarController {
    private final List<String> availableSlots = List.of("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00");
    private final int OPENING_TIME = 10; //10am
    private final int CLOSING_TIME = 18; //6pm
    private final int APPOINTMENT_DURATION = 1; //one hour per appointment
    private final String TIME_ZONE = "America/Mexico_City";

    @Autowired
    private Calendar calendarService;

    @GetMapping("/events")
    public List<CalendarEvent> getEventsForDay(@RequestParam String date) throws IOException {
        // Parse the date string (format: YYYY-MM-DD) to a LocalDate
        LocalDate localDate = LocalDate.parse(date);

        // Convert LocalDate to the start and end of the day in ISO format
        ZonedDateTime startOfDay = localDate.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = localDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault());

        // Convert ZonedDateTime to DateTime (used by Google API)
        DateTime startDateTime = new DateTime(Date.from(startOfDay.toInstant()));
        DateTime endDateTime = new DateTime(Date.from(endOfDay.toInstant()));

        // Fetch events from Google Calendar for the specified day
        Events events = calendarService.events().list("primary")
                .setTimeMin(startDateTime)  // Set the start time filter
                .setTimeMax(endDateTime)    // Set the end time filter
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        // Map Google events to custom Event objects
        return events.getItems().stream().map(event -> {
            String summary = event.getSummary();
            String start = event.getStart().getDateTime().toStringRfc3339();
            String end = event.getEnd().getDateTime().toStringRfc3339();
            return new CalendarEvent(summary, start, end);
        }).collect(Collectors.toList());
    }

    @GetMapping("/available-time")
    public List<String> getAvailableTime(@RequestParam String date) throws IOException {
        // Parse the date and define the time range for the day (10:00 AM to 6:00 PM)
        LocalDate localDate = LocalDate.parse(date);
        ZonedDateTime startOfDay = localDate.atTime(OPENING_TIME, 0).atZone(ZoneId.systemDefault());
        ZonedDateTime endOfDay = localDate.atTime(CLOSING_TIME, 0).atZone(ZoneId.systemDefault());

        // Convert ZonedDateTime to DateTime (used by Google API)
        DateTime googleStartOfDay = new DateTime(Date.from(startOfDay.toInstant()));
        DateTime googleEndOfDay = new DateTime(Date.from(endOfDay.toInstant()));

        // Fetch events from Google Calendar for the specified day
        Events events = calendarService.events().list("primary")
                .setTimeMin(googleStartOfDay)  // Set the start time filter (10:00 AM)
                .setTimeMax(googleEndOfDay)    // Set the end time filter (6:00 PM)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        // Filter out the slots that are occupied by events
        List<CalendarEvent> googleCalendarEvents = events.getItems().stream()
                .map(event -> {
                    String eventStart = event.getStart().getDateTime().toStringRfc3339();
                    String eventEnd = event.getEnd().getDateTime().toStringRfc3339();
                    return new CalendarEvent(event.getSummary(), eventStart, eventEnd);
                }).toList();

        // Remove slots that conflict with existing events and return
        return availableSlots.stream()
                .filter(slot -> {
                    LocalTime slotTime = LocalTime.parse(slot);
                    return googleCalendarEvents.stream().noneMatch(event -> {
                        LocalTime eventStart = LocalTime.parse(event.getStart().substring(11, 16)); //Substring to erase timeZone
                        LocalTime eventEnd = LocalTime.parse(event.getEnd().substring(11, 16)); //Substring to erase timeZone
                        return slotTime.isBefore(eventEnd) && slotTime.plusHours(APPOINTMENT_DURATION).isAfter(eventStart);
                    });}).toList();
    }

    @PostMapping("/create-event")
    public ResponseEntity<Map<String, String>> createEvent(@RequestBody CalendarEvent event) throws IOException {
        Event googleEvent = new Event()
                .setSummary(event.getSummary());

        DateTime startDateTime = new DateTime(event.getStart());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(TIME_ZONE);
        googleEvent.setStart(start);

        DateTime endDateTime = new DateTime(event.getEnd());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(TIME_ZONE);
        googleEvent.setEnd(end);

        googleEvent = calendarService.events().insert("primary", googleEvent).execute();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Event created successfully");
        return ResponseEntity.ok(response);
    }
}