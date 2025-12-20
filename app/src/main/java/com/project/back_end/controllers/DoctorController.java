package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final AuthService service;

    public DoctorController(DoctorService doctorService, AuthService service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // 3. Get doctor availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        ResponseEntity<?> tokenValidation = service.validateToken(token, user);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }

        return ResponseEntity.ok(
                doctorService.getDoctorAvailability(doctorId, date)
        );
    }

    // 4. Get all doctors
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        return ResponseEntity.ok(
                Map.of("doctors", doctorService.getAllDoctors())
        );
    }

    // 5. Save doctor (admin only)
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(
            @Valid @RequestBody Doctor doctor,
            @PathVariable String token) {

        ResponseEntity<?> tokenValidation = service.validateToken(token, "admin");
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return (ResponseEntity<Map<String, String>>) tokenValidation;
        }

        boolean exists = doctorService.existsByEmail(doctor.getEmail());
        if (exists) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Doctor already exists"));
        }

        doctorService.saveDoctor(doctor);
        return ResponseEntity.ok(Map.of("message", "Doctor saved successfully"));
    }

    // 6. Doctor login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(
            @Valid @RequestBody Login login) {

        return doctorService.login(login);
    }

    // 7. Update doctor (admin only)
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @Valid @RequestBody Doctor doctor,
            @PathVariable String token) {

        ResponseEntity<?> tokenValidation = service.validateToken(token, "admin");
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return (ResponseEntity<Map<String, String>>) tokenValidation;
        }

        boolean updated = doctorService.updateDoctor(doctor);

        if (!updated) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Doctor not found"));
        }

        return ResponseEntity.ok(Map.of("message", "Doctor updated successfully"));
    }

    // 8. Delete doctor (admin only)
    @DeleteMapping("/{doctorId}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token) {

        ResponseEntity<?> tokenValidation = service.validateToken(token, "admin");
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return (ResponseEntity<Map<String, String>>) tokenValidation;
        }

        boolean deleted = doctorService.deleteDoctor(doctorId);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Doctor not found"));
        }

        return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully"));
    }

    // 9. Filter doctors
    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String specialty) {

        return ResponseEntity.ok(
                Map.of("doctors", service.filterDoctor(name, specialty, time))
        );
    }
}
