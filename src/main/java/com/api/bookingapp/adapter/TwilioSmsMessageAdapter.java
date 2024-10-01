package com.api.bookingapp.adapter;

import com.api.bookingapp.config.TwilioMessageConfig;
import com.api.bookingapp.model.CalendarEvent;
import com.api.bookingapp.model.MessageData;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Component
public class TwilioSmsMessageAdapter implements SmsMessageAdapter{
    @Value("${message.doctor-name}")
    private String DOCTOR_NAME;
    @Value("${message.doctor-contact}")
    private String DOCTOR_CONTACT;
    @Value("${message.doctor-confirmation}")
    private String DOCTOR_CONFIRMATION;
    @Value("${message.patient-confirmation}")
    private String PATIENT_CONFIRMATION;
    @Value("${message.patient-reminder}")
    private String PATIENT_REMINDER;

    @Autowired
    private TwilioMessageConfig twilioMessageConfig;
    @Override
    public void sendConfirmationMessage(CalendarEvent calendarEvent) {
        MessageData messageData = getMessageData(calendarEvent);
        String messageToDoctorBody = String.format(
                DOCTOR_CONFIRMATION,
                messageData.getDoctorName(),
                messageData.getPatientName(),
                messageData.getAppointmentDate(),
                messageData.getAppointmentTime()
        );
        String messageToPatientBody = String.format(
                PATIENT_CONFIRMATION,
                messageData.getPatientName(),
                messageData.getAppointmentDate(),
                messageData.getAppointmentTime(),
                messageData.getDoctorName()
        );
        sendMessage(DOCTOR_CONTACT, messageToDoctorBody);
        sendMessage(messageData.getPatientContact(), messageToPatientBody);
    }
    @Override
    public void sendReminderMessage(CalendarEvent calendarEvent) {
        MessageData messageData = getMessageData(calendarEvent);
        String messageToPatientBody = String.format(
                PATIENT_REMINDER,
                messageData.getPatientName(),
                messageData.getAppointmentDate(),
                messageData.getAppointmentTime()
        );
        sendMessage(messageData.getPatientContact(), messageToPatientBody);
    }
    private MessageData getMessageData(CalendarEvent calendarEvent) {
        String[] summaryParts = calendarEvent.getSummary().split(":");
        String[] timeParts = calendarEvent.getStart().split("T");
        String patientName = summaryParts[0].trim();
        String patientContact = summaryParts[1].trim();
        DateTimeFormatter formatterIn = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(timeParts[0], formatterIn);
        String appointmentDate = date.format(formatterOut);
        String appointmentTime = timeParts[1].substring(0, 5);
        return new MessageData(DOCTOR_NAME, patientName, DOCTOR_CONTACT,
                patientContact, appointmentDate, appointmentTime);
    }
    private void sendMessage(String toPhone, String message) {
        Message m = Message.creator(
                new PhoneNumber("+52" + toPhone), // Prefijo MEXICO "+52"
                new PhoneNumber(twilioMessageConfig.getTwilioSmsNumber()),
                message
        ).create();
    }
}