package org.mediplus.appointment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@Validated
public class AppointmentController {

    private final AppointmentService apptService;

    public AppointmentController(AppointmentService apptService) {
        this.apptService = apptService;
    }

    @GetMapping
    public List<AppointmentResponseDTO> listAll() {
        return apptService.getAllAppointments().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getById(@PathVariable Long id) {
        Appointment appt = apptService.getAppointmentById(id);
        if (appt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(appt));
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(@Valid @RequestBody AppointmentRequestDTO dto) {
        Appointment toSave = fromDTO(dto);
        Appointment created = apptService.createAppointment(toSave);
        return ResponseEntity.status(201).body(toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO dto) {

        Appointment toUpdate = fromDTO(dto);
        toUpdate.setId(id);
        Appointment updated = apptService.updateAppointment(id, toUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Appointment updated = apptService.updateAppointmentStatus(id, status);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Appointment existing = apptService.getAppointmentById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        apptService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    // Helper functions for converting between DTO and Appointment entity
    private Appointment fromDTO(AppointmentRequestDTO dto) {
        return new Appointment(
                null,
                dto.getDateTime(),
                dto.getStatus(),
                dto.getPatientUsername(),
                dto.getDoctorUsername()
        );
    }

    private AppointmentResponseDTO toDTO(Appointment appt) {
        return new AppointmentResponseDTO(
                appt.getId(),
                appt.getDateTime(),
                appt.getStatus(),
                appt.getPatientUsername(),
                appt.getDoctorUsername()
        );
    }
}
