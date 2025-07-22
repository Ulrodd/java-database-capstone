package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.project.back_end.service.SharedService; // exemple, adapte selon ton service réel

@Controller
public class DashboardController {

    @Autowired
    private SharedService sharedService;  // Service commun pour la validation du token

    @GetMapping("/adminDashboard/{token}")
    public ModelAndView adminDashboard(@PathVariable("token") String token) {
        // Valide le token pour le rôle admin
        String error = sharedService.validateToken(token, "admin");
        if (error == null) {
            // Token valide, affiche la vue admin/adminDashboard.html
            return new ModelAndView("admin/adminDashboard");
        } else {
            // Token invalide, redirige vers la racine (login/home)
            return new ModelAndView("redirect:/");
        }
    }

    @GetMapping("/doctorDashboard/{token}")
    public ModelAndView doctorDashboard(@PathVariable("token") String token) {
        // Valide le token pour le rôle doctor
        String error = sharedService.validateToken(token, "doctor");
        if (error == null) {
            // Token valide, affiche la vue doctor/doctorDashboard.html
            return new ModelAndView("doctor/doctorDashboard");
        } else {
            // Token invalide, redirige vers la racine (login/home)
            return new ModelAndView("redirect:/");
        }
    }
}
