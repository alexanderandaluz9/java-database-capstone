package com.project.back_end.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    Appointment findByIdAndPatientId(Long appointmentId, Long patientId);

    List<Appointment> findByPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(String patientName, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

}
