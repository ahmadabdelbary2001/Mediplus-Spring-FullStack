package org.mediplus.repository;

import org.mediplus.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
     List<Doctor> findBySpecialization(String specialization);
}
