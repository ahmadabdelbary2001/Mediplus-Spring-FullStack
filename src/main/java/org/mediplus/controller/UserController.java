package org.mediplus.controller;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.mediplus.model.Doctor;
import org.mediplus.model.Patient;
import org.mediplus.model.User;
import org.mediplus.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody Patient patient) {
        log.info("Registering new user: {}", patient.getUsername());
        try {
            userService.createPatient(patient);
            log.info("User registered successfully: {}", patient.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (DataIntegrityViolationException e) {
            log.error("Registration failed - duplicate entry: {}", patient.getUsername());
            return ResponseEntity.status(409).body("Username or email already taken");
        } catch (Exception e) {
            log.error("Unexpected registration error: {}", e.getMessage());
            return ResponseEntity.status(500).body("Registration failed");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        log.debug("Login attempt for user: {}", username);

        User existingUser = userService.getUserByUsername(username);
        if (existingUser == null || !userService.authenticate(
                new Patient(username, existingUser.getEmail(), password, existingUser instanceof Patient ? ((Patient) existingUser).getDateOfBirth() : null,
                        existingUser instanceof Patient ? ((Patient) existingUser).getInsuranceId() : null)
        )) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }

        return ResponseEntity.ok(String.valueOf(existingUser.getId()));
    }


    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User u = userService.getUserByUsername(username);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    @PostMapping("/patients")
    public ResponseEntity<?> createPatient(@Valid @RequestBody Patient p) {
        try {
            Patient created = userService.createPatient(p);
            URI uri = URI.create("/api/users/" + created.getUsername());
            return ResponseEntity.created(uri).body(created);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("Username or email already taken");
        }
    }

    @PostMapping("/doctors")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody Doctor d) {
        try {
            Doctor created = userService.createDoctor(d);
            URI uri = URI.create("/api/users/" + created.getUsername());
            return ResponseEntity.created(uri).body(created);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("Username or email already taken");
        }
    }

    @PutMapping("/patients/{username}")
    public ResponseEntity<?> updatePatient(
            @PathVariable String username,
            @Valid @RequestBody Patient p) {

        User existing = userService.getUserByUsername(username);
        if (existing == null || !(existing instanceof Patient)) {
            return ResponseEntity.notFound().build();
        }

        p.setId(existing.getId());
        p.setUsername(username);
        Patient updated = userService.updatePatient(p);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/doctors/{username}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable String username,
            @Valid @RequestBody Doctor d) {

        User existing = userService.getUserByUsername(username);
        if (existing == null || !(existing instanceof Doctor)) {
            return ResponseEntity.notFound().build();
        }

        d.setId(existing.getId());
        d.setUsername(username);
        Doctor updated = userService.updateDoctor(d);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.warn("Deleting user with ID: {}", id);
        User existing = userService.getUserById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "role", u.getRole(),
                "email", u.getEmail()
        ));
    }
}