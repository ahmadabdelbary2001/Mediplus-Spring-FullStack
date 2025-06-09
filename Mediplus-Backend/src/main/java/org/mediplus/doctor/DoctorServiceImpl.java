package org.mediplus.doctor;

import lombok.extern.slf4j.Slf4j;
import org.mediplus.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        log.info("Initializing Doctor Service");
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        Long id = doctor.getId();
        if (id == null) {
            throw new ResourceNotFoundException("Doctor ID is missing");
        }

        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));

        existing.setUsername(doctor.getUsername());
        existing.setEmail(doctor.getEmail());
        existing.setSpecialization(doctor.getSpecialization());
        existing.setLicenseNumber(doctor.getLicenseNumber());
        existing.setClinicLocation(doctor.getClinicLocation());
        if (doctor.getPassword() != null && !doctor.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(doctor.getPassword()));
        }
        existing.setTermsAccepted(doctor.getTermsAccepted());

        return doctorRepository.save(existing);
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
    }
}
