package org.mediplus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mediplus.controller.PatientController;
import org.mediplus.model.Doctor;
import org.mediplus.model.Patient;
import org.mediplus.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PatientControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    @DisplayName("GET /api/patients/me → 200 OK returns Patient when principal is patient")
    void getCurrentPatient_Success() throws Exception {
        Patient p = new Patient();
        p.setId(101L);
        p.setUsername("sam");
        p.setEmail("sam@example.com");
        p.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1985-08-20"));
        p.setInsuranceId("INS-999");
        p.setTermsAccepted(true);

        given(userService.getUserByUsername("sam")).willReturn(p);

        mockMvc.perform(get("/api/patients/me")
                        .principal(() -> "sam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.username").value("sam"))
                .andExpect(jsonPath("$.email").value("sam@example.com"))
                .andExpect(jsonPath("$.insuranceId").value("INS-999"));

        verify(userService, times(1)).getUserByUsername("sam");
    }

    @Test
    @DisplayName("GET /api/patients/me → 500 class-cast error if user is not a Patient")
    void getCurrentPatient_ClassCastException() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(202L);
        doctor.setUsername("drX");
        doctor.setEmail("drx@example.com");
        doctor.setSpecialization("General");
        doctor.setLicenseNumber("LIC-123");
        doctor.setClinicLocation("Main Clinic");

        given(userService.getUserByUsername("drX")).willReturn(doctor);

        mockMvc.perform(get("/api/patients/me")
                        .principal(() -> "drX"))
                .andExpect(status().isInternalServerError()); // now returns 500

        verify(userService, times(1)).getUserByUsername("drX");
    }

    @Test
    @DisplayName("GET /api/patients/me → 400 if user not found")
    void getCurrentPatient_UserNotFound() throws Exception {
        given(userService.getUserByUsername("ghost")).willReturn(null);

        mockMvc.perform(get("/api/patients/me")
                        .principal(() -> "ghost"))
                .andExpect(status().isBadRequest()); // now returns 400

        verify(userService, times(1)).getUserByUsername("ghost");
    }
}
