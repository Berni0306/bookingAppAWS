package com.api.bookingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppReminderService {
    @Autowired
    private CalendarService calendarService;
    @Scheduled(cron = "0 0 12 * * ?") //Execute every day at 12pm
    public void sendDailyReminders() {
        calendarService.sendRemainderMessage();
    }
}