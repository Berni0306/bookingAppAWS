package com.api.bookingapp.adapter;

import com.api.bookingapp.config.TwilioWhatsAppConfig;
import com.api.bookingapp.model.CalendarEvent;
import com.api.bookingapp.model.MessageData;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class TwilioWhatsAppMessageAdapter implements WhatsAppMessageAdapter{
    private final String DOCTOR_NAME = "Dra. Fernanda Romero";
    private final String DOCTOR_CONTACT = "4171049568";
    private final String DOCTOR_CONFIRMATION = "Hola %s, tiene una nueva cita con %s el día %s a las %s hrs.";
    private final String PATIENT_CONFIRMATION = "Hola %s, tu cita esta confirmada para el día %s a las %s hrs con la %s.";
    private final String PATIENT_REMINDER = "Hola %s, recuerda que el dia de mañana %s a las %s hrs tienes una cita programada.";

    @Autowired
    private TwilioWhatsAppConfig twilioWhatsAppConfig;
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
        String appointmentDate = timeParts[0];
        String appointmentTime = timeParts[1].substring(0, 5);
    return new MessageData(DOCTOR_NAME, patientName, DOCTOR_CONTACT,
            patientContact, appointmentDate, appointmentTime);
    }
    private void sendMessage(String toPhone, String message) {
        Message.creator(
                new PhoneNumber("whatsapp:+521" + toPhone), // Prefijo MEXICO "whatsapp:+52"
                new PhoneNumber(twilioWhatsAppConfig.getTwilioPhoneNumber()),
                message
        ).create();
    }
}
