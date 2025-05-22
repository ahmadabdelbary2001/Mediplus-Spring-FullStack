package org.mediplus.controller;

import org.mediplus.model.Doctor;
import org.mediplus.model.Patient;
import org.mediplus.model.User;
import org.mediplus.service.AppointmentService;
import org.mediplus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final UserService userService;
    private final AppointmentService apptService;

    public DoctorController(UserService userService, AppointmentService apptService) {
        this.userService = userService;
        this.apptService = apptService;
    }

    @ModelAttribute("doctors")
    public Doctor currentDoctor(Principal principal) {
        User u = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return (Doctor) u;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User u = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("doctor", (Doctor) u);
        return "doctor/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        Doctor doctor = (Doctor) userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("doctor", doctor);
        return "doctor/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(Model model, Principal principal) {
        Doctor doctor = (Doctor) userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("doctor", doctor);
        return "doctor/profile/edit";
    }

    @PostMapping("/profile/edit")
    public String editProfileSubmit(@ModelAttribute("doctor") Doctor doctor) {
        userService.updateDoctor(doctor);
        return "redirect:/doctor/profile?updated";
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