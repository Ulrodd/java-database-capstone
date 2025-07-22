package com.example.medicalapp.repository;

import com.example.medicalapp.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Trouver un médecin par email
    Doctor findByEmail(String email);

    // Recherche par nom partiel avec LIKE et CONCAT pour appariement flexible
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> findByNameLike(@Param("name") String name);

    // Filtrer par nom partiel et spécialité exacte, insensible à la casse
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(d.specialty) = LOWER(:specialty)")
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(@Param("name") String name, @Param("specialty") String specialty);

    // Trouver par spécialité insensible à la casse
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
