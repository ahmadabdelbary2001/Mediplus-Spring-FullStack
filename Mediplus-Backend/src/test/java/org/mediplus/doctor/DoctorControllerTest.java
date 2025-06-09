package org.mediplus.doctor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mediplus.exception.ResourceNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DoctorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(doctorController)
                .setControllerAdvice(new org.mediplus.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/doctors → 200 OK returns list of DoctorResponseDTO")
    void listAllDoctors_Success() throws Exception {
        Doctor d1 = new Doctor("doc1", "doc1@example.com", "encoded", "Cardiology", "LIC1", "Loc1");
        d1.setId(1L);
        d1.setRole("DOCTOR");
        Doctor d2 = new Doctor("doc2", "doc2@example.com", "encoded", "Neurology", "LIC2", "Loc2");
        d2.setId(2L);
        d2.setRole("DOCTOR");

        given(doctorService.findAllDoctors()).willReturn(List.of(d1, d2));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("doc1"))
                .andExpect(jsonPath("$[1].username").value("doc2"));
    }

    @Test
    @DisplayName("POST /api/doctors → 201 Created returns DoctorResponseDTO")
    void createDoctor_Success() throws Exception {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setUsername("newdoc");
        dto.setEmail("newdoc@example.com");
        dto.setPassword("securePass");
        dto.setSpecialization("General");
        dto.setLicenseNumber("LIC3");
        dto.setClinicLocation("Loc3");
        dto.setTermsAccepted(true);

        Doctor saved = new Doctor("newdoc", "newdoc@example.com", "encodedPass", "General", "LIC3", "Loc3");
        saved.setId(3L);
        saved.setRole("DOCTOR");

        given(doctorService.createDoctor(any(Doctor.class))).willReturn(saved);

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.username").value("newdoc"));
    }

    @Test
    @DisplayName("GET /api/doctors/{id} → 200 OK returns DoctorResponseDTO when found")
    void getDoctorById_Found() throws Exception {
        Doctor d = new Doctor("docx", "docx@example.com", "encoded", "Ortho", "LIC4", "Loc4");
        d.setId(7L);
        d.setRole("DOCTOR");

        given(doctorService.getDoctorById(7L)).willReturn(d);

        mockMvc.perform(get("/api/doctors/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.username").value("docx"));
    }

    @Test
    @DisplayName("GET /api/doctors/{id} → 404 Not Found when missing")
    void getDoctorById_NotFound() throws Exception {
        given(doctorService.getDoctorById(15L))
                .willThrow(new ResourceNotFoundException("Doctor not found with ID: 15"));

        mockMvc.perform(get("/api/doctors/15"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Doctor not found with ID: 15"));
    }

    @Test
    @DisplayName("PUT /api/doctors/{id} → 200 OK returns updated DoctorResponseDTO")
    void updateDoctor_Success() throws Exception {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setUsername("updDoc");
        dto.setEmail("upd@example.com");
        dto.setPassword("newPass");
        dto.setSpecialization("Derm");
        dto.setLicenseNumber("LIC5");
        dto.setClinicLocation("Loc5");
        dto.setTermsAccepted(true);

        Doctor updated = new Doctor("updDoc", "upd@example.com", "encoded", "Derm", "LIC5", "Loc5");
        updated.setId(8L);
        updated.setRole("DOCTOR");

        given(doctorService.updateDoctor(any(Doctor.class))).willReturn(updated);

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/api/doctors/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.username").value("updDoc"));
    }

    @Test
    @DisplayName("PUT /api/doctors/{id} → 404 Not Found when updating non-existent")
    void updateDoctor_NotFound() throws Exception {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setUsername("updDoc");
        dto.setEmail("upd@example.com");
        dto.setPassword("newPass");
        dto.setSpecialization("Derm");
        dto.setLicenseNumber("LIC5");
        dto.setClinicLocation("Loc5");
        dto.setTermsAccepted(true);

        given(doctorService.updateDoctor(any(Doctor.class)))
                .willThrow(new ResourceNotFoundException("Doctor not found with ID: 8"));

        String payload = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/api/doctors/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Doctor not found with ID: 8"));
    }

    @Test
    @DisplayName("DELETE /api/doctors/{id} → 204 No Content when deleted")
    void deleteDoctor_Success() throws Exception {
        mockMvc.perform(delete("/api/doctors/12"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/doctors/{id} → 404 Not Found when missing")
    void deleteDoctor_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Doctor not found with ID: 12"))
                .when(doctorService).deleteDoctor(12L);

        mockMvc.perform(delete("/api/doctors/12"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Doctor not found with ID: 12"));
    }
}
