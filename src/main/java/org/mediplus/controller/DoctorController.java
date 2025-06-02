package org.mediplus.controller;

import org.mediplus.model.Doctor;
import org.mediplus.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class DoctorController {

    private final UserService userService;

    public DoctorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Doctor> listAllDoctors() {
        return userService.findAllDoctors();
    }
}