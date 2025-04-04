package com.attendance.service;

import com.attendance.model.Grade;
import com.attendance.model.Student;
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
    
    @Autowired
    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
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
    
    public List<Grade> getGradesByStudentAndSubject(Long studentId, String subject) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.map(s -> gradeRepository.findByStudentAndSubject(s, subject)).orElse(List.of());
    }
    
    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }
    
    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
}

