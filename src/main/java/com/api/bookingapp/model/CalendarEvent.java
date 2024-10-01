package com.api.bookingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEvent {
    private String summary;
    private String start;
    private String end;
}