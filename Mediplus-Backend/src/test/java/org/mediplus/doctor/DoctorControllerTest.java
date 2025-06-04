package org.mediplus.doctor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
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
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    @DisplayName("GET /api/doctors → 200 OK returns list of DoctorResponseDTO")
    void listAllDoctors_success() throws Exception {
        Doctor d1 = new Doctor("doc1", "doc1@example.com", "password", "Cardiology", "LIC1", "Loc1");
        d1.setId(3L);
        d1.setRole("DOCTOR");

        Doctor d2 = new Doctor("doc2", "doc2@example.com", "password", "Neurology", "LIC2", "Loc2");
        d2.setId(4L);
        d2.setRole("DOCTOR");

        given(doctorService.findAllDoctors()).willReturn(List.of(d1, d2));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].username").value("doc1"))
                .andExpect(jsonPath("$[1].username").value("doc2"));

        verify(doctorService, times(1)).findAllDoctors();
    }

    @Test
    @DisplayName("POST /api/doctors → 201 Created returns created DoctorResponseDTO")
    void createDoctor_success() throws Exception {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setUsername("newdoc");
        dto.setEmail("newdoc@example.com");
        dto.setPassword("password");
        dto.setSpecialization("General");
        dto.setLicenseNumber("LIC3");
        dto.setClinicLocation("Loc3");
        dto.setTermsAccepted(true);

        Doctor d = new Doctor("newdoc", "newdoc@example.com", "password", "General", "LIC3", "Loc3");
        d.setId(5L);
        d.setRole("DOCTOR");
        given(doctorService.createDoctor(any(Doctor.class))).willReturn(d);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("newdoc"))
                .andExpect(jsonPath("$.specialization").value("General"));

        verify(doctorService, times(1)).createDoctor(any(Doctor.class));
    }

    @Test
    @DisplayName("GET /api/doctors/{id} → 200 OK returns DoctorResponseDTO when found")
    void getDoctorById_found() throws Exception {
        Doctor d = new Doctor("docx", "docx@example.com", "password", "Ortho", "LIC4", "Loc4");
        d.setId(7L);
        d.setRole("DOCTOR");
        given(doctorService.getDoctorById(7L)).willReturn(d);

        mockMvc.perform(get("/api/doctors/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.username").value("docx"))
                .andExpect(jsonPath("$.specialization").value("Ortho"));

        verify(doctorService, times(1)).getDoctorById(7L);
    }

    @Test
    @DisplayName("GET /api/doctors/{id} → 404 Not Found when missing")
    void getDoctorById_notFound() throws Exception {
        given(doctorService.getDoctorById(15L)).willReturn(null);

        mockMvc.perform(get("/api/doctors/15"))
                .andExpect(status().isNotFound());

        verify(doctorService, times(1)).getDoctorById(15L);
    }

    @Test
    @DisplayName("PUT /api/doctors/{id} → 200 OK returns updated DoctorResponseDTO")
    void updateDoctor_success() throws Exception {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setUsername("updDoc");
        dto.setEmail("upd@example.com");
        dto.setPassword("password");
        dto.setSpecialization("Derm");
        dto.setLicenseNumber("LIC5");
        dto.setClinicLocation("Loc5");
        dto.setTermsAccepted(true);

        Doctor d = new Doctor("updDoc", "upd@example.com", "password", "Derm", "LIC5", "Loc5");
        d.setId(8L);
        d.setRole("DOCTOR");
        given(doctorService.updateDoctor(any(Doctor.class))).willReturn(d);

        mockMvc.perform(put("/api/doctors/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.username").value("updDoc"))
                .andExpect(jsonPath("$.specialization").value("Derm"));

        verify(doctorService, times(1)).updateDoctor(any(Doctor.class));
    }

    @Test
    @DisplayName("PUT /api/doctors/{id} → 404 Not Found when update fails")
    void updateDoctor_notFound() throws Exception {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setUsername("updDoc");
        dto.setEmail("upd@example.com");
        dto.setPassword("password");
        dto.setSpecialization("Derm");
        dto.setLicenseNumber("LIC5");
        dto.setClinicLocation("Loc5");
        dto.setTermsAccepted(true);

        given(doctorService.updateDoctor(any(Doctor.class))).willReturn(null);

        mockMvc.perform(put("/api/doctors/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());

        verify(doctorService, times(1)).updateDoctor(any(Doctor.class));
    }

    @Test
    @DisplayName("DELETE /api/doctors/{id} → 204 No Content")
    void deleteDoctor_success() throws Exception {
        mockMvc.perform(delete("/api/doctors/12"))
                .andExpect(status().isNoContent());

        verify(doctorService, times(1)).deleteDoctor(12L);
    }
}
