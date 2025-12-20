package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AuthService service;

    public AppointmentController(AppointmentService appointmentService, AuthService service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // GET appointments (doctor only)
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {

        ResponseEntity<?> validation = service.validateToken(token, "doctor");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return validation;
        }

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDateAndPatient(date, patientName)
        );
    }

    // BOOK appointment (patient only)
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<?> validation = service.validateToken(token, "patient");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return (ResponseEntity<Map<String, String>>) validation;
        }

        int result = service.validateAppointment(appointment);

        if (result == -1) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Doctor not found"));
        }

        if (result == 0) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Appointment time not available"));
        }

        appointmentService.bookAppointment(appointment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Appointment booked successfully"));
    }

    // UPDATE appointment (patient only)
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<?> validation = service.validateToken(token, "patient");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return (ResponseEntity<Map<String, String>>) validation;
        }

        boolean updated = appointmentService.updateAppointment(appointment);

        if (!updated) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Appointment not found"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Appointment updated successfully")
        );
    }

    // CANCEL appointment (patient only)
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable Long id,
            @PathVariable String token) {

        ResponseEntity<?> validation = service.validateToken(token, "patient");
        if (validation.getStatusCode() != HttpStatus.OK) {
            return (ResponseEntity<Map<String, String>>) validation;
        }

        boolean deleted = appointmentService.cancelAppointment(id);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Appointment not found"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Appointment cancelled successfully")
        );
    }
}
