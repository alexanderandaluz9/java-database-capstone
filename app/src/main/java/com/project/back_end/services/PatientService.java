package com.project.back_end.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public List<AppointmentDTO> getPatientAppointment(Long patientId) {
        try {
            return appointmentRepository.findByPatientId(patientId)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<AppointmentDTO> filterByCondition(String condition, Long patientId) {
        try {
            int status;

            if ("future".equalsIgnoreCase(condition)) status = 0;
            else if ("past".equalsIgnoreCase(condition)) status = 1;
            else throw new IllegalArgumentException("Invalid condition");

            return appointmentRepository
                    .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(patientId, status)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<AppointmentDTO> filterByDoctor(String doctorName, Long patientId) {
        try {
            return appointmentRepository
                    .filterByDoctorNameAndPatientId(doctorName, patientId)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<AppointmentDTO> filterByDoctorAndCondition(
            String doctorName,
            String condition,
            Long patientId
    ) {
        try {
            int status;
            if ("future".equalsIgnoreCase(condition)) status = 0;
            else if ("past".equalsIgnoreCase(condition)) status = 1;
            else throw new IllegalArgumentException("Invalid condition");

            return appointmentRepository
                    .filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Patient getPatientDetails(String token) {
        try {
            String email = tokenService.extractEmail(token);
            return patientRepository.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==================== Mapeo a DTO ====================
    private AppointmentDTO mapToDTO(Appointment a) {
        return new AppointmentDTO(
                a.getId(),
                a.getDoctor().getId(),
                a.getDoctor().getName(),
                a.getPatient().getId(),
                a.getPatient().getName(),
                a.getDoctor().getSpecialty(),
                a.getDoctor().getEmail(),
                a.getPatient().getEmail(),
                a.getAppointmentTime(),
                a.getStatus()
        );
    }
}
