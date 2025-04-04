package com.attendance.controller;

import com.attendance.model.Classroom;
import com.attendance.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ClassroomController {

    private final ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        try {
            List<Classroom> classrooms = classroomService.getAllClassrooms();
            System.out.println("Returning " + classrooms.size() + " classrooms");
            return ResponseEntity.ok(classrooms);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable Long id) {
        try {
            return classroomService.getClassroomById(id)
                    .map(classroom -> {
                        System.out.println("Classroom found: " + classroom.getName());
                        if (classroom.getStudents() != null) {
                            System.out.println("Students count: " + classroom.getStudents().size());
                        } else {
                            System.out.println("Students list is null");
                        }
                        return ResponseEntity.ok(classroom);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Classroom>> getClassroomsByTeacher(@PathVariable Long teacherId) {
        try {
            List<Classroom> classrooms = classroomService.getClassroomsByTeacher(teacherId);
            System.out.println("Returning " + classrooms.size() + " classrooms for teacher " + teacherId);
            return ResponseEntity.ok(classrooms);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom classroom) {
        try {
            Classroom savedClassroom = classroomService.saveClassroom(classroom);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClassroom);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Classroom> updateClassroom(@PathVariable Long id, @RequestBody Classroom classroom) {
        try {
            return classroomService.getClassroomById(id)
                    .map(existingClassroom -> {
                        classroom.setId(id);
                        return ResponseEntity.ok(classroomService.saveClassroom(classroom));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        try {
            return classroomService.getClassroomById(id)
                    .map(classroom -> {
                        classroomService.deleteClassroom(id);
                        return ResponseEntity.noContent().<Void>build();
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{classroomId}/students/{studentId}")
    public ResponseEntity<Classroom> addStudentToClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long studentId) {
        try {
            Classroom updatedClassroom = classroomService.addStudentToClassroom(classroomId, studentId);

            if (updatedClassroom != null) {
                return ResponseEntity.ok(updatedClassroom);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{classroomId}/students/{studentId}")
    public ResponseEntity<Classroom> removeStudentFromClassroom(
            @PathVariable Long classroomId,
            @PathVariable Long studentId) {
        try {
            Classroom updatedClassroom = classroomService.removeStudentFromClassroom(classroomId, studentId);

            if (updatedClassroom != null) {
                return ResponseEntity.ok(updatedClassroom);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

