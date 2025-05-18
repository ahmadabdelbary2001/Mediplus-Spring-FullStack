package org.mediplus.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;

    // Constructors
    public User() {}
    public User(Long id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}