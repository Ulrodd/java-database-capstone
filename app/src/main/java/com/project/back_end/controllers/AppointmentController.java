package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.SharedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SharedService service;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, SharedService service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    @PostMapping("/validate")
    public ResponseEntity<Integer> validateAppointment(@RequestBody Appointment appointment) {
        int result = service.validateAppointment(appointment);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveAppointment(@RequestBody Appointment appointment) {
        return appointmentService.saveAppointment(appointment);
    }
}
