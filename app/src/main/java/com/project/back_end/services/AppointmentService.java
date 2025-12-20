package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public List<Appointment> getAppointmentsByDateAndPatient(String date, String patientName) {
        LocalDateTime start = LocalDateTime.parse(date + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(date + "T23:59:59");
        return appointmentRepository.findByPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(patientName, start, end);
    }

    @Transactional
    public int updateAppointment(Long appointmentId, Long doctorId, LocalDateTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) return -1;

        appointment.setDoctorId(doctorId);
        appointment.setAppointmentTime(newTime);
        appointmentRepository.save(appointment);
        return 1;
    }

    @Transactional
    public int cancelAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findByIdAndPatientId(appointmentId, patientId);
        if (appointment == null) return -1;

        appointment.setStatus(2); // Cancelado
        appointmentRepository.save(appointment);
        return 1;
    }

    @Transactional
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Transactional
    public int updateStatus(int status, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) return -1;

        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        return 1;
    }
}
