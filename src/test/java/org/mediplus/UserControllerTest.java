package org.mediplus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mediplus.controller.UserController;
import org.mediplus.model.Patient;
import org.mediplus.model.User;
import org.mediplus.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Standalone setup bypasses the Spring context and security filters
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    @DisplayName("POST /api/users/register → 200 OK")
    void registerUser_Success() throws Exception {
        // Create a concrete Patient
        Patient patient = new Patient();
        patient.setUsername("alice");
        patient.setEmail("alice@example.com");
        patient.setPassword("password");
        patient.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
        patient.setInsuranceId("INS-123");
        patient.setTermsAccepted(true);

        // Stub the createPatient method (NOT createUser)
        when(userService.createPatient(any(Patient.class))).thenReturn(patient);

        String json = mapper.writeValueAsString(patient);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        // Verify createPatient was called once
        verify(userService, times(1)).createPatient(any(Patient.class));
    }

    @Test
    @DisplayName("POST /api/users/register → 409 Conflict on duplicate")
    void registerUser_DuplicateUsernameOrEmail() throws Exception {
        // Build a fully valid Patient so that bean validation passes
        Patient patient = new Patient();
        patient.setUsername("bob");
        patient.setEmail("bob@example.com");
        patient.setPassword("secret");
        patient.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1980-05-15"));
        patient.setInsuranceId("INS-456");
        patient.setTermsAccepted(true);

        // Stub createPatient(...) to throw DataIntegrityViolationException
        doThrow(new DataIntegrityViolationException("duplicate"))
                .when(userService).createPatient(any(Patient.class));

        String json = mapper.writeValueAsString(patient);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(content().string("Username or email already taken"));

        verify(userService, times(1)).createPatient(any(Patient.class));
    }


    @Test
    @DisplayName("POST /api/users/login → 200 OK returns user ID")
    void login_Success() throws Exception {
        // Prepare a stored user
        User stored = new Patient();
        stored.setId(123L);
        stored.setUsername("charlie");
        stored.setEmail("charlie@example.com");
        // For simplicity, dateOfBirth and insuranceId are ignored during authenticate
        ((Patient) stored).setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1975-07-20"));
        ((Patient) stored).setInsuranceId("INS-789");
        ((Patient) stored).setTermsAccepted(true);

        // Stub getUserByUsername
        given(userService.getUserByUsername("charlie")).willReturn(stored);
        // Stub authenticate to return true
        given(userService.authenticate(any(User.class))).willReturn(true);

        String json = mapper.writeValueAsString(Map.of(
                "username", "charlie",
                "password", "pass"
        ));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("123"));

        verify(userService, times(1)).authenticate(any(User.class));
    }

    @Test
    @DisplayName("POST /api/users/login → 401 Unauthorized on bad credentials")
    void login_Failure() throws Exception {
        // Stub getUserByUsername to return null
        given(userService.getUserByUsername("dave")).willReturn(null);
        // Note: we do NOT stub authenticate because it should never be called when user is null

        String json = mapper.writeValueAsString(Map.of(
                "username", "dave",
                "password", "wrong"
        ));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Login failed"));

        // Only getUserByUsername should have been called; authenticate should NOT be called
        verify(userService, times(1)).getUserByUsername("dave");
        verify(userService, never()).authenticate(any(User.class));
    }

    @Test
    @DisplayName("GET /api/users/{username} → 200 OK returns user")
    void getUser_Found() throws Exception {
        Patient p = new Patient();
        p.setId(50L);
        p.setUsername("eve");
        p.setEmail("eve@example.com");

        given(userService.getUserByUsername("eve")).willReturn(p);

        mockMvc.perform(get("/api/users/eve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(50))
                .andExpect(jsonPath("$.username").value("eve"))
                .andExpect(jsonPath("$.email").value("eve@example.com"));

        verify(userService, times(1)).getUserByUsername("eve");
    }

    @Test
    @DisplayName("GET /api/users/{username} → 404 Not Found")
    void getUser_NotFound() throws Exception {
        given(userService.getUserByUsername("nonexistent")).willReturn(null);

        mockMvc.perform(get("/api/users/nonexistent"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUsername("nonexistent");
    }

    @Test
    @DisplayName("GET /api/users/me → 200 OK returns profile when authenticated")
    void meEndpoint_Success() throws Exception {
        Patient p = new Patient();
        p.setId(77L);
        p.setUsername("frank");
        p.setEmail("frank@example.com");
        p.setRole("PATIENT");
        p.setTermsAccepted(true);

        given(userService.getUserByUsername("frank")).willReturn(p);

        mockMvc.perform(get("/api/users/me")
                        .principal((Principal) () -> "frank"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(77))
                .andExpect(jsonPath("$.username").value("frank"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(jsonPath("$.email").value("frank@example.com"));

        verify(userService, times(1)).getUserByUsername("frank");
    }

    @Test
    @DisplayName("GET /api/users/me → 401 if not authenticated (no Principal)")
    void meEndpoint_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Not authenticated"));
    }
}