package org.mediplus.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AppointmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppointmentService apptService;

    @InjectMocks
    private AppointmentController appointmentController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Register JavaTimeModule so LocalDateTime works
        mapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();
    }

    @Test
    @DisplayName("GET /api/appointments → 200 OK returns all appointments")
    void listAllAppointments() throws Exception {
        Appointment a1 = new Appointment();
        a1.setId(10L);
        a1.setDateTime(LocalDateTime.of(2025, 6, 15, 14, 30));
        a1.setStatus("PENDING");
        a1.setPatientUsername("alice");
        a1.setDoctorUsername("docA");

        Appointment a2 = new Appointment();
        a2.setId(11L);
        a2.setDateTime(LocalDateTime.of(2025, 6, 16, 9, 0));
        a2.setStatus("CONFIRMED");
        a2.setPatientUsername("bob");
        a2.setDoctorUsername("docB");

        given(apptService.getAllAppointments()).willReturn(List.of(a1, a2));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].id").value(11))
                .andExpect(jsonPath("$[1].status").value("CONFIRMED"));

        verify(apptService, times(1)).getAllAppointments();
    }

    @Test
    @DisplayName("GET /api/appointments/{id} → 200 OK when found")
    void getById_Found() throws Exception {
        Appointment a = new Appointment();
        a.setId(20L);
        a.setDateTime(LocalDateTime.of(2025, 7, 1, 11, 0));
        a.setStatus("CONFIRMED");
        a.setPatientUsername("carol");
        a.setDoctorUsername("docC");

        given(apptService.getAppointmentById(20L)).willReturn(a);

        mockMvc.perform(get("/api/appointments/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.patientUsername").value("carol"))
                .andExpect(jsonPath("$.doctorUsername").value("docC"));

        verify(apptService, times(1)).getAppointmentById(20L);
    }

    @Test
    @DisplayName("GET /api/appointments/{id} → 404 Not Found")
    void getById_NotFound() throws Exception {
        given(apptService.getAppointmentById(99L)).willReturn(null);

        mockMvc.perform(get("/api/appointments/99"))
                .andExpect(status().isNotFound());

        verify(apptService, times(1)).getAppointmentById(99L);
    }

    @Test
    @DisplayName("POST /api/appointments → 201 Created")
    void createAppointment_Success() throws Exception {
        Appointment incoming = new Appointment();
        incoming.setDateTime(LocalDateTime.of(2025, 8, 5, 15, 0));
        incoming.setStatus("PENDING");
        incoming.setPatientUsername("dan");
        incoming.setDoctorUsername("docD");

        Appointment saved = new Appointment();
        saved.setId(30L);
        saved.setDateTime(incoming.getDateTime());
        saved.setStatus(incoming.getStatus());
        saved.setPatientUsername(incoming.getPatientUsername());
        saved.setDoctorUsername(incoming.getDoctorUsername());

        given(apptService.createAppointment(any(Appointment.class))).willReturn(saved);

        String json = mapper.writeValueAsString(incoming);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(30))
                .andExpect(jsonPath("$.patientUsername").value("dan"))
                .andExpect(jsonPath("$.doctorUsername").value("docD"));

        verify(apptService, times(1)).createAppointment(any(Appointment.class));
    }

    @Test
    @DisplayName("PUT /api/appointments/{id} → 200 OK when update succeeds")
    void updateAppointment_Success() throws Exception {
        Appointment updateRequest = new Appointment();
        updateRequest.setId(40L);
        updateRequest.setDateTime(LocalDateTime.of(2025, 9, 10, 10, 0));
        updateRequest.setStatus("CANCELLED");
        updateRequest.setPatientUsername("erin");
        updateRequest.setDoctorUsername("docE");

        Appointment updated = new Appointment();
        updated.setId(40L);
        updated.setDateTime(updateRequest.getDateTime());
        updated.setStatus(updateRequest.getStatus());
        updated.setPatientUsername(updateRequest.getPatientUsername());
        updated.setDoctorUsername(updateRequest.getDoctorUsername());

        given(apptService.updateAppointment(eq(40L), any(Appointment.class))).willReturn(updated);

        String json = mapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/appointments/40")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(40))
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(apptService, times(1)).updateAppointment(eq(40L), any(Appointment.class));
    }

    @Test
    @DisplayName("PUT /api/appointments/{id} → 404 Not Found if not present")
    void updateAppointment_NotFound() throws Exception {
        Appointment updateRequest = new Appointment();
        updateRequest.setDateTime(LocalDateTime.of(2025, 10, 1, 12, 0));
        updateRequest.setStatus("PENDING");
        updateRequest.setPatientUsername("fiona");
        updateRequest.setDoctorUsername("docF");

        given(apptService.updateAppointment(eq(99L), any(Appointment.class))).willReturn(null);

        String json = mapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/appointments/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        verify(apptService, times(1)).updateAppointment(eq(99L), any(Appointment.class));
    }

    @Test
    @DisplayName("PATCH /api/appointments/{id}/status → 204 No Content on success")
    void updateStatus_Success() throws Exception {
        Appointment existing = new Appointment();
        existing.setId(50L);
        existing.setStatus("PENDING");
        existing.setDateTime(LocalDateTime.of(2025, 11, 5, 16, 0));
        existing.setPatientUsername("george");
        existing.setDoctorUsername("docG");

        Appointment updated = new Appointment();
        updated.setId(50L);
        updated.setStatus("CONFIRMED");
        updated.setDateTime(existing.getDateTime());
        updated.setPatientUsername(existing.getPatientUsername());
        updated.setDoctorUsername(existing.getDoctorUsername());

        given(apptService.updateAppointmentStatus(50L, "CONFIRMED")).willReturn(updated);

        mockMvc.perform(patch("/api/appointments/50/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isNoContent());

        verify(apptService, times(1)).updateAppointmentStatus(50L, "CONFIRMED");
    }

    @Test
    @DisplayName("PATCH /api/appointments/{id}/status → 404 if not found")
    void updateStatus_NotFound() throws Exception {
        given(apptService.updateAppointmentStatus(99L, "CANCELLED")).willReturn(null);

        mockMvc.perform(patch("/api/appointments/99/status")
                        .param("status", "CANCELLED"))
                .andExpect(status().isNotFound());

        verify(apptService, times(1)).updateAppointmentStatus(99L, "CANCELLED");
    }

    @Test
    @DisplayName("DELETE /api/appointments/{id} → 204 No Content when deleted")
    void deleteAppointment_Success() throws Exception {
        Appointment existing = new Appointment();
        existing.setId(60L);
        existing.setStatus("PENDING");
        existing.setPatientUsername("harry");
        existing.setDoctorUsername("docH");

        given(apptService.getAppointmentById(60L)).willReturn(existing);
        doNothing().when(apptService).deleteAppointment(60L);

        mockMvc.perform(delete("/api/appointments/60"))
                .andExpect(status().isNoContent());

        verify(apptService, times(1)).getAppointmentById(60L);
        verify(apptService, times(1)).deleteAppointment(60L);
    }

    @Test
    @DisplayName("DELETE /api/appointments/{id} → 404 Not Found if no such appointment")
    void deleteAppointment_NotFound() throws Exception {
        given(apptService.getAppointmentById(99L)).willReturn(null);

        mockMvc.perform(delete("/api/appointments/99"))
                .andExpect(status().isNotFound());

        verify(apptService, times(1)).getAppointmentById(99L);
        verify(apptService, never()).deleteAppointment(anyLong());
    }
}
