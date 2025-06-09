package org.mediplus.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mediplus.exception.GlobalExceptionHandler;
import org.mediplus.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
        mockMvc = MockMvcBuilders
                .standaloneSetup(appointmentController)
                .setControllerAdvice(new org.mediplus.exception.GlobalExceptionHandler())
                .build();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("GET /api/appointments → 200 OK returns all appointments")
    void listAllAppointments_Success() throws Exception {
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
                .andExpect(jsonPath("$[1].status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("GET /api/appointments/{id} → 200 OK returns AppointmentResponseDTO when found")
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
                .andExpect(jsonPath("$.patientUsername").value("carol"));
    }

    @Test
    @DisplayName("GET /api/appointments/{id} → 404 Not Found when missing")
    void getById_NotFound() throws Exception {
        given(apptService.getAppointmentById(99L))
                .willThrow(new ResourceNotFoundException("Appointment not found with ID: 99"));

        mockMvc.perform(get("/api/appointments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment not found with ID: 99"));
    }

    @Test
    @DisplayName("POST /api/appointments → 201 Created returns AppointmentResponseDTO")
    void createAppointment_Success() throws Exception {
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setDateTime(LocalDateTime.of(2025, 8, 5, 15, 0));
        dto.setStatus("PENDING");
        dto.setPatientUsername("dan");
        dto.setDoctorUsername("docD");

        Appointment saved = new Appointment();
        saved.setId(30L);
        saved.setDateTime(dto.getDateTime());
        saved.setStatus(dto.getStatus());
        saved.setPatientUsername(dto.getPatientUsername());
        saved.setDoctorUsername(dto.getDoctorUsername());

        given(apptService.createAppointment(any(Appointment.class))).willReturn(saved);

        String payload = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON) // 3. نحدّد أنّنا نتوقع JSON
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(30))
                .andExpect(jsonPath("$.doctorUsername").value("docD"));
    }

    @Test
    @DisplayName("PUT /api/appointments/{id} → 200 OK returns updated AppointmentResponseDTO")
    void updateAppointment_Success() throws Exception {
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setDateTime(LocalDateTime.of(2025, 9, 10, 10, 0));
        dto.setStatus("CANCELLED");
        dto.setPatientUsername("erin");
        dto.setDoctorUsername("docE");

        Appointment updated = new Appointment();
        updated.setId(40L);
        updated.setDateTime(dto.getDateTime());
        updated.setStatus(dto.getStatus());
        updated.setPatientUsername(dto.getPatientUsername());
        updated.setDoctorUsername(dto.getDoctorUsername());

        given(apptService.updateAppointment(eq(40L), any(Appointment.class))).willReturn(updated);

        mockMvc.perform(put("/api/appointments/40")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON) // نتوقع JSON
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(40))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @DisplayName("POST /api/appointments → 400 Bad Request when date in the past")
    void createAppointment_PastDate() throws Exception {
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setDateTime(LocalDateTime.of(2020, 1, 1, 10, 0)); // تاريخ قديم
        dto.setStatus("PENDING");
        dto.setPatientUsername("dan");
        dto.setDoctorUsername("docD");

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON) // نتوقع JSON
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Cannot create appointment in the past"));
    }

    @Test
    @DisplayName("PUT /api/appointments/{id} → 404 Not Found when missing")
    void updateAppointment_NotFound() throws Exception {
        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setDateTime(LocalDateTime.of(2025, 10, 1, 12, 0));
        dto.setStatus("PENDING");
        dto.setPatientUsername("fiona");
        dto.setDoctorUsername("docF");

        given(apptService.updateAppointment(eq(99L), any(Appointment.class)))
                .willThrow(new ResourceNotFoundException("Appointment not found with ID: 99"));

        mockMvc.perform(put("/api/appointments/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON) // نتوقع JSON
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment not found with ID: 99"));
    }

    @Test
    @DisplayName("PATCH /api/appointments/{id}/status → 204 No Content on success")
    void updateStatus_Success() throws Exception {
        mockMvc.perform(patch("/api/appointments/50/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /api/appointments/{id}/status → 404 Not Found when missing")
    void updateStatus_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Appointment not found with ID: 99"))
                .when(apptService).updateAppointmentStatus(99L, "CANCELLED");

        mockMvc.perform(patch("/api/appointments/99/status")
                        .param("status", "CANCELLED"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment not found with ID: 99"));
    }

    @Test
    @DisplayName("DELETE /api/appointments/{id} → 204 No Content when successful")
    void deleteAppointment_Success() throws Exception {
        doNothing().when(apptService).deleteAppointment(30L);

        mockMvc.perform(delete("/api/appointments/30")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/appointments/{id} → 404 Not Found when missing")
    void deleteAppointment_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Appointment not found with ID: 99"))
                .when(apptService).deleteAppointment(99L);

        mockMvc.perform(delete("/api/appointments/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment not found with ID: 99"));
    }
}
