package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Trouver les rendez-vous d'un docteur dans une plage horaire (avec jointure pour charger la dispo du doc)
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor.availableTimes WHERE a.doctor.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(@Param("doctorId") Long doctorId,
                                                             @Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end);

    // Trouver les rendez-vous par docteur, nom partiel de patient (case insensitive) et plage horaire
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH a.patient p " +
           "WHERE d.id = :doctorId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%')) " +
           "AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("patientName") String patientName,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Supprimer tous les rendez-vous liés à un docteur
    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.doctor.id = :doctorId")
    void deleteAllByDoctorId(@Param("doctorId") Long doctorId);

    // Trouver tous les rendez-vous d'un patient
    List<Appointment> findByPatientId(Long patientId);

    // Trouver les rendez-vous d'un patient par statut, triés par heure
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    // Filtrer rendez-vous par nom partiel de docteur et ID patient
    @Query("SELECT a FROM Appointment a JOIN a.doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND a.patient.id = :patientId")
    List<Appointment> filterByDoctorNameAndPatientId(@Param("doctorName") String doctorName,
                                                    @Param("patientId") Long patientId);

    // Filtrer rendez-vous par nom partiel de docteur, ID patient et statut
    @Query("SELECT a FROM Appointment a JOIN a.doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND a.patient.id = :patientId AND a.status = :status")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(@Param("doctorName") String doctorName,
                                                             @Param("patientId") Long patientId,
                                                             @Param("status") int status);

    // Mettre à jour le statut d'un rendez-vous
    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(@Param("status") int status, @Param("id") long id);
}
