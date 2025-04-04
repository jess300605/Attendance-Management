package com.attendance.service;

import com.attendance.model.Classroom;
import com.attendance.model.Grade;
import com.attendance.model.Student;
import com.attendance.repository.ClassroomRepository;
import com.attendance.repository.GradeRepository;
import com.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository,
                        StudentRepository studentRepository,
                        ClassroomRepository classroomRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    public List<Grade> getGradesByStudent(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(gradeRepository::findByStudent).orElse(List.of());
    }

    public List<Grade> getGradesByClassroom(Long classroomId) {
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        return classroom.map(gradeRepository::findByClassroom).orElse(List.of());
    }

    public List<Grade> getGradesByStudentAndClassroom(Long studentId, Long classroomId) {
        Optional<Student> student = studentRepository.findById(studentId);
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);

        if (student.isPresent() && classroom.isPresent()) {
            return gradeRepository.findByStudentAndClassroom(student.get(), classroom.get());
        }

        return List.of();
    }

    public List<Grade> getGradesByStudentAndSubject(Long studentId, String subject) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(s -> gradeRepository.findByStudentAndSubject(s, subject)).orElse(List.of());
    }

    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public List<Grade> saveAllGrades(List<Grade> grades) {
        return gradeRepository.saveAll(grades);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
}

