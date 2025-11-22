package com.example.smartclinic.service;

import com.example.smartclinic.model.Appointment;
import com.example.smartclinic.repository.AppointmentRepository;
import com.example.smartclinic.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment bookAppointment(Appointment appointment) {
        // (Optional) Additional validations here
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsForDoctorOnDate(Long doctorId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23,59,59);
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
    }
}
