package org.mediplus.user;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.mediplus.exception.BadRequestException;
import org.mediplus.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            User authenticated = userService.authenticate(dto.getUsername(), dto.getPassword());
            return ResponseEntity.ok(authenticated.getId().toString());
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw new BadRequestException("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String username) {
        User u = userService.getUserByUsername(username);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponseDTO dto = new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRole());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.warn("Deleting user with ID: {}", id);
        User existing = userService.getUserById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(null);
        }
        User u = userService.getUserByUsername(principal.getName());
        if (u == null) {
            return ResponseEntity.status(404).body(null);
        }
        UserResponseDTO dto = new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRole());
        return ResponseEntity.ok(dto);
    }
}
