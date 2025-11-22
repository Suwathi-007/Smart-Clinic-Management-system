package com.example.smartclinic.service;

import com.example.smartclinic.model.Doctor;
import com.example.smartclinic.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ⭐ Return available time slots for a doctor on a specific date
    public List<String> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        Optional<Doctor> opt = doctorRepository.findById(doctorId);
        if (opt.isEmpty()) {
            throw new RuntimeException("Doctor not found");
        }

        Doctor doctor = opt.get();

        String json = doctor.getAvailableTimes();
        if (json == null || json.isBlank()) return List.of();

        String trimmed = json.replace("[", "").replace("]", "").replace("\"", "");
        String[] slots = trimmed.split(",");

        return Arrays.stream(slots)
                .map(String::trim)
                .toList();
    }

    // ⭐ Improve doctor credential validation with secure password check and structured response
    public ResponseEntity<?> validateDoctorCredentials(String email, String password) {

        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

        if (doctorOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email"));
        }

        Doctor doctor = doctorOpt.get();

        // Check hashed password using BCrypt
        if (!passwordEncoder.matches(password, doctor.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid password"));
        }

        // Success response
        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful",
                        "doctorId", doctor.getId(),
                        "name", doctor.getName(),
                        "email", doctor.getEmail()
                )
        );
    }
}
