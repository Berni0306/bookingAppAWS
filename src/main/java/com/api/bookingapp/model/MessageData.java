package com.api.bookingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
    private String doctorName;
    private String patientName;
    private String doctorContact;
    private String patientContact;
    private String appointmentDate;
    private String appointmentTime;
}
