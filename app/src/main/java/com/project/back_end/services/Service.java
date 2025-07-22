package com.example.service;

import com.example.model.*;
import com.example.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(TokenService tokenService, AdminRepository adminRepository,
                   DoctorRepository doctorRepository, PatientRepository patientRepository,
                   DoctorService doctorService, PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // Vérifie la validité d'un token
    public ResponseEntity<Map<String, String>> validateToken(String token, String userType) {
        Map<String, String> response = new HashMap<>();
        boolean isValid = tokenService.validateToken(token, userType);
        if (!isValid) {
            response.put("message", "Token invalide ou expiré");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        response.put("message", "Token valide");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Valide un admin et génère un token
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();
        Optional<Admin> adminOpt = adminRepository.findByUsername(receivedAdmin.getUsername());
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getPassword().equals(receivedAdmin.getPassword())) {
                String token = tokenService.generateToken(admin.getUsername());
                response.put("token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        response.put("message", "Identifiants invalides");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Filtre des médecins selon nom, spécialité et temps disponible
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorService.filterDoctorsByNameSpecialtyAndTime(name, specialty, time);
        response.put("doctors", doctors);
        return response;
    }

    // Valide la disponibilité d'un rendez-vous
    public int validateAppointment(Appointment appointment) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());
        if (doctorOpt.isEmpty()) {
            return -1; // Médecin non trouvé
        }
        Doctor doctor = doctorOpt.get();
        boolean available = doctorService.getDoctorAvailability(doctor, appointment.getAppointmentTime());
        return available ? 1 : 0;
    }

    // Vérifie si le patient existe déjà
    public boolean validatePatient(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return existingPatient.isEmpty(); // true = patient n'existe pas, donc ok pour création
    }

    // Valide la connexion d'un patient
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();
        Optional<Patient> patientOpt = patientRepository.findByEmail(login.getEmail());
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            if (patient.getPassword().equals(login.getPassword())) {
                String token = tokenService.generateToken(patient.getEmail());
                response.put("token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        response.put("message", "Identifiants invalides");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Filtre les rendez-vous des patients par condition et nom médecin
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        Map<String, Object> response = new HashMap<>();
        // On extrait email du token pour identifier le patient
        String email = tokenService.extractIdentifier(token);
        if (!tokenService.validateToken(token, "patient")) {
            response.put("message", "Token invalide ou expiré");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<Appointment> filteredAppointments;

        if (condition != null && !condition.isEmpty() && name != null && !name.isEmpty()) {
            filteredAppointments = patientService.filterByDoctorAndCondition(email, name, condition);
        } else if (condition != null && !condition.isEmpty()) {
            filteredAppointments = patientService.filterByCondition(email, condition);
        } else if (name != null && !name.isEmpty()) {
            filteredAppointments = patientService.filterByDoctor(email, name);
        } else {
            filteredAppointments = patientService.getAllAppointments(email);
        }

        response.put("appointments", filteredAppointments);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
