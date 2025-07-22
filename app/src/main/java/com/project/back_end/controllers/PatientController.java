package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.SharedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final SharedService service;

    @Autowired
    public PatientController(PatientService patientService, SharedService service) {
        this.patientService = patientService;
        this.service = service;
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validatePatient(@RequestBody Patient patient) {
        boolean isValid = service.validatePatient(patient);
        if (isValid) {
            return ResponseEntity.ok(Map.of("message", "Patient valide pour création"));
        } else {
            return ResponseEntity.status(409).body(Map.of("message", "Patient existe déjà"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> patientLogin(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterPatient(
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String name,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("message", "Authorization header missing or invalid"));
        }
        
        String token = authorizationHeader.substring(7); // retire "Bearer "
        return service.filterPatient(condition, name, token);
    }
}
