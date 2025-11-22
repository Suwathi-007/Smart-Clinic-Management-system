package com.example.smartclinic.controller;

import com.example.smartclinic.model.Prescription;
import com.example.smartclinic.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<?> savePrescription(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody Prescription prescription
    ) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid or missing token");
        }
        // further token validation delegated to TokenService in real app

        try {
            Prescription saved = prescriptionService.save(prescription);
            return ResponseEntity.ok().body(
                new ApiResponse(true, "Prescription saved", saved)
            );
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Error: " + ex.getMessage(), null));
        }
    }

    public static record ApiResponse(boolean success, String message, Object data) { }
}
