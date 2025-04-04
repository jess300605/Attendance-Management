package com.attendance.controller;

import com.attendance.model.Attendance;
import com.attendance.model.AttendanceSession;
import com.attendance.service.AttendanceSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance-sessions")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AttendanceSessionController {

    private final AttendanceSessionService attendanceSessionService;

    @Autowired
    public AttendanceSessionController(AttendanceSessionService attendanceSessionService) {
        this.attendanceSessionService = attendanceSessionService;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceSession>> getAllAttendanceSessions() {
        try {
            List<AttendanceSession> sessions = attendanceSessionService.getAllAttendanceSessions();
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceSession> getAttendanceSessionById(@PathVariable Long id) {
        try {
            return attendanceSessionService.getAttendanceSessionById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<AttendanceSession>> getAttendanceSessionsByClassroom(@PathVariable Long classroomId) {
        try {
            List<AttendanceSession> sessions = attendanceSessionService.getAttendanceSessionsByClassroom(classroomId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/classroom/{classroomId}/date/{date}")
    public ResponseEntity<AttendanceSession> getAttendanceSessionByClassroomAndDate(
            @PathVariable Long classroomId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return attendanceSessionService.getAttendanceSessionByClassroomAndDate(classroomId, date)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/classroom/{classroomId}")
    public ResponseEntity<AttendanceSession> createAttendanceSession(
            @PathVariable Long classroomId,
            @RequestBody AttendanceSession session) {
        try {
            AttendanceSession createdSession = attendanceSessionService.createAttendanceSession(classroomId, session);

            if (createdSession != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{sessionId}/attendance")
    public ResponseEntity<List<Attendance>> takeAttendanceForSession(
            @PathVariable Long sessionId,
            @RequestBody List<Attendance> attendanceRecords) {
        try {
            List<Attendance> savedRecords = attendanceSessionService.takeAttendanceForSession(sessionId, attendanceRecords);

            if (savedRecords != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedRecords);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceSession> updateAttendanceSession(
            @PathVariable Long id,
            @RequestBody AttendanceSession session) {
        try {
            AttendanceSession updatedSession = attendanceSessionService.updateAttendanceSession(id, session);

            if (updatedSession != null) {
                return ResponseEntity.ok(updatedSession);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendanceSession(@PathVariable Long id) {
        try {
            return attendanceSessionService.getAttendanceSessionById(id)
                    .map(session -> {
                        attendanceSessionService.deleteAttendanceSession(id);
                        return ResponseEntity.noContent().<Void>build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}



