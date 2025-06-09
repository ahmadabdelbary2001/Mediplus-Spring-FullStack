package org.mediplus.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String role;

    public AdminResponseDTO() {}

    public AdminResponseDTO(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
