package org.mediplus.patient;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.mediplus.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@Validated
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    public PatientController(PatientService patientService,
                             UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody PatientRequestDTO dto) {
        log.info("Registering new user: {}", dto.getUsername());
        try {
            patientService.createPatient(fromDTO(dto));
            log.info("User registered successfully: {}", dto.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(409).body("Username or email already taken");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<PatientResponseDTO> getCurrentPatient(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        Patient p = (Patient) userService.getUserByUsername(principal.getName());
        if (p == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(toDTO(p));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> listAllPatients() {
        List<PatientResponseDTO> dtos = patientService.findAllPatients()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO dto) {
        Patient created = patientService.createPatient(fromDTO(dto));
        return ResponseEntity.status(201).body(toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDTO dto) {

        Patient toUpdate = fromDTO(dto);
        toUpdate.setId(id);
        Patient updated = patientService.updatePatient(toUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    // Helper functions for converting between DTO and Patient entity
    private Patient fromDTO(PatientRequestDTO dto) {
        Patient p = new Patient(
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getDateOfBirth(),
                dto.getInsuranceId()
        );
        p.setTermsAccepted(dto.getTermsAccepted());
        return p;
    }

    private PatientResponseDTO toDTO(Patient p) {
        return new PatientResponseDTO(
                p.getId(),
                p.getUsername(),
                p.getEmail(),
                p.getInsuranceId(),
                p.getRole()
        );
    }
}
