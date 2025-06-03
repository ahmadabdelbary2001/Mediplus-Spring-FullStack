package org.mediplus.patient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mediplus.user.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Getter
@Setter
@Entity
@DiscriminatorValue("PATIENT")
public class Patient extends User {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "insurance_id")
    private String insuranceId;

    // Constructors
    public Patient() {
        super();
        this.setRole("PATIENT");
    }

    public Patient(String username, String email, String password, Date dateOfBirth, String insuranceId) {
        super(username, email, password, "PATIENT");
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_PATIENT"));
    }
}