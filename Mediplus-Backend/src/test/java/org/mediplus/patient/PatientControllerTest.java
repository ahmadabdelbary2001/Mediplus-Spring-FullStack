package org.mediplus.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    @DisplayName("GET /api/patients/me → 200 OK returns PatientResponseDTO when authenticated")
    void getCurrentPatient_authenticated() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        Patient p = new Patient("jane", "jane@example.com", "pass", dob, "INS-123");
        p.setId(1L);
        p.setInsuranceId("INS-123");
        p.setRole("PATIENT");
        p.setTermsAccepted(true);

        given(userService.getUserByUsername("jane")).willReturn(p);

        mockMvc.perform(get("/api/patients/me")
                        .principal((Principal) () -> "jane"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jane"))
                .andExpect(jsonPath("$.insuranceId").value("INS-123"));

        verify(userService, times(1)).getUserByUsername("jane");
    }

    @Test
    @DisplayName("GET /api/patients/me → 404 Not Found when user not found")
    void getCurrentPatient_notFound() throws Exception {
        given(userService.getUserByUsername("jane")).willReturn(null);

        mockMvc.perform(get("/api/patients/me")
                        .principal((Principal) () -> "jane"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUsername("jane");
    }

    @Test
    @DisplayName("GET /api/patients/me → 401 Unauthorized when no principal")
    void getCurrentPatient_unauthenticated() throws Exception {
        mockMvc.perform(get("/api/patients/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/patients → 200 OK returns list of PatientResponseDTO")
    void listAllPatients_success() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        Patient p1 = new Patient("a", "a@example.com", "pass", dob, "INS-1");
        p1.setRole("PATIENT");
        Patient p2 = new Patient("b", "b@example.com", "pass", dob, "INS-2");
        p2.setRole("PATIENT");
        given(patientService.findAllPatients()).willReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("a"))
                .andExpect(jsonPath("$[1].username").value("b"));

        verify(patientService, times(1)).findAllPatients();
    }

    @Test
    @DisplayName("POST /api/patients → 201 Created returns created PatientResponseDTO")
    void createPatient_success() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        PatientRequestDTO dto = new PatientRequestDTO();
        dto.setUsername("new");
        dto.setEmail("new@example.com");
        dto.setPassword("password"); // تم تغييره من "pass" إلى "password"
        dto.setDateOfBirth(dob);
        dto.setInsuranceId("INS-3");
        dto.setTermsAccepted(true);

        Patient p = new Patient("new", "new@example.com", "password", dob, "INS-3");
        p.setId(5L);
        p.setRole("PATIENT");
        given(patientService.createPatient(any(Patient.class))).willReturn(p);

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("new"));

        verify(patientService, times(1)).createPatient(any(Patient.class));
    }

    @Test
    @DisplayName("PUT /api/patients/{id} → 200 OK returns updated PatientResponseDTO")
    void updatePatient_success() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        PatientRequestDTO dto = new PatientRequestDTO();
        dto.setUsername("upd");
        dto.setEmail("upd@example.com");
        dto.setPassword("password"); // تم تغييره هنا أيضًا
        dto.setDateOfBirth(dob);
        dto.setInsuranceId("INS-4");
        dto.setTermsAccepted(true);

        Patient p = new Patient("upd", "upd@example.com", "password", dob, "INS-4");
        p.setId(5L);
        p.setRole("PATIENT");
        given(patientService.updatePatient(any(Patient.class))).willReturn(p);

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/api/patients/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("upd"));

        verify(patientService, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    @DisplayName("PUT /api/patients/{id} → 404 Not Found when update fails")
    void updatePatient_notFound() throws Exception {
        Date dob = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
        PatientRequestDTO dto = new PatientRequestDTO();
        dto.setUsername("upd");
        dto.setEmail("upd@example.com");
        dto.setPassword("password"); // تم تغييره هنا أيضًا
        dto.setDateOfBirth(dob);
        dto.setInsuranceId("INS-4");
        dto.setTermsAccepted(true);

        given(patientService.updatePatient(any(Patient.class))).willReturn(null);

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/api/patients/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());

        verify(patientService, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    @DisplayName("DELETE /api/patients/{id} → 204 No Content")
    void deletePatient_success() throws Exception {
        mockMvc.perform(delete("/api/patients/10"))
                .andExpect(status().isNoContent());

        verify(patientService, times(1)).deletePatient(10L);
    }
}
