package org.mediplus.controller;

import org.mediplus.model.*;
import org.mediplus.service.AppointmentService;
import org.mediplus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/patient")
public class PatientController {
    private final UserService userService;
    private final AppointmentService apptService;

    public PatientController(UserService userService, AppointmentService apptService) {
        this.userService = userService;
        this.apptService = apptService;
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

    @ModelAttribute("doctors")
    public List<Doctor> allDoctors() {
        return userService.findAllDoctors();
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
        userService.updatePatient(patient);
        return "redirect:/patient/profile?updated";
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model, Principal principal) {
        List<Appointment> patientAppointments = apptService.findAll().stream()
                .filter(a -> a.getPatientUsername().equals(principal.getName()))
                .collect(Collectors.toList());
        model.addAttribute("appointments", patientAppointments);
        return "patient/appointments";
    }

    @GetMapping("/add-new-appointment")
    public String showAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", userService.findAllDoctors());
        return "patient/add-new-appointment";
    }

    @PostMapping("/add-new-appointment")
    public String addAppointment(
            @Valid @ModelAttribute("appointment") Appointment appointment,
            BindingResult result,
            Principal principal,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("doctors", userService.findAllDoctors());
            return "patient/add-new-appointment";
        }
        String patientUsername = currentPatient(principal).getUsername();
        appointment.setPatientUsername(patientUsername);
        appointment.setStatus("PENDING");
        int nextId = apptService.findAll().stream()
                .mapToInt(Appointment::getId)
                .max()
                .orElse(0) + 1;
        appointment.setId(nextId);
        apptService.add(appointment);
        return "redirect:/patient/appointments?success";
    }

    @GetMapping("/edit-appointment/{id}")
    public String showFormForEdit(@PathVariable("id") Integer id, Model model) {
        Optional<Appointment> appointmentOpt = apptService.findById(id);
        if (appointmentOpt.isPresent()) {
            model.addAttribute("appointment", appointmentOpt.get());
            model.addAttribute("doctors", userService.findAllDoctors());
            return "patient/edit-appointment";
        }
        return "redirect:/patient/appointments";
    }

    @PostMapping("/edit-appointment")
    public String EditAppointment(@ModelAttribute("appointment") Appointment appointment) {
        apptService.saveAppointment(appointment);
        return "redirect:/patient/appointments";
    }

    @PostMapping("/delete-appointment/{id}")
    public String deleteAppointment(@PathVariable("id") Integer id) {
        apptService.deleteById(id);
        return "redirect:/patient/appointments";
    }
}