package org.mediplus.controller;

import org.mediplus.model.User;
import org.mediplus.model.Patient;
import org.mediplus.service.UserService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";           // templates/auth/login.html
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "auth/register";        // templates/auth/register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("patient") Patient patient,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model) {
        try {
            if (!patient.getPassword().equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "auth/register";
            }

            patient.setRole("PATIENT");
            userService.registerUser(patient);
            model.addAttribute("success", "Registration successful! Please login.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration error: " + e.getMessage());
            return "auth/register";
        }
    }
}