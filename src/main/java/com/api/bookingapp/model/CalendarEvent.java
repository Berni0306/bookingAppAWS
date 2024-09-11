package com.api.bookingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEvent {
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("start")
    private String start;
    @JsonProperty("end")
    private String end;
}