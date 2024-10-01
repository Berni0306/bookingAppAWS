package com.api.bookingapp.controller;

import com.api.bookingapp.exceptions.CalendarServiceException;
import com.api.bookingapp.service.CalendarService;
import com.api.bookingapp.model.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
public class CalendarController {
    @Value("${calendar.time-zone}")
    private String TIME_ZONE;

    @Autowired
    private CalendarService calendarService;
    @GetMapping("/events")
    public List<CalendarEvent> getEventsForDay(@RequestParam String date){
        if (date == null || date.isEmpty()) throw new IllegalArgumentException("Date null or empty");
        try {
            LocalDate localDate = LocalDate.parse(date);
            return calendarService.getEventsForDay(localDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is YYYY-MM-DD", e);
        }
    }
    @GetMapping("/available-time")
    public List<String> getAvailableTime(@RequestParam String date) {
        if (date == null || date.isEmpty()) throw new IllegalArgumentException("Date null or empty");
        try {
            LocalDate localDate = LocalDate.parse(date);
            return calendarService.getAvailableTime(localDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is YYYY-MM-DD", e);
        }
    }
    @PostMapping("/create-event")
    public ResponseEntity<Map<String, String>> createEvent(@RequestBody CalendarEvent event) {
        if (event == null) throw new IllegalArgumentException("Event null");
        try {
            calendarService.createEvent(event, TIME_ZONE);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Event created successfully");
            return ResponseEntity.ok(response);
        } catch (CalendarServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}