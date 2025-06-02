package org.mediplus.controller;

import jakarta.validation.Valid;
import org.mediplus.model.Appointment;
import org.mediplus.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Appointment> listAll() {
        return apptService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable Long id) {
        Appointment appt = apptService.getAppointmentById(id);
        if (appt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appt);
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@Valid @RequestBody Appointment appointment) {
        Appointment created = apptService.createAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> update(
            @PathVariable Long id,
            @Valid @RequestBody Appointment appointment) {

        Appointment updated = apptService.updateAppointment(id, appointment);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Appointment updated = apptService.updateAppointmentStatus(id, status);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
}
