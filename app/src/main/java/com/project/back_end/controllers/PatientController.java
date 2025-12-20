package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}patient")
public class PatientController {

    private final PatientService patientService;
    private final AuthService service;

    public PatientController(PatientService patientService, AuthService service) {
        this.patientService = patientService;
        this.service = service;
    }

    // 3. Get patient details
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(@PathVariable String token) {

        ResponseEntity<?> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }

        return ResponseEntity.ok(
                patientService.getPatientByToken(token)
        );
    }

    // 4. Create patient
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(
            @Valid @RequestBody Patient patient) {

        boolean isValid = service.validatePatient(patient);

        if (!isValid) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Patient already exists"));
        }

        boolean created = patientService.savePatient(patient);

        if (!created) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not create patient"));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Patient created successfully"));
    }

    // 5. Patient login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody Login login) {

        return service.validatePatientLogin(
                login.getEmail(),
                login.getPassword()
        );
    }

    // 6. Get patient appointments
    @GetMapping("/appointments/{user}/{patientId}/{token}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable String user,
            @PathVariable Long patientId,
            @PathVariable String token) {

        ResponseEntity<?> tokenValidation = service.validateToken(token, user);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }

        return ResponseEntity.ok(
                patientService.getAppointmentsByPatientId(patientId)
        );
    }

    // 7. Filter patient appointments
    @GetMapping("/appointments/filter/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String name,
            @PathVariable String token) {

        ResponseEntity<?> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }

        return ResponseEntity.ok(
                service.filterPatient(token, condition, name)
        );
    }
}
