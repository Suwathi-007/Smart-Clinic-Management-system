package com.example.smartclinic.service;

import com.example.smartclinic.model.Doctor;
import com.example.smartclinic.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Return available time slots for doctor on a date
    public List<String> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        Optional<Doctor> opt = doctorRepository.findById(doctorId);
        if (opt.isEmpty()) throw new RuntimeException("Doctor not found");

        Doctor doctor = opt.get();
        // For demo: decode availableTimes JSON and return list
        String json = doctor.getAvailableTimes();
        // assumption: availableTimes = ["09:00","09:30","10:00", ...]
        // Simple parse (production: use Jackson)
        if (json == null || json.isBlank()) return List.of();
        String trimmed = json.trim();
        trimmed = trimmed.replace("[", "").replace("]", "").replace("\"", "");
        String[] slots = trimmed.split(",");
        return Arrays.stream(slots).map(String::trim).toList();
    }

    // Validate doctor login credentials (example)
    public boolean validateDoctorCredentials(String email, String password) {
        // Placeholder - implement real auth with hashed password
        Optional<Doctor> doc = doctorRepository.findByEmail(email);
        return doc.isPresent() && password != null && !password.isBlank();
    }
}
