package com.api.bookingapp.exceptions;

public class CalendarServiceException extends RuntimeException{
    public CalendarServiceException(String message) {
        super(message);
    }
    public CalendarServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
