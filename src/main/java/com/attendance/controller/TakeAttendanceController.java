package com.attendance.controller;
import com.attendance.model.Attendance;
import com.attendance.model.AttendanceSession;
import com.attendance.model.Classroom;
import com.attendance.model.Student;
import com.attendance.service.AttendanceSessionService;
import com.attendance.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/take-attendance")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class TakeAttendanceController {

    @Autowired
    private AttendanceSessionService attendanceSessionService;

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getSessionDetails(@PathVariable Long sessionId) {
        try {
            Optional<AttendanceSession> sessionOpt = attendanceSessionService.getAttendanceSessionById(sessionId);

            if (sessionOpt.isPresent()) {
                AttendanceSession session = sessionOpt.get();
                Classroom classroom = session.getClassroom();

                if (classroom.getStudents() == null || classroom.getStudents().isEmpty()) {
                    Map<String, String> response = new HashMap<>();
                    response.put("error", "No hay estudiantes asignados a este salón");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                return ResponseEntity.ok(session);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<?> getClassroomStudents(@PathVariable Long classroomId) {
        try {
            Optional<Classroom> classroomOpt = classroomService.getClassroomById(classroomId);

            if (classroomOpt.isPresent()) {
                Classroom classroom = classroomOpt.get();

                if (classroom.getStudents() == null || classroom.getStudents().isEmpty()) {
                    Map<String, String> response = new HashMap<>();
                    response.put("error", "No hay estudiantes asignados a este salón");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                return ResponseEntity.ok(classroom.getStudents());
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

