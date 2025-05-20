package org.mediplus.controller;

import org.mediplus.model.Doctor;
import org.mediplus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final UserService userService;

    public DoctorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Doctor doctor = (Doctor) userService.findByUsername(principal.getName());
        model.addAttribute("doctor", doctor);
        return "doctor/dashboard";
    }

//    @GetMapping("/dashboard")
//    public String dashboard(Model model, Principal principal) {
//        model.addAttribute("doctorName", principal != null ? principal.getName() : "Doctor");
//        return "doctor/dashboard";
//    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        return "doctor/schedule";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        return "doctor/appointments";
    }

    @GetMapping("/prescription")
    public String prescription(Model model) {
        return "doctor/prescription";
    }
}