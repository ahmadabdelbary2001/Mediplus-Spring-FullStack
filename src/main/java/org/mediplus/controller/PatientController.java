package org.mediplus.controller;

import org.mediplus.model.Patient;
import org.mediplus.service.UserService;
import org.mediplus.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final UserService userService;

    public PatientController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        return "patient/dashboard";   // templates/patient/dashboard.html
    }

    @ModelAttribute("patient")
    public Patient currentPatient(Principal principal) {
        User u = userService.findByUsername(principal.getName());
        return (Patient) u;
    }


    @GetMapping("/profile")
    public String profile() {
        return "patient/profile";     // templates/patient/profile.html
    }

    @GetMapping("/profile/edit")
    public String editProfileForm() {
        return "patient/profile/edit";
    }

    @PostMapping("/profile/edit")
    public String editProfileSubmit(@ModelAttribute("patient") Patient patient) {
        // Update in-memory store (add this method to UserService)
        userService.updatePatient(patient);
        return "redirect:/patient/profile?updated";
    }

    @GetMapping("/appointments")
    public String listAppointments() {
        return "patient/appointments"; // templates/patient/appointments.html
    }
}