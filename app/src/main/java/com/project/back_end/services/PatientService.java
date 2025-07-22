package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Renvoie toutes les appointments du patient (DTO)
    public List<AppointmentDTO> getPatientAppointmentDTOs(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }

    // Renvoie toutes les appointments du patient (model Appointment)
    public List<Appointment> getAllAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // Filtre par condition "past" ou "future"
    public List<Appointment> filterByCondition(Long patientId, String condition) {
        if ("past".equalsIgnoreCase(condition)) {
            return appointmentRepository.findByPatientIdAndStatus(patientId, 1);
        } else if ("future".equalsIgnoreCase(condition)) {
            return appointmentRepository.findByPatientIdAndStatus(patientId, 0);
        }
        return Collections.emptyList();
    }

    // Filtre par nom de docteur
    public List<Appointment> filterByDoctor(Long patientId, String doctorName) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .filter(a -> a.getDoctor().getName().equalsIgnoreCase(doctorName))
                .collect(Collectors.toList());
    }

    // Filtre par date (format yyyy-MM-dd)
    public List<Appointment> filterByDate(Long patientId, String dateStr) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .filter(a -> a.getAppointmentTime().toLocalDate().toString().equals(dateStr))
                .collect(Collectors.toList());
    }

    // Filtre par docteur ET condition
    public List<Appointment> filterByDoctorAndCondition(Long patientId, String doctorName, String condition) {
        List<Appointment> filteredByCondition = filterByCondition(patientId, condition);
        return filteredByCondition.stream()
                .filter(a -> a.getDoctor().getName().equalsIgnoreCase(doctorName))
                .collect(Collectors.toList());
    }
}
