package com.example.service;

import com.example.dto.AppointmentDTO;
import com.example.model.Appointment;
import com.example.model.Patient;
import com.example.repository.AppointmentRepository;
import com.example.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> response = new HashMap<>();
        String email = tokenService.extractEmail(token);
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null || !patient.getEmail().equals(email)) {
            response.put("message", "Unauthorized or patient not found");
            return ResponseEntity.status(401).body(response);
        }
        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        List<AppointmentDTO> dtos = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
        response.put("appointments", dtos);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> appointments;
        if ("past".equalsIgnoreCase(condition)) {
            appointments = appointmentRepository.findByPatientIdAndStatus(id, 1);
        } else if ("future".equalsIgnoreCase(condition)) {
            appointments = appointmentRepository.findByPatientIdAndStatus(id, 0);
        } else {
            response.put("message", "Invalid condition");
            return ResponseEntity.badRequest().body(response);
        }
        List<AppointmentDTO> dtos = appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
        response.put("appointments", dtos);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long id) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        List<AppointmentDTO> filtered = appointments.stream()
                .filter(a -> a.getDoctor().getName().equalsIgnoreCase(name))
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
        response.put("appointments", filtered);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> filterByDate(String dateStr, Long id) {
        Map<String, Object> response = new HashMap<>();
        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        List<AppointmentDTO> filtered = appointments.stream()
                .filter(a -> a.getAppointmentTime().toLocalDate().toString().equals(dateStr))
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
        response.put("appointments", filtered);
        return ResponseEntity.ok(response);
    }
}
