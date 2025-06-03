package org.mediplus.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mediplus.patient.Patient;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("POST /api/users/login → 200 OK returns user ID when successful")
    void login_Success() throws Exception {
        // Prepare a stored user
        Patient stored = new Patient();
        stored.setId(123L);
        stored.setUsername("charlie");
        stored.setEmail("charlie@example.com");
        // DateOfBirth and insuranceId not used in authenticate here
        stored.setRole("PATIENT");
        stored.setTermsAccepted(true);

        given(userService.getUserByUsername("charlie")).willReturn(stored);
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
    @DisplayName("POST /api/users/login → 401 Unauthorized when failed")
    void login_Failed() throws Exception {
        given(userService.getUserByUsername("charlie")).willReturn(null);

        String json = mapper.writeValueAsString(Map.of(
                "username", "charlie",
                "password", "pass"
        ));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Login failed"));
    }

    @Test
    @DisplayName("GET /api/users/{username} → 200 OK returns User when found")
    void getUser_Found() throws Exception {
        Patient u = new Patient();
        u.setId(2L);
        u.setUsername("alice");
        u.setEmail("alice@example.com");
        u.setRole("PATIENT");
        u.setTermsAccepted(true);

        given(userService.getUserByUsername("alice")).willReturn(u);

        mockMvc.perform(get("/api/users/alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));

        verify(userService, times(1)).getUserByUsername("alice");
    }

    @Test
    @DisplayName("GET /api/users/{username} → 404 Not Found when missing")
    void getUser_NotFound() throws Exception {
        given(userService.getUserByUsername("bob")).willReturn(null);

        mockMvc.perform(get("/api/users/bob"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUsername("bob");
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
    @DisplayName("GET /api/users/me → 401 Unauthorized when not authenticated")
    void meEndpoint_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Not authenticated"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} → 204 No Content when deleted")
    void deleteUser_Success() throws Exception {
        Patient u = new Patient();
        u.setId(3L);
        u.setUsername("mark");
        u.setEmail("mark@example.com");
        u.setRole("PATIENT");
        u.setTermsAccepted(true);

        given(userService.getUserById(3L)).willReturn(u);

        mockMvc.perform(delete("/api/users/3"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(3L);
    }

    @Test
    @DisplayName("DELETE /api/users/{id} → 404 Not Found when missing")
    void deleteUser_NotFound() throws Exception {
        given(userService.getUserById(5L)).willReturn(null);

        mockMvc.perform(delete("/api/users/5"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(5L);
    }
}
