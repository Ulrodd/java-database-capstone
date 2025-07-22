package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TokenService tokenService;

    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());
        if (existing.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(404).body(response);
        }
        // Supposons validateAppointment() existe dans ce service
        if (!validateAppointment(appointment)) {
            response.put("message", "Invalid appointment data");
            return ResponseEntity.badRequest().body(response);
        }
        appointmentRepository.save(appointment);
        response.put("message", "Appointment updated successfully");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();
        Optional<Appointment> existing = appointmentRepository.findById(id);
        if (existing.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.status(404).body(response);
        }
        Appointment appointment = existing.get();
        String emailFromToken = tokenService.extractEmail(token);
        if (!appointment.getPatient().getEmail().equals(emailFromToken)) {
            response.put("message", "Unauthorized cancellation attempt");
            return ResponseEntity.status(401).body(response);
        }
        appointmentRepository.delete(appointment);
        response.put("message", "Appointment cancelled successfully");
        return ResponseEntity.ok(response);
    }

    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> result = new HashMap<>();
        String doctorEmail = tokenService.extractEmail(token);
        var doctor = doctorRepository.findByEmail(doctorEmail);
        if (doctor == null) {
            result.put("message", "Doctor not found");
            return result;
        }
        LocalDate start = date.atStartOfDay().toLocalDate();
        LocalDate end = date.plusDays(1);
        var appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctor.getId(), date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        if (pname != null && !pname.isEmpty()) {
            appointments.removeIf(a -> !a.getPatient().getName().equalsIgnoreCase(pname));
        }
        result.put("appointments", appointments);
        return result;
    }

    // Méthode de validation d'exemple (à implémenter selon règles)
    private boolean validateAppointment(Appointment appointment) {
        // Exemple : vérifier que date > maintenant etc.
        return true;
    }
}
