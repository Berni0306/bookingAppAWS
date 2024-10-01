package com.api.bookingapp.adapter;

import com.api.bookingapp.model.CalendarEvent;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class MetaWhatsAppMessageAdapter implements WhatsAppMessageAdapter {
    private final String API_URL = "";
    private final String ACCESS_TOKEN = "";
    private final String SEPARATOR_SUMMARY_EVENT = ":";
    private final String DOCTOR_NAME = "";
    private final String DOCTOR_CONTACT = "";
    @Override
    public void sendConfirmationMessage(CalendarEvent calendarEvent) {
        String[] eventData = calendarEvent.getSummary().split(SEPARATOR_SUMMARY_EVENT);
        String[] dateData = calendarEvent.getStart().split("T");
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpPost post = new HttpPost(API_URL);

            post.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            post.setHeader("Content-Type", "application/json");

            String json = "{"
                    + "\"messaging_product\": \"whatsapp\","
                    + "\"to\": \"52" + eventData[1] + "\","
                    + "\"type\": \"text\","
                    + "\"text\": {"
                    + "\"body\": \"Hola " + eventData[0].trim() + ", tu cita esta confirmada con la " + DOCTOR_NAME + " para el dia "
                    + dateData[0] + " a las " + dateData[1].substring(0, 5) + "hrs.\""
                    + "}"
                    + "}";

            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Response: " + response.getCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sendReminderMessage(CalendarEvent calendarEvent) {
        String[] eventData = calendarEvent.getSummary().split(SEPARATOR_SUMMARY_EVENT);
        String[] dateData = calendarEvent.getStart().split("T");
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpPost post = new HttpPost(API_URL);

            post.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
            post.setHeader("Content-Type", "application/json");

            String json = "{"
                    + "\"messaging_product\": \"whatsapp\","
                    + "\"to\": \"52" + eventData[1] + "\","
                    + "\"type\": \"text\","
                    + "\"text\": {"
                    + "\"body\": \"Hola " + eventData[0].trim() + ", recuerda que el dia de mañana "
                    + dateData[0] + " a las " + dateData[1].substring(0, 5) + "hrs tienes una cita programada.\""
                    + "}"
                    + "}";

            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Response: " + response.getCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}