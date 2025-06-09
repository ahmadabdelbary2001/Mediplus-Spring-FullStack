package org.mediplus.appointment;

import jakarta.validation.Valid;
import org.mediplus.exception.BadRequestException;
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
        return ResponseEntity.ok(toDTO(appt));
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(@Valid @RequestBody AppointmentRequestDTO request) {
        Appointment appt = fromDTO(request);
        if (appt.getDateTime().isBefore(java.time.LocalDateTime.now())) {
            throw new BadRequestException("Cannot create appointment in the past");
        }
        Appointment created = apptService.createAppointment(appt);
        return ResponseEntity.status(201).body(toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO request) {

        Appointment appt = fromDTO(request);
        appt.setId(id);
        if (appt.getDateTime().isBefore(java.time.LocalDateTime.now())) {
            throw new BadRequestException("Cannot reschedule appointment to a past date");
        }
        Appointment updated = apptService.updateAppointment(id, appt);
        return ResponseEntity.ok(toDTO(updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Appointment existing = apptService.updateAppointmentStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
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
