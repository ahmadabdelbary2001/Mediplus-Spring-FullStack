package org.mediplus.repository;

import org.mediplus.model.Doctor;
import org.mediplus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Method to find a user by their username
    Optional<User> findByUsername(String username);

    // Method to find a user by their email
    Optional<User> findByEmail(String email);

    // Method to check a user exists by their username. Should be named existsByUsername().
    boolean existsByUsername(String username);

    // Method to check a user exists by their email
    boolean existsByEmail(String email);

    @Query("SELECT d FROM Doctor d")
    List<Doctor> findAllDoctors();
}