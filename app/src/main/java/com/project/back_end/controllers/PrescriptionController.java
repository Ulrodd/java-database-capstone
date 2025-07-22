package com.example.controller;

import com.example.model.Prescription;
import com.example.service.PrescriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> savePrescription(@RequestBody Prescription prescription) {
        return prescriptionService.savePrescription(prescription);
    }

    @GetMapping("/get/{appointmentId}")
    public ResponseEntity<Map<String, Object>> getPrescription(@PathVariable Long appointmentId) {
        return prescriptionService.getPrescription(appointmentId);
    }
}
