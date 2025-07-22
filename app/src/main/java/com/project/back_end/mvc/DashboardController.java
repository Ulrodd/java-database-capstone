package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import com.project.back_end.services.SharedService;

@Controller
public class DashboardController {

    @Autowired
    private SharedService sharedService;

    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable("token") String token) {
        if (isTokenValid(token, "admin")) {
            return new ModelAndView("admin/adminDashboard");
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/doctorDashboard/{token}")
    public ModelAndView doctorDashboard(@PathVariable("token") String token) {
        if (isTokenValid(token, "doctor")) {
            return new ModelAndView("doctor/doctorDashboard");
        }
        return new ModelAndView("redirect:/");
    }

    private boolean isTokenValid(String token, String userType) {
        ResponseEntity<?> response = sharedService.validateToken(token, userType);
        return response.getStatusCode() == HttpStatus.OK;
    }
}
