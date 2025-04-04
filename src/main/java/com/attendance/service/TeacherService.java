package com.attendance.service;

import com.attendance.model.Attendance;
import com.attendance.model.Teacher;
import com.attendance.model.TeacherLogin;
import com.attendance.model.TeacherLoginResponse;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, AttendanceRepository attendanceRepository) {
        this.teacherRepository = teacherRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> getTeacherByEmployeeId(String employeeId) {
        return teacherRepository.findByEmployeeId(employeeId);
    }

    public Optional<Teacher> getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    public TeacherLoginResponse login(TeacherLogin loginRequest) {
        try {
            System.out.println("Login request: " + loginRequest);

            if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                System.out.println("Login request is invalid");
                return null;
            }

            Optional<Teacher> teacherOpt = teacherRepository.findByEmail(loginRequest.getEmail());
            System.out.println("Teacher found: " + teacherOpt.isPresent());

            if (teacherOpt.isPresent()) {
                Teacher teacher = teacherOpt.get();
                System.out.println("Teacher password: " + teacher.getPassword());
                System.out.println("Login password: " + loginRequest.getPassword());

                if (teacher.getPassword() != null && teacher.getPassword().equals(loginRequest.getPassword())) {
                    // Crear respuesta simplificada
                    TeacherLoginResponse response = new TeacherLoginResponse();
                    response.setId(teacher.getId());
                    response.setFirstName(teacher.getFirstName());
                    response.setLastName(teacher.getLastName());
                    response.setEmail(teacher.getEmail());
                    response.setEmployeeId(teacher.getEmployeeId());
                    response.setToken(UUID.randomUUID().toString());
                    response.setAttendanceRegistered(true);

                    return response;
                }
            }

            return null;
        } catch (Exception e) {
            System.out.println("Error in login: " + e.getMessage());
            e.printStackTrace();
            return null; // Devolver null en lugar de lanzar la excepción
        }
    }
}

