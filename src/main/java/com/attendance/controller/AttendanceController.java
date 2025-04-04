package com.attendance.controller;

import com.attendance.model.Attendance;
import com.attendance.model.Student;
import com.attendance.model.Teacher;
import com.attendance.service.AttendanceService;
import com.attendance.service.StudentService;
import com.attendance.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public AttendanceController(
            AttendanceService attendanceService,
            StudentService studentService,
            TeacherService teacherService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        try {
            return ResponseEntity.ok(attendanceService.getAllAttendance());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        try {
            return attendanceService.getAttendanceById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        try {
            List<Attendance> attendanceList = attendanceService.getAttendanceByStudent(studentId);
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<List<Attendance>> getAttendanceByTeacher(@PathVariable Long teacherId) {
        try {
            List<Attendance> attendanceList = attendanceService.getAttendanceByTeacher(teacherId);
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Attendance> attendanceList = attendanceService.getAttendanceByDate(date);
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/students/{studentId}/between")
    public ResponseEntity<List<Attendance>> getStudentAttendanceBetweenDates(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Attendance> attendanceList = attendanceService.getStudentAttendanceBetweenDates(studentId, startDate, endDate);
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/teachers/{teacherId}/between")
    public ResponseEntity<List<Attendance>> getTeacherAttendanceBetweenDates(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Attendance> attendanceList = attendanceService.getTeacherAttendanceBetweenDates(teacherId, startDate, endDate);
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createAttendance(@RequestBody Attendance attendance) {
        try {
            // Verificar si es asistencia de estudiante
            if (attendance.getType() == Attendance.AttendanceType.STUDENT) {
                if (attendance.getStudent() == null || attendance.getStudent().getId() == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Se requiere un ID de estudiante válido"));
                }

                // Obtener el estudiante
                Optional<Student> studentOpt = studentService.getStudentById(attendance.getStudent().getId());
                if (studentOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Estudiante no encontrado"));
                }

                // Establecer el estudiante completo
                attendance.setStudent(studentOpt.get());
            }

            // Verificar si es asistencia de profesor
            if (attendance.getType() == Attendance.AttendanceType.TEACHER) {
                if (attendance.getTeacher() == null || attendance.getTeacher().getId() == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Se requiere un ID de profesor válido"));
                }

                // Obtener el profesor
                Optional<Teacher> teacherOpt = teacherService.getTeacherById(attendance.getTeacher().getId());
                if (teacherOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Profesor no encontrado"));
                }

                // Establecer el profesor completo
                attendance.setTeacher(teacherOpt.get());
            }

            // Guardar la asistencia
            Attendance savedAttendance = attendanceService.saveAttendance(attendance);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAttendance);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear la asistencia: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        try {
            return attendanceService.getAttendanceById(id)
                    .map(existingAttendance -> {
                        attendance.setId(id);
                        return ResponseEntity.ok(attendanceService.saveAttendance(attendance));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        try {
            return attendanceService.getAttendanceById(id)
                    .map(attendance -> {
                        attendanceService.deleteAttendance(id);
                        return ResponseEntity.noContent().<Void>build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

