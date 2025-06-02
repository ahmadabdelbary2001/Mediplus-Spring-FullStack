package org.mediplus.controller;

import lombok.extern.slf4j.Slf4j;
import org.mediplus.model.Patient;
import org.mediplus.model.User;
import org.mediplus.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PatientController {

    private final UserService userService;

    public PatientController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<Patient> getCurrentPatient(Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().build();
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Patient p = (Patient) u;
            log.debug("Successfully retrieved patient: {}", p);
            return ResponseEntity.ok(p);
        } catch (ClassCastException ex) {
            // Return 500 if u is not a Patient
            return ResponseEntity.status(500).build();
        }
    }
}
