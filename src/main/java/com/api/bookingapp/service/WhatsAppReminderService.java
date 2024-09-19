package com.api.bookingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class WhatsAppReminderService {
    @Autowired
    private CalendarService calendarService;
    //Execute every day at 8am
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyReminders() throws IOException {
        calendarService.sendRemainderMessage();
    }
}
