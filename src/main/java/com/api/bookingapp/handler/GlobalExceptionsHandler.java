package com.api.bookingapp.handler;

import com.api.bookingapp.exceptions.CalendarServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(CalendarServiceException.class)
    public ResponseEntity<Map<String, Object>> handleCalendarServiceException(CalendarServiceException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(IOException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private <T extends Exception> ResponseEntity<Map<String, Object>> buildErrorResponse(T e, HttpStatus status) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", e.getClass().getSimpleName());
        errorDetails.put("message", e.getMessage());
        errorDetails.put("status", status.value());
        errorDetails.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorDetails, status);
    }
}
