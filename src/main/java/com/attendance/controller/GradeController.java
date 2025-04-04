package com.attendance.controller;

import com.attendance.model.Grade;
import com.attendance.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GradeController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        return gradeService.getGradeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesByStudent(studentId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/classrooms/{classroomId}")
    public ResponseEntity<List<Grade>> getGradesByClassroom(@PathVariable Long classroomId) {
        List<Grade> grades = gradeService.getGradesByClassroom(classroomId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/students/{studentId}/classrooms/{classroomId}")
    public ResponseEntity<List<Grade>> getGradesByStudentAndClassroom(
            @PathVariable Long studentId,
            @PathVariable Long classroomId) {
        List<Grade> grades = gradeService.getGradesByStudentAndClassroom(studentId, classroomId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/students/{studentId}/subjects/{subject}")
    public ResponseEntity<List<Grade>> getGradesByStudentAndSubject(
            @PathVariable Long studentId,
            @PathVariable String subject) {
        List<Grade> grades = gradeService.getGradesByStudentAndSubject(studentId, subject);
        return ResponseEntity.ok(grades);
    }

    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestBody Grade grade) {
        Grade savedGrade = gradeService.saveGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Grade>> createGrades(@RequestBody List<Grade> grades) {
        List<Grade> savedGrades = gradeService.saveAllGrades(grades);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrades);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade grade) {
        return gradeService.getGradeById(id)
                .map(existingGrade -> {
                    grade.setId(id);
                    return ResponseEntity.ok(gradeService.saveGrade(grade));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        return gradeService.getGradeById(id)
                .map(grade -> {
                    gradeService.deleteGrade(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

