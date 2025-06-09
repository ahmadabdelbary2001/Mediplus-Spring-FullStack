package org.mediplus.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mediplus.exception.ResourceNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mediplus.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PatientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PatientController patientController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(patientController)
                .setControllerAdvice(new org.mediplus.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/patients/me → 200 OK returns PatientResponseDTO when authenticated")
    void getCurrentPatient_Success() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        Patient p = new Patient("jane", "jane@example.com", "encodedPass", dob, "INS-123");
        p.setId(1L);
        p.setRole("PATIENT");
        p.setInsuranceId("INS-123");

        given(userService.getUserByUsername("jane")).willReturn(p);

        mockMvc.perform(get("/api/patients/me")
                        .principal((Principal) () -> "jane"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("jane"))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.insuranceId").value("INS-123"))
                .andExpect(jsonPath("$.role").value("PATIENT"));
    }

    @Test
    @DisplayName("GET /api/patients/me → 404 Not Found when user not found")
    void getCurrentPatient_NotFound() throws Exception {
        given(userService.getUserByUsername("jane"))
                .willThrow(new ResourceNotFoundException("Patient not found"));

        mockMvc.perform(get("/api/patients/me")
                        .principal((Principal) () -> "jane"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    @DisplayName("GET /api/patients/me → 400 Bad Request when no principal")
    void getCurrentPatient_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/patients/me"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Not authenticated"));
    }

    @Test
    @DisplayName("GET /api/patients → 200 OK returns list of PatientResponseDTO")
    void listAllPatients_Success() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        Patient p1 = new Patient("a", "a@example.com", "pass", dob, "INS-1");
        p1.setId(1L);
        p1.setRole("PATIENT");
        p1.setInsuranceId("INS-1");

        Patient p2 = new Patient("b", "b@example.com", "pass", dob, "INS-2");
        p2.setId(2L);
        p2.setRole("PATIENT");
        p2.setInsuranceId("INS-2");

        given(patientService.findAllPatients()).willReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("a"))
                .andExpect(jsonPath("$[1].username").value("b"));
    }

    @Test
    @DisplayName("POST /api/patients → 201 Created returns PatientResponseDTO")
    void createPatient_Success() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        PatientRequestDTO dto = new PatientRequestDTO();
        dto.setUsername("new");
        dto.setEmail("new@example.com");
        dto.setPassword("securePass");
        dto.setDateOfBirth(dob);
        dto.setInsuranceId("INS-3");
        dto.setTermsAccepted(true);

        Patient saved = new Patient("new", "new@example.com", "encodedPass", dob, "INS-3");
        saved.setId(5L);
        saved.setRole("PATIENT");
        saved.setInsuranceId("INS-3");

        given(patientService.createPatient(any(Patient.class))).willReturn(saved);

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("new"))
                .andExpect(jsonPath("$.insuranceId").value("INS-3"));
    }

    @Test
    @DisplayName("PUT /api/patients/{id} → 200 OK returns updated PatientResponseDTO")
    void updatePatient_Success() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        PatientRequestDTO dto = new PatientRequestDTO();
        dto.setUsername("upd");
        dto.setEmail("upd@example.com");
        dto.setPassword("newPass");
        dto.setDateOfBirth(dob);
        dto.setInsuranceId("INS-4");
        dto.setTermsAccepted(true);

        Patient updated = new Patient("upd", "upd@example.com", "encodedPass", dob, "INS-4");
        updated.setId(5L);
        updated.setRole("PATIENT");

        given(patientService.updatePatient(any(Patient.class))).willReturn(updated);

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/api/patients/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("upd"));
    }

    @Test
    @DisplayName("PUT /api/patients/{id} → 404 Not Found when updating non-existent")
    void updatePatient_NotFound() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        PatientRequestDTO dto = new PatientRequestDTO();
        dto.setUsername("upd");
        dto.setEmail("upd@example.com");
        dto.setPassword("newPass");
        dto.setDateOfBirth(dob);
        dto.setInsuranceId("INS-4");
        dto.setTermsAccepted(true);

        given(patientService.updatePatient(any(Patient.class)))
                .willThrow(new ResourceNotFoundException("Patient not found with ID: 5"));

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/api/patients/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Patient not found with ID: 5"));
    }

    @Test
    @DisplayName("DELETE /api/patients/{id} → 204 No Content when deleted")
    void deletePatient_Success() throws Exception {
        mockMvc.perform(delete("/api/patients/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/patients/{id} → 404 Not Found when missing")
    void deletePatient_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Patient not found with ID: 10"))
                .when(patientService).deletePatient(10L);

        mockMvc.perform(delete("/api/patients/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Patient not found with ID: 10"));
    }
}
