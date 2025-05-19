package org.mediplus.controller;

import org.mediplus.model.Patient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        model.addAttribute("patient", getCurrentPatient(principal));
        return "patient/dashboard";   // templates/patient/dashboard.html
    }

    @GetMapping("/profile")
    public String profile() {
        return "patient/profile";     // templates/patient/profile.html
    }

    @GetMapping("/appointments")
    public String listAppointments() {
        return "patient/appointments"; // templates/patient/appointments.html
    }

    private Patient getCurrentPatient(Principal principal) {
        // منطق جلب بيانات المريض
        return new Patient();
    }
}