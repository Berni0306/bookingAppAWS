package com.api.bookingapp.controller;

import com.api.bookingapp.service.CalendarService;
import com.api.bookingapp.model.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CalendarController {
    private final String TIME_ZONE = "America/Mexico_City";

    @Autowired
    private CalendarService calendarService;
    @GetMapping("/events")
    public List<CalendarEvent> getEventsForDay(@RequestParam String date) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        return calendarService.getEventsForDay(localDate);
    }
    @GetMapping("/available-time")
    public List<String> getAvailableTime(@RequestParam String date) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        return calendarService.getAvailableTime(localDate);
    }
    @PostMapping("/create-event")
    public ResponseEntity<Map<String, String>> createEvent(@RequestBody CalendarEvent event) throws IOException {
        calendarService.createEvent(event, TIME_ZONE);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Event created successfully");
        return ResponseEntity.ok(response);
    }
}