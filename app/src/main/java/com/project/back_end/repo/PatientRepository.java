package com.project.back_end.repo;
import java.util.Optional;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmailOrPhone(String email, String phone);
    Optional<Patient> findByEmail(String email);
}
