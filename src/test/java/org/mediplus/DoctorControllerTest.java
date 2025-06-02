package org.mediplus;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mediplus.controller.DoctorController;
import org.mediplus.model.Doctor;
import org.mediplus.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DoctorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private DoctorController doctorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();
    }

    @Test
    @DisplayName("GET /api/doctors → 200 OK returns list of doctors")
    void listAllDoctors_Success() throws Exception {
        Doctor d1 = new Doctor();
        d1.setId(1L);
        d1.setUsername("docA");
        d1.setEmail("docA@med.com");
        d1.setSpecialization("Cardiology");
        d1.setLicenseNumber("LIC-100");
        d1.setClinicLocation("Loc A");

        Doctor d2 = new Doctor();
        d2.setId(2L);
        d2.setUsername("docB");
        d2.setEmail("docB@med.com");
        d2.setSpecialization("Dermatology");
        d2.setLicenseNumber("LIC-200");
        d2.setClinicLocation("Loc B");

        given(userService.findAllDoctors()).willReturn(List.of(d1, d2));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("docA"))
                .andExpect(jsonPath("$[1].username").value("docB"));

        verify(userService, times(1)).findAllDoctors();
    }

    @Test
    @DisplayName("GET /api/doctors → 200 OK returns empty list when no doctors")
    void listAllDoctors_Empty() throws Exception {
        given(userService.findAllDoctors()).willReturn(List.of());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userService, times(1)).findAllDoctors();
    }
}
