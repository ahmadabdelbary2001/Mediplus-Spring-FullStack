package org.mediplus.repository;

import org.mediplus.model.Doctor;
import org.mediplus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query("SELECT d FROM Doctor d")
    List<Doctor> findAllDoctors();
}