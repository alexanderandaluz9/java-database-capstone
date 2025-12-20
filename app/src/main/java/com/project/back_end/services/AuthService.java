package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Validación login admin
    public Optional<Admin> validateAdminLogin(Login login) {
        return adminRepository.findByUsername(login.getUsername())
                .filter(admin -> admin.getPassword().equals(login.getPassword()));
    }

    // Validación login paciente
    public Optional<Patient> validatePatientLogin(Login login) {
        return patientRepository.findByEmailOrPhone(login.getUsername(), login.getUsername())
                .filter(patient -> patient.getPassword().equals(login.getPassword()));
    }
}

