package org.mediplus.doctor;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> listAllDoctors() {
        List<DoctorResponseDTO> dtos = doctorService.findAllDoctors().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(@Valid @RequestBody DoctorRequestDTO dto) {
        Doctor doc = fromDTO(dto);
        Doctor created = doctorService.createDoctor(doc);
        return ResponseEntity.status(201).body(toDTO(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        Doctor d = doctorService.getDoctorById(id);
        if (d == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(d));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDTO dto) {

        Doctor doc = fromDTO(dto);
        doc.setId(id);
        Doctor updated = doctorService.updateDoctor(doc);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    // Helper functions for converting between DTO and Doctor entity
    private Doctor fromDTO(DoctorRequestDTO dto) {
        Doctor d = new Doctor(
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getSpecialization(),
                dto.getLicenseNumber(),
                dto.getClinicLocation()
        );
        d.setTermsAccepted(dto.getTermsAccepted());
        return d;
    }

    private DoctorResponseDTO toDTO(Doctor d) {
        return new DoctorResponseDTO(
                d.getId(),
                d.getUsername(),
                d.getEmail(),
                d.getSpecialization(),
                d.getLicenseNumber(),
                d.getClinicLocation(),
                d.getRole()
        );
    }
}
