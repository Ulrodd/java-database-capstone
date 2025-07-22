package com.example.controller;

import com.example.model.Doctor;
import com.example.service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final Service service;

    @Autowired
    public DoctorController(Service service) {
        this.service = service;
    }

    @GetMapping("/filter")
    public Map<String, Object> filterDoctors(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) String specialty,
                                             @RequestParam(required = false) String time) {
        return service.filterDoctor(name, specialty, time);
    }
}
