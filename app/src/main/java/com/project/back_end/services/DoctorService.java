package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public List<LocalTime> getDoctorAvailability(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) return List.of();

        List<LocalTime> bookedTimes = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(
                        doctorId,
                        date.atStartOfDay(),
                        date.atTime(23, 59)
                )
                .stream()
                .map(a -> a.getAppointmentTime().toLocalTime())
                .collect(Collectors.toList());

        return doctor.getAvailableTimes()
                .stream()
                .map(LocalTime::parse)
                .filter(time -> !bookedTimes.contains(time))
                .collect(Collectors.toList());
    }

    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) return -1;
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.existsById(doctor.getId())) return -1;
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(Long doctorId) {
        try {
            if (!doctorRepository.existsById(doctorId)) return -1;
            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String validateDoctor(String email, String password) {
        try {
            Doctor doctor = doctorRepository.findByEmail(email);
            if (doctor == null || !doctor.getPassword().equals(password)) return null;
            return tokenService.generateToken(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= Filtros =================
    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Doctor> filterDoctorsByNameSpecilityandTime(
            String name, String specialty, String time
    ) {
        return filterDoctorByTime(
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty),
                time
        );
    }

    public List<Doctor> filterDoctorByNameAndTime(String name, String time) {
        return filterDoctorByTime(doctorRepository.findByNameContainingIgnoreCase(name), time);
    }

    public List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    public List<Doctor> filterDoctorByTimeAndSpecility(String specialty, String time) {
        return filterDoctorByTime(doctorRepository.findBySpecialtyIgnoreCase(specialty), time);
    }

    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    public List<Doctor> filterDoctorsByTime(String time) {
        return filterDoctorByTime(doctorRepository.findAll(), time);
    }

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String time) {
        return doctors.stream()
                .filter(d -> d.getAvailableTimes().stream()
                        .map(LocalTime::parse)
                        .anyMatch(t -> isTimeMatch(t, time)))
                .collect(Collectors.toList());
    }

    private boolean isTimeMatch(LocalTime time, String period) {
        if ("AM".equalsIgnoreCase(period)) return time.isBefore(LocalTime.NOON);
        if ("PM".equalsIgnoreCase(period)) return !time.isBefore(LocalTime.NOON);
        return false;
    }
}
