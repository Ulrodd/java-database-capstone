package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        // Récupérer tous les créneaux, filtrer ceux réservés
        // Exemple simplifié : supposons des créneaux fixes 9h-17h chaque heure
        List<String> allSlots = List.of("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");
        var appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        Set<String> bookedSlots = appointments.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toSet());
        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            return -1; // déjà existant
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        if (!doctorRepository.existsById(doctor.getId())) {
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(long id) {
        if (!doctorRepository.existsById(id)) {
            return -1;
        }
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(login.getEmail());
        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(401).body(response);
        }
        String token = tokenService.generateToken(login.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> result = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        result.put("doctors", doctors);
        return result;
    }

    public Map<String, Object> filterDoctorsByNameSpecialityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> filtered = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        filtered = filterDoctorByTime(filtered, amOrPm);
        return Map.of("doctors", filtered);
    }

    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> filtered = doctorRepository.findByNameContainingIgnoreCase(name);
        filtered = filterDoctorByTime(filtered, amOrPm);
        return Map.of("doctors", filtered);
    }

    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        List<Doctor> filtered = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return Map.of("doctors", filtered);
    }

    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> filtered = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        filtered = filterDoctorByTime(filtered, amOrPm);
        return Map.of("doctors", filtered);
    }

    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        List<Doctor> filtered = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return Map.of("doctors", filtered);
    }

    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        List<Doctor> allDoctors = doctorRepository.findAll();
        List<Doctor> filtered = filterDoctorByTime(allDoctors, amOrPm);
        return Map.of("doctors", filtered);
    }

    // Méthode privée pour filtrer selon AM/PM
    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        return doctors.stream()
                .filter(d -> {
                    if ("AM".equalsIgnoreCase(amOrPm)) {
                        return d.getAvailableTimes().stream()
                                .anyMatch(slot -> slot.compareTo("12:00") < 0);
                    } else if ("PM".equalsIgnoreCase(amOrPm)) {
                        return d.getAvailableTimes().stream()
                                .anyMatch(slot -> slot.compareTo("12:00") >= 0);
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
