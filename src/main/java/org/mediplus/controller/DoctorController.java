package org.mediplus.controller;

import org.mediplus.model.Doctor;
import org.mediplus.service.AppointmentService;
import org.mediplus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final UserService userService;
    private final AppointmentService apptService;

    public DoctorController(UserService userService, AppointmentService apptService) {
        this.userService = userService;
        this.apptService = apptService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Doctor doctor = (Doctor) userService.findByUsername(principal.getName());
        model.addAttribute("doctor", doctor);
        return "doctor/dashboard";
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        return "doctor/schedule";
    }

    @GetMapping("/prescription")
    public String prescription(Model model) {
        return "doctor/prescription";
    }

    @GetMapping("/appointments")
    public String appointments(Model model, Principal principal) {
        String docUsername = principal.getName();
        model.addAttribute("requests",
                apptService.findByDoctorUsername(docUsername));
        return "doctor/appointments";
    }

    @PostMapping("/appointments/confirm/{id}")
    public String confirm(@PathVariable int id) {
        apptService.updateStatus(id, "CONFIRMED");
        return "redirect:/doctor/appointments";
    }

    @PostMapping("/appointments/cancel/{id}")
    public String cancel(@PathVariable int id) {
        apptService.updateStatus(id, "CANCELLED");
        return "redirect:/doctor/appointments";
    }
}