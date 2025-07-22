package com.example.controller;

import com.example.model.Login;
import com.example.model.Patient;
import com.example.service.PatientService;
import com.example.service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    @Autowired
    public PatientController(PatientService patientService, Service service) {
        this.patientService = patientService;
        this.service = service;
    }

    @PostMapping("/validate")
    public boolean validatePatient(@RequestBody Patient patient) {
        return service.validatePatient(patient);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> patientLogin(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterPatient(
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String name,
            @RequestHeader("Authorization") String token) {
        return service.filterPatient(condition, name, token.replace("Bearer ", ""));
    }
}
