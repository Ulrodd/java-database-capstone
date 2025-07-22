package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Trouver un médecin par email
    Doctor findByEmail(String email);

    // Recherche par nom partiel
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> findByNameLike(@Param("name") String name);

    // Recherche par nom partiel et spécialité
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(d.specialty) = LOWER(:specialty)")
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(@Param("name") String name, @Param("specialty") String specialty);

    // Recherche uniquement par nom partiel
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> findByNameContainingIgnoreCase(@Param("name") String name);

    // Recherche par spécialité uniquement
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
