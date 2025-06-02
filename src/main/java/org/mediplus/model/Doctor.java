package org.mediplus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("DOCTOR")
public class Doctor extends User {

    @Column
    private String specialization;

//    @Column(name = "license_number")
//    private String licenseNumber;
//
//    @Column(name = "clinic_location")
//    private String clinicLocation;

    private String licenseNumber;
    private String clinicLocation;
//    private List<LocalDateTime> availableSlots; // times the doctor is available

    public Doctor() {
        super();
        this.setRole("DOCTOR");
    }

    public Doctor(String username,
                  String email,
                  String password,
                  String specialization,
                  String licenseNumber,
                  String clinicLocation) {
        super(username, email, password, "DOCTOR");
        this.specialization = specialization;
        this.licenseNumber  = licenseNumber;
        this.clinicLocation = clinicLocation;
//        this.availableSlots = availableSlots;
    }

    //    public List<LocalDateTime> getAvailableSlots() {
//        return availableSlots;
//    }
//
//    public void setAvailableSlots(List<LocalDateTime> availableSlots) {
//        this.availableSlots = availableSlots;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_DOCTOR"));
    }
}
