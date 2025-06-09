package org.mediplus.admin;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@Validated
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminResponseDTO> createAdmin(
            @Valid @RequestBody AdminRequestDTO dto) {
        log.info("Creating new admin: {}", dto.getUsername());

        if (adminService.existsByUsername(dto.getUsername())) {
            return ResponseEntity.status(409).build();
        }
        if (adminService.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(409).build();
        }

        Admin entity = fromDTO(dto);
        Admin created = adminService.createAdmin(entity);
        return ResponseEntity.status(201).body(toDTO(created));
    }

    @GetMapping
    public ResponseEntity<List<AdminResponseDTO>> listAllAdmins() {
        List<AdminResponseDTO> dtos = adminService.findAllAdmins().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable Long id) {
        Admin a = adminService.getAdminById(id);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(a));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminRequestDTO dto) {
        Admin existing = adminService.getAdminById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        if (!existing.getUsername().equals(dto.getUsername())
                && adminService.existsByUsername(dto.getUsername())) {
            return ResponseEntity.status(409).build();
        }
        if (!existing.getEmail().equals(dto.getEmail())
                && adminService.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(409).build();
        }

        Admin toUpdate = new Admin();
        toUpdate.setId(id);
        toUpdate.setUsername(dto.getUsername());
        toUpdate.setEmail(dto.getEmail());
        toUpdate.setPassword(dto.getPassword());

        Admin updated = adminService.updateAdmin(toUpdate);
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        Admin existing = adminService.getAdminById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    private Admin fromDTO(AdminRequestDTO dto) {
        Admin a = new Admin(dto.getUsername(), dto.getEmail(), dto.getPassword());
        a.setTermsAccepted(dto.getTermsAccepted());
        return a;
    }

    private AdminResponseDTO toDTO(Admin a) {
        return new AdminResponseDTO(
                a.getId(),
                a.getUsername(),
                a.getEmail(),
                a.getRole()
        );
    }
}
