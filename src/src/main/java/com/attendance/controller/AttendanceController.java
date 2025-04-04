package com.attendance.controller;

import com.attendance.model.Attendance;
import com.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {
    
    private final AttendanceService attendanceService;
    
    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
    
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        return attendanceService.getAttendanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<Attendance> attendanceList = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(attendanceList);
    }
    
    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<List<Attendance>> getAttendanceByTeacher(@PathVariable Long teacherId) {
        List<Attendance> attendanceList = attendanceService.getAttendanceByTeacher(teacherId);
        return ResponseEntity.ok(attendanceList);
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Attendance> attendanceList = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendanceList);
    }
    
    @GetMapping("/students/{studentId}/between")
    public ResponseEntity<List<Attendance>> getStudentAttendanceBetweenDates(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Attendance> attendanceList = attendanceService.getStudentAttendanceBetweenDates(studentId, startDate, endDate);
        return ResponseEntity.ok(attendanceList);
    }
    
    @GetMapping("/teachers/{teacherId}/between")
    public ResponseEntity<List<Attendance>> getTeacherAttendanceBetweenDates(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Attendance> attendanceList = attendanceService.getTeacherAttendanceBetweenDates(teacherId, startDate, endDate);
        return ResponseEntity.ok(attendanceList);
    }
    
    @PostMapping
    public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
        Attendance savedAttendance = attendanceService.saveAttendance(attendance);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAttendance);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        return attendanceService.getAttendanceById(id)
                .map(existingAttendance -> {
                    attendance.setId(id);
                    return ResponseEntity.ok(attendanceService.saveAttendance(attendance));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        return attendanceService.getAttendanceById(id)
                .map(attendance -> {
                    attendanceService.deleteAttendance(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

