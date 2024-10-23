package com.api.bookingapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class BookingApp {
	// silence console logging
	@Value("${logging.level.root:OFF}")
	String message = "";
	public static void main(String[] args) {
		SpringApplication.run(BookingApp.class, args);
	}
}