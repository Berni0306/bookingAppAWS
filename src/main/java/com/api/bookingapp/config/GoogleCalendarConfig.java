package com.api.bookingapp.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.json.gson.GsonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GoogleCalendarConfig {
    private static final String APPLICATION_NAME = "bookingappjava";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private final AmazonS3 s3client;

    public GoogleCalendarConfig(AmazonS3 s3client) {
        this.s3client = s3client;
    }
    @Bean
    public Calendar getCalendarService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        S3Object s3Object = s3client.getObject("my-sam-bookingapp", "credentials.json");
        InputStream credentialsStream = s3Object.getObjectContent();
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(SCOPES);

        return new Calendar.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}